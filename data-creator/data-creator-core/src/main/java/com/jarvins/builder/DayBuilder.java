package com.jarvins.builder;

import com.jarvins.Constant;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DayBuilder {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final Map<Integer,Integer> MONTH = new HashMap<Integer, Integer>(){{
        put(1,31);
        put(2,28);
        put(3,31);
        put(4,30);
        put(5,31);
        put(6,30);
        put(7,31);
        put(8,31);
        put(9,30);
        put(10,31);
        put(11,30);
        put(12,31);
    }};


    public static LocalDate build(int age){
        int year = Constant.YEAR -age;
        int month = RANDOM.nextInt(1,13);
        int day;
        if(month == 2 && (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)){
            day = RANDOM.nextInt(1,30);
        }
        else {
            day = RANDOM.nextInt(1, MONTH.get(month) + 1);
        }
        return LocalDate.of(year,month,day);
    }
}
