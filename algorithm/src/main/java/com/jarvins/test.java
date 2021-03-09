package com.jarvins;

public class test {
    private static final String[] NUMBER = new String[]{"零","一","二","三","四","五","六","七","八","九"};

    public static void main(String[] args) {
        System.out.println(removeDuplicates("fdddfdfdfd"));
    }

    public static String removeDuplicates(String S) {
        char[] chars = S.toCharArray();
        for(int i = 0; i < chars.length; i++){
            int j = i + 1;
            if(j < chars.length && chars[j] == chars[i]){
                chars[j] = '-';
                chars[i] = '-';
                while(i > 0 && j < chars.length - 1 && chars[i-1] == chars[j+1]){
                    chars[--i] = '-';
                    chars[++j] = '-';
                }
                i = j+1;
            }
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < chars.length; i++){
            if(chars[i] != '-'){
                result.append(chars[i]);
            }
        }
        return result.toString();
    }
}
