package com.jarvins.configuration;

import com.jarvins.intercept.MybatisInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {

    @Bean
    public MybatisInterceptor mybatisInterceptor(){
        return new MybatisInterceptor();
    }
}
