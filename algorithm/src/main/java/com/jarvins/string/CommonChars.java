package com.jarvins.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。
 * 例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
 * <p>
 * 你可以按任意顺序返回答案。
 * <p>
 * 示例 1：
 * 输入：["bella","label","roller"]
 * 输出：["e","l","l"]
 * <p>
 * 示例 2：
 * 输入：["cool","lock","cook"]
 * 输出：["c","o"]
 */
public class CommonChars {

    public static List<String> commonChars(String[] A) {
        int[] arr = new int[26];
        int[] min = new int[26];
        Arrays.fill(min,Integer.MAX_VALUE);

        for (String str : A) {
            char[] chars = str.toCharArray();
            for (char aChar : chars) {
                arr[aChar - 'a']++;
            }
            for (int i = 0; i < 26; i++) {
                min[i] = Math.min(min[i], arr[i]);
            }
            Arrays.fill(arr, 0);
        }
        List<String> result = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            if (min[i] > 0) {
                while (min[i] > 0) {
                    result.add(String.valueOf((char) ('a' + i)));
                    min[i]--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String[] a = {"bella", "label", "roller"};
        System.out.println(commonChars(a));
    }
}
