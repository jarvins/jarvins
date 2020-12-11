package com.jarvins;

public class Util {

    /**
     * 二维矩阵输出
     *
     * @param dp
     * @return
     */
    public static String toString(int[][] dp) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                if (j == dp[i].length - 1) {
                    builder.append(String.format("%-5d\n", dp[i][j]));
                } else {
                    builder.append(String.format("%-5d", dp[i][j]));
                }
            }
        }
        return builder.toString();
    }

    /**
     * 一维矩阵输出
     *
     * @param dp
     * @return
     */
    public static String toString(int[] dp) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dp.length; i++) {
            builder.append(String.format("%-5d", dp[i]));
        }
        return builder.toString();
    }
}



