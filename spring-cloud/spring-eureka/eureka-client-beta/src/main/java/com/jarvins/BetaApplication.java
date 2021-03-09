package com.jarvins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BetaApplication.class);
    }
}
