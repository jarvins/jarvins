package com.jarvins;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GateWayController {

    @PostMapping("post")
    public String post(){
        return "post response from get request by gate way.";
    }

}
