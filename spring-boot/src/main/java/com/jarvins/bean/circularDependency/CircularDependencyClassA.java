package com.jarvins.bean.circularDependency;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class CircularDependencyClassA {

    @Autowired
    private CircularDependencyClassB b;

    public void methodA(){
        System.out.println("execute ClassA.methodA");
    }

    public void methodB(){
        b.methodB();
    }
}
