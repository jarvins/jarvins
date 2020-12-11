package com.jarvins;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.jarvins.mapper"})
public class DataCreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCreatorApplication.class,args);
    }
}
