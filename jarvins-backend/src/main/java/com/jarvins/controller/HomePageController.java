package com.jarvins.controller;

import com.jarvins.entity.response.ErrorResponse;
import com.jarvins.entity.Response;
import com.jarvins.entity.response.SuccessResponse;
import com.jarvins.entity.dto.*;
import com.jarvins.service.HomepageService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jarvins.entity.response.ErrorEnum.*;

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

//    @GetMapping("/getMemorandum")
//    public Response getMemorandum() {
//        List<Memorandum> memorandum = homepageService.getMemorandum();
//        return SuccessResponse.success(memorandum);
//    }
//
//    @PostMapping("/addMemorandum")
//    public Response addMemorandum(@RequestBody Memorandum memorandum) {
//        if (homepageService.addMemorandum(memorandum)) {
//            return SuccessResponse.success(true);
//        }
//        return ErrorResponse.error(ADD_ERROR);
//    }
//
//    @GetMapping("/completeMemorandum")
//    public Response completeMemorandum(@RequestParam int id) {
//        if (homepageService.completeMemorandum(id)) {
//            return SuccessResponse.success(true);
//        }
//        return ErrorResponse.error(DElETE_ERROR);
//    }
//
//    @GetMapping("/getMonthlyBillStatistics")
//    public Response getMonthlyBillStatistics(@RequestParam String month) {
//        return SuccessResponse.success(homepageService.getBillMonthlyStatistics(month));
//    }
//
//    @GetMapping("/getDailyBillRecord")
//    public Response getBillDailyStatistics(@RequestParam String day) {
//        return SuccessResponse.success(homepageService.getDailyBillRecord(day));
//    }
//
//    @PostMapping("/addBillRecord")
//    public Response addBillRecord(@RequestBody BillRecord billRecord) {
//        if (homepageService.addDailyBill(billRecord)) {
//            return SuccessResponse.success(true);
//        }
//        return ErrorResponse.error(ADD_ERROR);
//    }

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
}
