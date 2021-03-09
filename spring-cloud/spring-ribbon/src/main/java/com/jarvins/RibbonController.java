package com.jarvins;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class RibbonController {

    @Resource
    RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @GetMapping("/consumer")
    public  String consumer(){
        return restTemplate.getForObject("http://ALPHA-CLIENT/get", String.class);
    }


    @GetMapping("/selfConsumer")
    public String selfConsumer(){
        return restTemplate.getForObject("http://RIBBON-CLIENT/get", String.class);
    }

    @GetMapping("get")
    public String get() {
        return "get response from client ribbon";
    }

}
