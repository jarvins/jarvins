package com.jarvins.controller;

import com.jarvins.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/third")
@Slf4j
public class ThirdController {

    @GetMapping("/background")
    public Response  background(){
        return null;
    }

}
