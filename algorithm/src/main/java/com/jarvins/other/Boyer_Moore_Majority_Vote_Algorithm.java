package com.jarvins.other;

import java.util.ArrayList;
import java.util.List;

/**
 * 摩尔投票法: 用于统计m个人参加投票，选出得票超过m/k的人
 * <p>
 * 分析(前提是存在):
 * 如果10个人中需要选1个人,即: 选出得票超过m/2的人
 * 如果10个人中需要选2个人,即: 选出得票超过m/3的人
 * 如果m个人中需要选n个人,即: 选出得票超过m/(n+1)的人
 * <p>
 * 两个阶段:
 * 1,抵消阶段,比较对象和被比较对象(候选者)不相同,抵消候选者(计数自减),候选者不足抵消,更换候选者
 * 2,校验阶段,校验最后的候选者是否符合要求
 * <p>
 * 证明:
 * 比如在M个数中找出出现次数超过M/3的数字,最终结果只有1个或者2个,
 * <p>
 * 应用:
 * 1,在M个数中找出出现次数超过M/3的数字(实际就是找出现次数 > M/3)
 * 2,在M个数字找出众数(假设只有一个,实际就是找出现次数 > M/2)
 */
public class Boyer_Moore_Majority_Vote_Algorithm {

    public static void main(String[] args) {
        int[] arr = {1,2,1,-1,-1,-1,-1};
        System.out.println(find(arr, 2));
    }


    private static List<Integer> find(int[] arr, int base) {
        int count = arr.length / base;
        int[][] result = new int[base - 1][2];

        for (int i : arr) {
            int j = 0;
            for (; j < result.length; j++) {
                //找候选人
                if (result[j][0] == i) {
                    result[j][0] = i;
                    result[j][1]++;
                    break;
                }
            }
            if (j == result.length) {
                j = 0;
                for (; j < result.length; j++) {
                    if (result[j][1] == 0) {
                        result[j][0] = i;
                        result[j][1]++;
                        break;
                    }
                }
            }
            //候选人阵营抵消非候选人阵营(n个抵消1个)
            if (j == result.length) {
                for (int[] re : result) {
                    re[1]--;
                }
            }
        }
        for (int[] re : result) {
            re[1] = 0;
        }
        //统计最终候选人
        for (int i : arr) {
            for (int[] re : result) {
                if (re[0] == i) {
                    re[1]++;
                }
            }
        }
        //校验候选人
        List<Integer> list = new ArrayList<>();
        for (int[] re : result) {
            if (re[1] > count && !list.contains(re[0])) {
                list.add(re[0]);
            }
        }
        return list;
    }
}
