package com.jarvins.builder;

public class ZodiacBuilder {

    private static final char[] ZODIAC = {'鼠','牛','虎','兔','龙','蛇','马','羊','猴','鸡','狗','猪'};

    public static char build(int year){
        return ZODIAC[(year - 1900)%12];
    }
}
