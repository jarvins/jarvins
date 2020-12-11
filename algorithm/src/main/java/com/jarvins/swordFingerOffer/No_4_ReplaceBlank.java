package com.jarvins.swordFingerOffer;

/**
 * 对于网络编程，需要将' '字符替换成"%20",否则转义失败
 * <p>
 * 方案:
 * 倒序遍历，字符下标一次定位，复杂度O(n)
 */
public class No_4_ReplaceBlank {

    public static void main(String[] args) {
        System.out.println(replaceBlank(" e3w 4 32 "));
        System.out.println(replaceBlank(null));
    }

    private static String replaceBlank(String str) {
        if (str == null) return "";
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                count++;
            }
        }
        char[] arr = new char[str.length() + count * 2];
        int index = arr.length - 1;
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == ' ') {
                arr[index--] = '0';
                arr[index--] = '2';
                arr[index--] = '%';
            } else {
                arr[index--] = str.charAt(i);
            }
        }
        return new String(arr);
    }
}
