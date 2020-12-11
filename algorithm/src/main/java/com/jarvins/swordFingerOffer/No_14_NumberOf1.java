package com.jarvins.swordFingerOffer;

/**
 * 统计一个int类型的数的二进制有多少个1
 * <p>
 * 思路:
 * 1,右移(死循环bug)
 * 2,左移(移动32次)
 * 3,与运算(完美)
 */
public class No_14_NumberOf1 {

    public static void main(String[] args) {
        System.out.println(count1_1(32423));
        System.out.println(count1_2(32423));
        System.out.println(count1_3(32423));

        System.out.println(count1_2(-32423));
        System.out.println(count1_3(-32423));
    }

    //对于负数，会出现死循环bug(0xffffffff)
    private static int count1_1(int n) {
        int count = 0;
        while (n != 0) {
            if ((n & 1) == 1) {
                count++;
                n = n >> 1;
            }
        }
        return count;
    }

    private static int count1_2(int n) {
        int count = 0;
        int c = 1;
        while (c != 0) {
            if ((c & n) != 0) {
                count++;
            }
            c = c << 1;
        }
        return count;
    }

    private static int count1_3(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n = n & (n - 1);
        }
        return count;
    }
}
