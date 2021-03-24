package com.jarvins.configuration;

import com.jarvins.bean.BeanLifeCycleObject;
import com.jarvins.bean.SpringAopObject;
import com.jarvins.bean.circularDependency.CircularDependencyClassA;
import com.jarvins.bean.circularDependency.CircularDependencyClassB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@Slf4j
public class BeanConfiguration {

    //展示Bean生命周期方法调用
    @Bean(initMethod = "start", destroyMethod="destroy")
    public BeanLifeCycleObject testBean(){
        return new BeanLifeCycleObject();
    }

    //展示Spring AOP的方法调用
    @Bean
    public SpringAopObject springAopObject(){
        return new SpringAopObject();
    }


    //展示setter循环依赖注入
    @Bean
    public CircularDependencyClassA classA(){
        return new CircularDependencyClassA();
    }

    //展示setter循环依赖注入
    @Bean
    CircularDependencyClassB classB(){
       return new CircularDependencyClassB();
    }

}



