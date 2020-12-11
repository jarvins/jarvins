package com.jarvins.swordFingerOffer;

/**
 * 将String转换成数字，考虑边界问题:
 * 1,不合法输入([0,9])
 * 2,第一个负号问题
 * 2,边界问题 (Integer.MIN_VALUE + 1 = Integer.MAX_VALUE)
 */
public class No_2_StrToInt {

    private static int strToInt(String str) {
        char[] chars = str.toCharArray();
        long result = 0;
        boolean flag = chars[0] == '-';
        int i = flag ? 1 : 0;
        for (; i < chars.length; i++) {
            if (chars[i] - '0' > 9
                    || chars[i] - '0' < 0
                    || (!flag && result * 10 + chars[i] - '0' > Integer.MAX_VALUE)
                    || (flag && result * 10 + chars[i] - '0' > (long) Integer.MAX_VALUE + 1)) {
                throw new NumberFormatException("for input string " + str);
            }
            result = result * 10 + chars[i] - '0';
        }
        return flag ? -(int) result : (int) result;
    }

    public static void main(String[] args) {
        System.out.println(strToInt(String.valueOf(Integer.MAX_VALUE)));
        System.out.println(strToInt(String.valueOf((long) Integer.MAX_VALUE + 1)));

        System.out.println(strToInt(String.valueOf(Integer.MIN_VALUE)));
        System.out.println(strToInt(String.valueOf((long) Integer.MIN_VALUE + 1)));
    }
}
