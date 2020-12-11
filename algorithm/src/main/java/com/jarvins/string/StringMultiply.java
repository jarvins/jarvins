package com.jarvins.string;

import java.util.Arrays;

/**
 * 给定两个以字符串形式表示的非负整数num1和num2，返回num1和num2的乘积，它们的乘积也表示为字符串形式。
 * <p>
 * 示例 1:
 * <p>
 * 输入: num1 = "2", num2 = "3"
 * 输出: "6"
 * 示例2:
 * <p>
 * 输入: num1 = "123", num2 = "456"
 * 输出: "56088"
 */
public class StringMultiply {

    public static void main(String[] args) {
        String s1 = "204";
        String s2 = "5";
        String multiply = multiply(s1, s2);
        System.out.println(multiply);
    }

    public static String multiply(String num1, String num2) {
        if(num1 == null || num1.length() == 0) return num2;
        if(num2 == null || num2.length() == 0) return num1;
        int m = num1.length();
        int n = num2.length();
        char[] n1 = num1.toCharArray();
        char[] n2 = num2.toCharArray();
        char[] result = new char[m + n];
        Arrays.fill(result,'0');

        for(int i =  m - 1; i >= 0; i--){
            int index = n + i;
            for(int j  = n - 1; j >= 0; j--){
                int t = (n1[i] - '0') * (n2[j] - '0');
                result[index] = (char)(result[index] + t % 10);
                result[index-1] = (char)(result[index - 1] + t / 10);
                index--;
            }
        }
        int add = 0;
        for(int i =  m + n - 1; i >= 0; i --){
            int t = result[i] + add - '0';
            add = t / 10;
            result[i] = (char)(t % 10 + '0');
        }
        for(int i = 0;  i < m + n; i++){
            if(result[i] != '0'){
                return new String(result,i,m + n - i);
            }
        }
        return "0";
    }
}
