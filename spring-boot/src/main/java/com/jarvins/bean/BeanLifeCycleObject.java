package com.jarvins.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;

/**
 * bean声明周期展示类
 */
@Slf4j
public class BeanLifeCycleObject implements BeanNameAware, BeanFactoryAware, BeanPostProcessor, InitializingBean {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("BeanFactoryAware executed setBeanFactory.");
    }

    @Override
    public void setBeanName(String s) {
        log.info("BeanNameAware executed setBeanName.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("InitializingBean executed afterPropertiesSet.");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("BeanPostProcessor executed postProcessAfterInitialization,beanName:{}", beanName);
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("BeanPostProcessor executed postProcessBeforeInitialization,beanName:{}", beanName);
        return bean;
    }

    @PostConstruct
    public void construct() {
        log.info("@PostConstruct executed construct.");
    }

    public void start() {
        log.info("init-method executed start.");
    }

    public void destroy() {
        log.info("init-method executed destroy.");
    }
}
