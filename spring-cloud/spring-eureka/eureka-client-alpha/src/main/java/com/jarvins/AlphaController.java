package com.jarvins;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlphaController {

    @GetMapping("/get")
    public String get() {
        return "get response from client alpha";
    }

    @PostMapping("/post")
    public String post(@RequestBody String name) {
        return "post response from client alpha with param name [" + name + "]";
    }

}
