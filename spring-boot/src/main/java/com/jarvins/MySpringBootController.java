package com.jarvins;

import com.jarvins.bean.SpringAopObject;
import com.jarvins.bean.circularDependency.CircularDependencyClassA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MySpringBootController {

    @Autowired
    SpringAopObject springAopObject;

    @Autowired
    CircularDependencyClassA classA;

    @GetMapping("/getAopRequest")
    public void getAopRequest(){
        springAopObject.firstMethod();
        springAopObject.secondMethod();
        springAopObject.thirdMethod();
    }

    @GetMapping("/getCircularDependency")
    public void getCircularDependency(){
        classA.methodB();
    }

}
