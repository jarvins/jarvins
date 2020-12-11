package com.jarvins.swordFingerOffer;

/**
 * 计算斐波那契的第n项
 *
 * 方案:
 * 1,递归(着重分析效率问题)
 * 2,循环
 * 3,矩阵(不实现),复杂度为O(logN)
 */
public class No_9_Fibonacci {


    /**
     * 1,递归会使用栈分配内存空间地址,而每个线程可用的栈空间是有限的,过度递归会引起栈溢出
     * 2,这个方案存在大量的重复计算过程,比如计算f(5):
     *   f(5) = f(4) + f(3)
     *        = f(2) + f(3) + f(1) + f(2)
     *        = 1 + 0 + 1 + f(2) + 0 + 0 + 1
     *        = 3 + 0 + 1
     *        = 4
     */
    private static long method_1(int n){
        if(n <= 0) return 0;
        if(n == 1) return 1;
        return method_1(n-1) + method_1(n-2);
    }

    /**
     * 2个数字交替充当f(n-1),f(n-2)
     * @param n
     * @return
     */
    private static long method_2(int n){
        if(n <= 0) return 0;
        if(n == 1) return 1;

        long l = 0;
        long r = 1;
        for (int i = 2; i <= n; i++) {
            long result = l + r;
            l = r;
            r = result;
        }
        return r;
    }
}
