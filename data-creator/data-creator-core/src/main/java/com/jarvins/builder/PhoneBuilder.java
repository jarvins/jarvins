package com.jarvins.builder;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PhoneBuilder {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final String[] PHONE_START = {
            "134","135","136","137","138","139","147","150",
            "151","152","157","158","159","182","187","188",
            "130","131","132","155","156","185","186","145",
            "175","176"
    };

    public static String build(){
        String start = PHONE_START[RANDOM.nextInt(PHONE_START.length)];
        int end = RANDOM.nextInt(99999999);
        //这里有重复,使用位运算变换效果一般,多次使用效果一致,通过数据库唯一约束
        return start + String.format("%08d",end);
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 10000000; i++) {
            set.add(build());
        }
        System.out.println(set.size());
    }
}
