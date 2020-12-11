package com.jarvins.swordFingerOffer;

/**
 * 实现一个Math.pow()函数
 * 输入底数为double类型,指数为int类型
 * <p>
 * 方案:
 * 考虑指数符号,考虑0,使用2分计算
 */
public class No_15_Power {

    public static void main(String[] args) {
        System.out.println(power(2.123, 13));
        System.out.println(Math.pow(2.123,13));
        System.out.println(power(2.123, -13));
        System.out.println(Math.pow(2.123, -13));
    }

    private static double power(double base, int exp) {
        if (exp == 0) return 1;
        if (exp == 1) return base;
        boolean flip = false;
        if (exp < 0) {
            flip = true;
            exp = -exp;
        }
        double pow = pow(base, exp);
        return flip ? 1.0 / pow : pow;
    }

    private static double pow(double base, int exp) {
        if (exp == 1) return base;
        else if ((exp & 1) == 1) {
            return pow(base, exp >> 1) * pow(base, exp >> 1) * base;
        } else {
            return pow(base, exp >> 1) * pow(base, exp >> 1);
        }
    }
}
