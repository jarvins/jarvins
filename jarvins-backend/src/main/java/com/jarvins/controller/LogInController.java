package com.jarvins.controller;


import com.jarvins.entity.Response;
import com.jarvins.entity.response.SuccessResponse;
import com.jarvins.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
@Slf4j
public class LogInController {

    LoginService loginService;

    public LogInController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public Response login(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam String secretKey) {
        return loginService.login(request,response,secretKey);
    }

    @GetMapping("/logout")
    public Response logout(HttpServletRequest request, HttpServletResponse response){
        loginService.logout(request,response);
        return SuccessResponse.success(true);
    }


    @GetMapping("/logged")
    public Response logged(HttpServletRequest request){
        boolean logged = loginService.logged(request);
        return SuccessResponse.success(logged);
    }

    @GetMapping("/background")
    public Response background(){
        String src = loginService.background();
        return SuccessResponse.success(src);
    }

}
