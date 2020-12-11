package com.jarvins.configuration;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Slf4j
@EnableOpenApi
@Configuration
public class SwaggerConfiguration {

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("数据生成器")
                .description("百万数据生成接口")
                .version("1.0.0")
                .build();
    }

    @Bean
    public Docket docket(ApiInfo apiInfo) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build();
    }

}
