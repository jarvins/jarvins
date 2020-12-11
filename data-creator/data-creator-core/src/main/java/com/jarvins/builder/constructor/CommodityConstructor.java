package com.jarvins.builder.constructor;

import com.jarvins.entity.Commodity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.jarvins.Constant.URL;
import static com.jarvins.builder.CommodityBuilder.COMMODITY_SEARCH_ITEM;


@Slf4j
public class CommodityConstructor {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static List<Commodity> create() {
        RestTemplate template = new RestTemplate();
        String searchTitle = COMMODITY_SEARCH_ITEM[RANDOM.nextInt(COMMODITY_SEARCH_ITEM.length)];
        String url = String.format(URL, searchTitle, 1, 99999, RANDOM.nextInt(1, 50));

        LinkedHashMap<String, Object> result = template.getForObject(url, LinkedHashMap.class);
        assert result != null;
        List<Commodity> list = new ArrayList<>();
        String errorCode = (String) result.get("error_code");
        //调用成功
        if ("0000".equals(errorCode)) {
            Map<String, Object> items = (LinkedHashMap<String, Object>) result.get("items");
            List<LinkedHashMap<String, Object>> array = (ArrayList) items.get("item");
            array.forEach(e -> {
                String productName = (String) e.get("title");
                String productCode = String.valueOf(e.get("num_iid"));
                BigDecimal productPrice = new BigDecimal((String) e.get("price"));
                String area = (String) e.get("area");
                int sales = (int) e.get("sales");
                String seller = (String) e.get("seller_nick");
                int inventory = productPrice.compareTo(new BigDecimal("10000")) >= 0 ? RANDOM.nextInt(10, 100) :
                        productPrice.compareTo(new BigDecimal("5000")) >= 0 ? RANDOM.nextInt(10, 300) :
                                productPrice.compareTo(new BigDecimal("2000")) >= 0 ? RANDOM.nextInt(10, 500) :
                                        productPrice.compareTo(new BigDecimal("500")) >= 0 ? RANDOM.nextInt(100, 800) :
                                                productPrice.compareTo(new BigDecimal("200")) >= 0 ? RANDOM.nextInt(100, 1000) :
                                                        RANDOM.nextInt(100, 10000);

                Commodity build = Commodity.builder()
                        .productType(searchTitle)
                        .productName(productName)
                        .productCode(productCode)
                        .productPrice(productPrice)
                        .goodsSupply(area)
                        .inventory(inventory)
                        .seller(seller)
                        .salas(sales)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                list.add(build);
            });
            return list;
        }
        //调用失败
        log.warn("remote interface request occurred error,retry.");
        return create();
    }

    public static void main(String[] args) {
        List<Commodity> build = create();
        System.out.println(build);
    }
}
