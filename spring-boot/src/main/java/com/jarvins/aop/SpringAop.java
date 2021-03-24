package com.jarvins.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SpringAop {

    @Around("execution(* com.jarvins.bean.SpringAopObject.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Spring AOP: before execute method...");
        Object proceed = joinPoint.proceed();
        log.info("Spring AOP: after execute method...");
        return proceed;
    }
}
