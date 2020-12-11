package com.jarvins.string;

import com.jarvins.tree.TrieTree_Map;

/**
 * 哦，不！你不小心把一个长篇文章中的空格、标点都删掉了，并且大写也弄成了小写。
 * 像句子"I reset the computer. It still didn’t boot!"已经变成了"iresetthecomputeritstilldidntboot"。
 * 在处理标点符号和大小写之前，你得先把它断成词语。当然了，你有一本厚厚的词典dictionary，
 * 不过，有些词没在词典里。假设文章用sentence表示，设计一个算法，把文章断开，要求未识别的字符最少，返回未识别的字符数。
 * <p>
 * 输入：
 * dictionary = ["looked","just","like","her","brother"]
 * sentence = "jesslookedjustliketimherbrother"
 * 输出： 7
 * 解释： 断句后为"jess looked just like tim her brother"，共7个未识别字符。
 * <p>
 * <p>
 * 方案:
 * 1,暴力dp,初始dp[i] = dp[i-1] + 1,遍历字典，检查最后长度为n的字符串是否等于当前检索单词,如果等,则dp[i] = min(dp[i],dp[i-length]);
 * 2,字典树
 */
public class ReSpace {

    public static void main(String[] args) {
        String[] arr = {"looked", "just", "like", "her", "brother"};
        String str = "jesslookedjustliketimherbrother";
        System.out.println(dp(arr,str));
        System.out.println(dictionaryTree(arr,str));
    }

    public static int dp(String[] dictionary, String sentence) {
        int[] dp = new int[sentence.length() + 1];
        char[] chars = sentence.toCharArray();
        for (int i = 1; i < dp.length; i++) {
            dp[i] = dp[i - 1] + 1;
            for (String str : dictionary) {
                int length = str.length();
                if (i >= length && String.valueOf(chars, i - length, length).equals(str)) {
                    dp[i] = Math.min(dp[i - length], dp[i]);
                }
            }
        }
        return dp[sentence.length()];
    }

    public static int dictionaryTree(String[] dictionary, String sentence){
        TrieTree_Map trieTree = new TrieTree_Map();
        for (String s : dictionary) {
            trieTree.put(s,false);
        }
        char[] chars = sentence.toCharArray();
        int[] dp = new int[chars.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = i > 0 ? dp[i-1] + 1 : 1;
            TrieTree_Map tree = trieTree;
            int j = i;
            while (tree.getNext().containsKey(chars[j])){
                tree = tree.getNext().get(chars[j--]);
                if(tree.isEnd()){
                    dp[i] = Math.min(dp[j],dp[i]);
                }
            }
        }
        return dp[sentence.length()-1];
    }
}
