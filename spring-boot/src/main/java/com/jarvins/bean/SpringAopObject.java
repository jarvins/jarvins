package com.jarvins.bean;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于展示Spring AOP的类
 */
@Slf4j
public class SpringAopObject {

    public void firstMethod() {
        log.info("execute first method.");
    }

    public void secondMethod(){
        log.info("execute second method.");

    }

    public void thirdMethod(){
        log.info("execute third method.");

    }
}
