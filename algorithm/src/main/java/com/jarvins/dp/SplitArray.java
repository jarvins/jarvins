package com.jarvins.dp;

import java.util.Arrays;

/**
 * 给定一个非负整数数组和一个整数 m，你需要将这个数组分成 m 个非空的连续子数组。设计一个算法使得这 m 个子数组各自和的最大值最小。
 * <p>
 * 注意:
 * 数组长度 n 满足以下条件:
 * <p>
 * 1 ≤ n ≤ 1000
 * 1 ≤ m ≤ min(50, n)
 * 示例:
 * <p>
 * 输入:
 * nums = [7,2,5,10,8]
 * m = 2
 * <p>
 * 输出:
 * 18
 * <p>
 * 解释:
 * 一共有四种方法将nums分割为2个子数组。
 * 其中最好的方式是将其分为[7,2,5] 和 [10,8]，
 * 因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。
 */
public class SplitArray {

    /**
     * dp查找
     */
    public static int splitArray_dp(int[] nums, int m) {
        if (nums == null || nums.length == 0) return 0;
        int length = nums.length;
        if (length == 1) return nums[0];
        if (nums[1] == Integer.MAX_VALUE) return Integer.MAX_VALUE;
        int[][] dp = new int[length][m + 1];
        for (int i = 0; i < length; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][1] = nums[0];
        for (int i = 1; i < length; i++) {
            dp[i][1] = dp[i - 1][1] + nums[i];
        }
        //dp[i][k]: nums[0,i-1]分为k段的最大值
        //所以结果是dp[length-1][m];

        for (int i = 1; i < length; i++) {
            for (int j = 2; j <= Math.min(m, i + 1); j++) {
                for (int k = 0; k <= i; k++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[k][j - 1], dp[i][1] - dp[k][1]));
                }
            }
        }
        return dp[length - 1][m];
    }

    /**
     * 二分查找 + 贪心
     */
    public static int splitArray_binarySearch(int[] nums, int m) {
        return -1;
    }
}
