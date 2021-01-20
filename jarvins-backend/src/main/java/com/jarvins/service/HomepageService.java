package com.jarvins.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jarvins.entity.dto.*;
import com.jarvins.entity.vo.LinksVo;
import com.jarvins.mapper.LinksMapper;
import com.jarvins.mapper.TimeLimeMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


@Slf4j
@Service
public class HomepageService {

    @Resource
    TimeLimeMapper timeLimeMapper;

    @Resource
    LinksMapper linksMapper;

    //微博爬虫地址
    private static final String WEIBO_URL = "https://s.weibo.com/top/summary?cate=realtimehot";

    //知乎爬虫地址
    private static final String ZHIHU_URL = "https://zhihu.com/billboard";

    //掘金爬虫地址
    private static final String JUEJIN_URL = "https://e.xitu.io/resources/gold";

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public List<WeiboDTO> weiboCrawl() {
        String weiboPrefix = "https://s.weibo.com";
        List<WeiboDTO> res = new ArrayList<>();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity(WEIBO_URL, String.class);
        String body = response.getBody();
        Document parse = Jsoup.parse(body);
        Element tbody = parse.getElementsByTag("tbody").get(0);
        List<Node> nodes = tbody.childNodes();
        for (Node node : nodes) {
            if (node instanceof Element) {
                WeiboDTO dto = new WeiboDTO();
                List<Node> childNodes = node.childNodes();
                for (Node childNode : childNodes) {
                    if (childNode.toString().startsWith("<td class=\"td-01 ranktop\">")) {
                        TextNode textNode = (TextNode) childNode.childNode(0);
                        dto.setRank(Integer.parseInt(textNode.text()));
                    } else if (childNode.childNodeSize() > 3 && childNode.toString().startsWith("<td class=\"td-02\">")) {
                        Element child = (Element) childNode;
                        Element element = child.getElementsByTag("a").get(0);
                        dto.setTitle(element.childNode(0).toString());
                        dto.setHref(weiboPrefix + element.attributes().get("href"));
                        Element span = child.getElementsByTag("span").get(0);
                        dto.setHotCount(Integer.parseInt(span.childNode(0).toString()));
                    }
                }
                if (dto.getRank() > 0) {
                    res.add(dto);
                }
            }
        }
        log.info("HomepageService.weiboCrawl: crawl weibo hot search success.");
        return res;
    }

    public List<ZhihuDTO> zhihuCrawl() {
        List<ZhihuDTO> res = new ArrayList<>();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity(ZHIHU_URL, String.class);
        String body = response.getBody();
        Document parse = Jsoup.parse(body);
        body = parse.getElementsByTag("body").get(0).getElementsByTag("script").get(1).childNode(0).attributes().get("data");
        JSONObject obj = JSON.parseObject(body);
        JSONArray array = obj.getJSONObject("initialState").getJSONObject("topstory").getJSONArray("hotList");
        int rank = 1;
        int lastHotCount = Integer.MAX_VALUE;
        for (Object o : array) {
            ZhihuDTO dto = new ZhihuDTO();
            JSONObject object = (JSONObject) o;
            JSONObject target = object.getJSONObject("target");
            dto.setAnswerCount(object.getJSONObject("feedSpecific").getInteger("answerCount"));
            dto.setRank(rank++);
            dto.setExtra(target.getJSONObject("excerptArea").getString("text"));
            dto.setTitle(target.getJSONObject("titleArea").getString("text"));
            dto.setHref(target.getJSONObject("link").getString("url"));
            dto.setHotCount(target.getJSONObject("metricsArea").getString("text").contains("万热度") ?
                    lastHotCount = Integer.parseInt(target.getJSONObject("metricsArea").getString("text").replace(" 万热度", String.valueOf(RANDOM.nextInt(1000, 9999)))) : lastHotCount - RANDOM.nextInt(10000, 99999));
            res.add(dto);

        }
        log.info("HomepageService.zhihuCrawl: crawl zhihu hot search success.");
        return res;
    }

    public List<TimeLine> getTimeLine() {
        List<Map<String, String>> list = timeLimeMapper.timeLine();
        Map<String, TimeLine> res = new HashMap<>();
        list.forEach(e -> {
            if (res.containsKey(e.get("date"))) {
                List<String> content = res.get(e.get("date")).getContent();
                content.add(e.get("content"));
            } else {
                List<String> contentList = new ArrayList<>();
                String date = e.get("date");
                String version = e.get("version");
                contentList.add(e.get("content"));
                res.put(e.get("date"), TimeLine.builder().date(date).version(version).content(contentList).build());
            }
        });
        return res.values().stream().sorted(Comparator.comparing(TimeLine::getDate)).collect(Collectors.toList());
    }

    public List<JuejinItem> getJuejin(String category, String order) {
        RestTemplate template = new RestTemplate();
        JSONObject param = new JSONObject().fluentPut("category", category).fluentPut("order", order).fluentPut("offset", 0).fluentPut("limit", 30);
        Map<String,Object> body = template.postForObject(JUEJIN_URL, param, Map.class);
        assert body != null;
        JSONObject result = new JSONObject(body);
        List<JuejinItem> collect = result.getJSONArray("data").toJavaList(JSONObject.class).stream().map(e ->
                JuejinItem.builder()
                        .collectionCount(e.getInteger("collectionCount"))
                        .title(e.getString("title"))
                        .url(e.getString("url"))
                        .user(e.getJSONObject("user").getString("username"))
                        .time(calTime(e.getJSONObject("date").getString("iso")))
                        .build()).collect(Collectors.toList());
        log.info("HomepageService.getJuejin: crawl Juejin article success.");
        return collect;
    }

    public LinksVo getLinks() {
        List<Links> links = linksMapper.links();
        Map<String, List<Links>> folders = links.stream()
                .filter(e -> e.getParent() != null)
                .collect(Collectors.groupingBy(Links::getParent));
        List<LinksVo.Link> linkList = links.stream().filter(e -> e.getParent() == null).map(e -> new LinksVo.Link(e.getName(), e.getLinks(),e.getIconLink())).collect(Collectors.toList());
        List<LinksVo.Folder> folderList = new ArrayList<>();
        folders.forEach((k,v) ->
                folderList.add(LinksVo.Folder.builder().folderName(k).links(v.stream().filter(e -> e.getName() != null).map(e -> new LinksVo.Link(e.getName(), e.getLinks(),e.getIconLink())).collect(Collectors.toList())).build()));
        return new LinksVo(linkList,folderList);
    }

    public boolean addLinks(String folderName, String name, String link){
        return linksMapper.addLinks( name, link,folderName) == 1;
    }

        private String calTime(String time) {
        LocalDateTime createTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        long secondBetween = Duration.between(createTime, LocalDateTime.now()).getSeconds();
        //一分钟以内
        if (secondBetween < 60) {
            return secondBetween + 1 + "秒前";
        }
        //一小时以内
        else if (secondBetween < 3600) {
            return secondBetween / 60 + 1 + "分钟前";
        }
        //一天以内
        else if (secondBetween < 3600 * 24) {
            return secondBetween / 3600 + 1 + "小时前";
            //一天以后
        } else {
            return secondBetween / (3600 * 24) + 1 + "天前";
        }
    }


}
