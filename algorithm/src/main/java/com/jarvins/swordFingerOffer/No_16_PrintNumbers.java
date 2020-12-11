package com.jarvins.swordFingerOffer;

/**
 * 输入n,给出小于等于n位的所有10进制数，比如:
 * <p>
 * 输入:n = 1;
 * 输出: 1,2,3,4,5,6,7,8,9
 * <p>
 * 输入:n = 14;
 * 输出: 1,2,3,.......,99999999999999
 */
public class No_16_PrintNumbers {

    public static void main(String[] args) {
        print(5);
    }

    private static void print(int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            printNum(i, 0, builder);
        }
    }

    private static void printNum(int length, int currentLength, StringBuilder builder) {
        if (currentLength == length) {
            System.out.println(builder);
            builder.setLength(builder.length() - 1);
        } else {
            for (int i = 0; i < 10; i++) {
                if (!(builder.length() == 0) || !(i == 0)) {
                    builder.append(i);
                    printNum(length, currentLength + 1, builder);
                }
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
        }
    }
}
