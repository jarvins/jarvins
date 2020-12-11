package com.jarvins;

import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) {
        Map<String,Integer> map = new HashMap<>();
        map.put("1",1);
        map.computeIfAbsent("2",k -> Integer.parseInt(k) + 1);
        System.out.println(map);
    }
}
