package com.jarvins.swordFingerOffer;

/**
 * 地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。一个机器人从坐标 [0, 0] 的格子开始移动，
 * 它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。
 * 例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，
 * 因为3+5+3+8=19。请问该机器人能够到达多少个格子？
 * <p>
 * 示例 1：
 * <p>
 * 输入：m = 2, n = 3, k = 1
 * 输出：3
 * 示例 2：
 * <p>
 * 输入：m = 3, n = 1, k = 0
 * 输出：1
 */
public class No_12_MovingCount {

    public static void main(String[] args) {
        System.out.println(movingCount(14, 23, 16));
    }

    public static int movingCount(int m, int n, int k) {
        if (k == 0) return 1;
        int[][] dp = new int[m][n];
        return count(dp, 0, 0, k);
    }

    private static int count(int[][] dp, int i, int j, int k) {
        if (i >= 0 && i < dp.length
                && j >= 0 && j < dp[0].length
                && dp[i][j] == 0
                && canArrive(i, j, k)) {
            dp[i][j] = 1;
            return 1 + count(dp, i - 1, j, k) + count(dp, i + 1, j, k) + count(dp, i, j - 1, k) + count(dp, i, j + 1, k);
        }
        return 0;
    }

    private static boolean canArrive(int i, int j, int k) {
        int sum = 0;
        while (i > 0) {
            sum += i % 10;
            i = i / 10;
        }
        while (j > 0) {
            sum += j % 10;
            j = j / 10;
        }
        return sum <= k;
    }
}
