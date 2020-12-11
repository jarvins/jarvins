package com.jarvins.backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定n,返回1，2，3，4，5....n的全排列
 */
public class FullArray {

    public static void main(String[] args) {
        int n = 3;
        List<List<Integer>> lists = fullArray(n);
        System.out.println();
    }

    public static List<List<Integer>> fullArray(int n){
        int[] arr = new int[n];
        for(int i = 0; i<n; i++){
            arr[i] = i+1;
        }
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        backTrack(arr,list,result);
        return result;
    }

    private static void backTrack(int[] arr, List<Integer> list, List<List<Integer>> result){
        if(list.size() == arr.length){
            result.add(new ArrayList<>(list));
            return;
        }
        for(int i = 0; i < arr.length; i++){
            if(list.contains(arr[i])){
                continue;
            }
            list.add(arr[i]);
            backTrack(arr,list,result);
            list.remove(list.size()-1);
        }
    }
}
