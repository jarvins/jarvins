package com.jarvins.dp;

/**
 * 给定三个字符串 s1, s2, s3, 验证 s3 是否是由 s1 和 s2 交错组成的。
 * <p>
 * 示例 1:
 * <p>
 * 输入: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
 * 输出: true
 * 示例 2:
 * <p>
 * 输入: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
 * 输出: false
 */
public class IsInterleave {

    public static void main(String[] args) {
        String A = "db";
        String B = "b";
        String C = "cbb";
        System.out.println(isInterleave_1(A, B, C));
        System.out.println(isInterleave_2(A, B, C));
    }


    private static boolean isInterleave_1(String A, String B, String C) {
        int lengthA = A.length();
        int lengthB = B.length();
        int lengthC = C.length();

        if (lengthC != lengthA + lengthB) {
            return false;
        }

        //dp[i][j]表示[0,i+j]可以由A的前i个字母 + B的前j个字母组成
        boolean[][] dp = new boolean[lengthA + 1][lengthB + 1];
        dp[0][0] = true;
        return dp_1(dp, A, B, C);
    }

    private static boolean dp_1(boolean[][] dp, String A, String B, String C) {
        for (int i = 0; i <= A.length(); i++) {
            for (int j = 0; j <= B.length(); j++) {
                if (i > 0) {
                    dp[i][j] = dp[i - 1][j] && (A.charAt(i - 1) == C.charAt(i + j - 1));
                }
                if (j > 0) {
                    dp[i][j] = dp[i][j] || (dp[i][j - 1] && (B.charAt(j - 1) == C.charAt(i + j - 1)));
                }
            }
        }
        return dp[A.length()][B.length()];
    }

    private static boolean isInterleave_2(String A, String B, String C) {
        int lengthA = A.length();
        int lengthB = B.length();
        int lengthC = C.length();

        if (lengthC != lengthA + lengthB) {
            return false;
        }

        //dp[i][j]表示[0,i+j]可以由A的前i个字母 + B的前j个字母组成
        boolean[] dp = new boolean[lengthB + 1];
        dp[0] = true;
        return dp_2(dp, A, B, C);
    }

    //由于dp[i][j]只受dp[i-1][j](上一行),或者dp[i][j-1](当前行)的影响，故可以考虑压缩算法
    private static boolean dp_2(boolean[] dp, String A, String B, String C) {
        for (int i = 0; i <= A.length(); i++) {
            for (int j = 0; j <= B.length(); j++) {
                if (i > 0) {
                    dp[j] = dp[j] && C.charAt(i + j - 1) == A.charAt(i - 1);
                }
                if (j > 0) {
                    dp[j] = dp[j] || dp[j-1] && C.charAt(i + j - 1) == B.charAt(j - 1);
                }
            }
        }
        return dp[B.length()];
    }
}
