package com.jarvins.dp;


/**
 * 背包问题: 一个固定的容量，每件物品附带不同的空间和价值，保证满足容量的前提下，获取最大价值。
 * 0-1背包: 每件物品只有一件,只限定背包重量.
 * 有界背包,多重背包(BKP): 物品数量不限制，即每件物品都有n个副本，只限定背包重量.
 * 无界背包,完全背包(UKP): 物品除重量外，每件限制件数N[i]
 * <p>
 * 关键:
 * dp[i][j] 表示的是将前i件物品装进容量为j的空间的最大价值
 * weight[]和value[]只需要对应，并不需要按顺序排列
 * <p>
 * example:
 * private final static int[] WEIGHT = new int[]{5,4,3,1};
 * private final static int[] VALUE = new int[]{7,5,4,1};
 * private final static int N = 7;
 */
public class KnapsackProblem {

    /**
     * 0-1背包常规求解,二维矩阵
     *
     * @param weight   重量数组
     * @param value    价值数组
     * @param capacity 背包容量
     * @return dp二维矩阵
     */
    public static int[][] solve_01_twoDimensionalMatrix(int[] weight, int[] value, int capacity) {
        //创建动态规划结果数组,默认0
        int[][] dp = new int[weight.length + 1][capacity + 1];

        //i,j从1开始,因为无论是容量为0还是数量为0,其最大价值均为0(已被初始化)
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {

                /*
                 weight[i-1] : 当前物品的重量
                 value[i-1] : 当前物品的价值
                 */

                //第i件物品的重量 <= 背包的容量,即可以将这件物品放进背包中
                if (weight[i - 1] <= j) {
                    //不放入背包,价值等价同重量前i-1件的最大价值
                    int notPut = dp[i - 1][j];
                    //放入背包,当前的价值 + dp[前n-1件物品][重量为当前重量 - 放入物品的重量]
                    int put = value[i - 1] + dp[i - 1][j - weight[i - 1]];
                    dp[i][j] = Math.max(notPut, put);
                }
                //这件物品无法放入背包
                else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp;
    }

    /**
     * 0-1背包省空间解法
     *
     * @param weight   重量数组
     * @param value    价值数组
     * @param capacity 背包容量
     * @return dp一维矩阵
     */
    public static int[] solve_01_oneDimensionalMatrix(int[] weight, int[] value, int capacity) {

        /*
         考虑到第i个物品放与不放的最大价值只与第i-1个物品的结果相关,即
         即，dp[i]关联与dp[i-1]
         因此，可以倒序遍历(正序导致dp[i-1]结果被覆盖)
         */

        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weight.length; i++) {
            for (int j = capacity; j >= 1; j--) {
                //第i件物品的重量 <= 背包的容量,即可以将这件物品放进背包中
                if (weight[i] <= j) {
                    //不装入第j件物品
                    int notPut = dp[j];
                    //装入第i件物品
                    int put = dp[j - weight[i]] + value[i];
                    dp[j] = Math.max(notPut, put);
                } else {
                    //这件物品放不进背包,当前最大值 = 上一次(不包含第i件物品，容量为j的最大值)
                    dp[j] = dp[j];
                }
            }
        }
        return dp;
    }

    /**
     * 多重背包常规求解，二维矩阵
     * 沿用01背包的思路,略微调整状态转义方程
     *
     * @param weight   重量数组
     * @param value    价值数组
     * @param capacity 背包容量
     * @return dp二维矩阵
     */
    public static int[][] solve_BKP_twoDimensionalMatrix_Adjust(int[] weight, int[] value, int capacity) {
        //创建动态规划结果数组
        int[][] dp = new int[weight.length + 1][capacity + 1];

        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                //放得下
                if (weight[i - 1] <= j) {
                    //不放入背包,价值等价同重量前i-1件的最大价值
                    int notPut = dp[i - 1][j];
                    //放入第i件物品，此时不再是dp[i-1],而是dp[i]，因为第i件物品可以重复放入
                    int put = value[i - 1] + dp[i][j - weight[i - 1]];
                    dp[i][j] = Math.max(notPut, put);
                }
                //放不下
                else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp;
    }

    /**
     * 多重背包,从可以放N件weight[i]考虑
     * 对于容量M,第i件物品重量为weight[i-1],最多可以放 M/weight[i-1]件
     *
     * 多重背包方法和这个方法类似，唯一区别是，k in [0, min(M/weight[i-1],N[i])]
     *
     * @param weight   重量数组
     * @param value    价值数组
     * @param capacity 背包容量
     * @return dp二维矩阵
     */
    public static int[][] solve_BKP_twoDimensionalMatrix_N(int[] weight, int[] value, int capacity) {
        //创建动态规划结果数组
        int[][] dp = new int[weight.length + 1][capacity + 1];

        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {

                /*
                 第i件物品的重量 <= 背包的容量,即可以将这件物品放进背包中
                 对于容量M,第i件物品重量为weight[i-1],最多可以放 M/weight[i-1]件
                 */

                for (int k = 0; k <= j / weight[i - 1]; k++) {
                    //不放入背包,价值等价同重量前i-1件的最大价值
                    int notPut = dp[i - 1][j];
                    //放入k件,当k=0表示不放入
                    int put = k * value[i - 1] + dp[i - 1][j - k * weight[i - 1]];
                    dp[i][j] = Math.max(notPut, put);
                }
            }
        }
        return dp;
    }

    /**
     * 多重背包一位数组优化
     * 由于dp[i]关联dp[i]而非dp[i-1](dp[i-1][j]也可认为是覆盖)，因此这里需要正向覆盖，而非逆序
     */
    public static int[] solve_BKP_oneDimensionalMatrix(int[] weight, int[] value, int capacity) {
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weight.length; i++) {
            for (int j = 1; j <= capacity; j++) {
                if(weight[i] <= j){
                    //不放入
                    int notPut = dp[j];
                    //放入
                    int put = value[i] + dp[j - weight[i]];
                    dp[j] = Math.max(notPut, put);
                }
                else {
                    dp[j] = dp[j];
                }
            }
        }
        return dp;
    }

}
