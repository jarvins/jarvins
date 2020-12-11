package com.jarvins.controller;

import com.jarvins.service.DataCreatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "数据生成器")
@RequestMapping("create")
public class DataCreatorController {

    private final DataCreatorService dataCreatorService;

    public DataCreatorController(DataCreatorService dataCreatorService) {
        this.dataCreatorService = dataCreatorService;
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户生成接口")
    public String createUser(@ApiParam int times){
        return dataCreatorService.createUser(times);
    }

    @GetMapping("/employee")
    @ApiOperation(value = "员工生成接口")
    public String createEmployee(@ApiParam int times){
        return dataCreatorService.createEmployee(times);
    }

    @GetMapping("/commodity")
    @ApiOperation(value = "商品生成接口")
    public String createCommodity(@ApiParam int times){
        return dataCreatorService.createCommodity(times);
    }

    @GetMapping("/shopping")
    @ApiOperation(value = "购物信息生成接口")
    public String createShopping(@ApiParam int times){
        return dataCreatorService.createShopping(times);
    }
}
