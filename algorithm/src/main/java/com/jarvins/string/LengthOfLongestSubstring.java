package com.jarvins.string;

import java.util.Arrays;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 */
public class LengthOfLongestSubstring {

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring_1("abcabcbb"));
        System.out.println(lengthOfLongestSubstring_1("pwwkew"));
        System.out.println(lengthOfLongestSubstring_1("dfksjalfjdfilgjlsaadjljcsialjcs"));
    }

    /**
     * 数组下标记录方式
     *
     * @param
     * @return
     */
    public static int lengthOfLongestSubstring_1(String s) {
        if (s == null || s.length() == 0) return 0;
        if (s.length() == 1) return 1;
        char[] chars = s.toCharArray();
        //arr[0]表示'a'
        int[] arr = new int[26];
        Arrays.fill(arr, -1);
        int length = 0;
        int low = 0;
        arr[chars[low]] = low;
        int high = 1;
        while (low < chars.length) {
            while (high < chars.length) {
                int index = chars[high];
                if (arr[index] >= low) {
                    length = Math.max(length, high - low);
                    low = arr[index] + 1;
                    arr[index] = high;
                    high++;
                    break;
                } else {
                    arr[chars[high]] = high;
                    high++;
                }
            }
            if (high == chars.length) {
                return Math.max(length, high - low);
            }
        }
        return length;
    }
}
