package com.jarvins.bean.circularDependency;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class CircularDependencyClassB {

    @Autowired
    CircularDependencyClassA a;

    public void methodA(){
        a.methodA();
    }

    public void methodB(){
        System.out.println("execute ClassB.methodB");
    }
}
