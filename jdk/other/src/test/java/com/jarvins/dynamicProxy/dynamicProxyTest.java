package com.jarvins.dynamicProxy;

import dynamicProxy.InvocationHandlerImpl;
import dynamicProxy.TargetClass;
import dynamicProxy.TargetClassImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class dynamicProxyTest {

    @Test
    public void testProxy(){
        TargetClass target = new TargetClassImpl();
        TargetClass proxy = (TargetClass)Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandlerImpl(target));
        proxy.say();
        proxy.doSomething();
    }
}
