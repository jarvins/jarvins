package com.jarvins.controller;

import com.jarvins.entity.Response;
import com.jarvins.entity.response.ErrorResponse;
import com.jarvins.entity.response.SuccessResponse;
import com.jarvins.entity.dto.*;
import com.jarvins.entity.vo.LinksVo;
import com.jarvins.service.HomepageService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jarvins.entity.response.ErrorEnum.ADD_ERROR;


@RestController
@RequestMapping("/homepage")
public class HomePageController {

    HomepageService homepageService;

    public HomePageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping("/weibo")
    public Response weiboCrawl() {
        List<WeiboDTO> weiboDTOS = homepageService.weiboCrawl();
        return SuccessResponse.success(weiboDTOS);
    }

    @GetMapping("/zhihu")
    public Response zhihuCrawl() {
        List<ZhihuDTO> zhihuDTOS = homepageService.zhihuCrawl();
        return SuccessResponse.success(zhihuDTOS);
    }

    @GetMapping("/timeLine")
    public Response getTimeLine() {
        List<TimeLine> timeLine = homepageService.getTimeLine();
        return SuccessResponse.success(timeLine);
    }

    @GetMapping("/juejin")
    public Response getJuejun(@RequestParam String category, @RequestParam String order){
        List<JuejinItem> juejin = homepageService.getJuejin(category, order);
        return SuccessResponse.success(juejin);
    }

    @GetMapping("/links")
    public Response getLinks(){
        LinksVo links = homepageService.getLinks();
        return SuccessResponse.success(links);
    }

    @GetMapping("/addLinks")
    public Response addLinks(@RequestParam @Nullable String folderName,@RequestParam @Nullable String name, @RequestParam @Nullable String link){
        if(homepageService.addLinks(folderName,name,link)){
            return SuccessResponse.success(null);
        }
        return ErrorResponse.error(ADD_ERROR);
    }
}
