package com.jarvins.backtrack;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 返回某个数组的全排列，不包含重复
 *
 * 输入: [1,1,2]
 * 输出:
 * [
 * [1,1,2],
 * [1,2,1],
 * [2,1,1]
 * ]
 *
 *
 * 难点在于如何去重:
 * 重复产生的原因是相同的数字，经过排序后让相同的数字并列在一起，产生如下去重思路:
 * 如果相邻的两个元素相同，且前一个元素被使用过，则当前状态当前元素不应该被使用，抽象结果就是:
 *
 * 对于: i1,i2
 * 如果当前使用过了i1,则允许使用i2,导致不可能出现如下结果组合: .....,i1,....,i2,......
 * 但对于优先使用i2,则允许使用i1,则会出现如下结果组合: .....,i2,.....,i1,....
 * 达到了去重的目的
 *
 */
public class FullArrayAdder {

    public static void main(String[] args) {
        FullArrayAdder arrayAdder = new FullArrayAdder();
        int[] arr = {1,1,2};
        List<List<Integer>> lists = arrayAdder.permuteUnique(arr);
        System.out.println();
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> ans = new ArrayList<>();
        boolean[] visible = new boolean[nums.length];
        backTrack(nums,ans,result,visible);
        return result;
    }

    private void backTrack(int[] nums, List<Integer> ans, List<List<Integer>> result, boolean[] visible){
        if(ans.size() == nums.length){
            result.add(new ArrayList<>(ans));
            return;
        }
        for(int i = 0; i < nums.length; i++){
            if(visible[i] || i > 0 && nums[i] == nums[i-1] && !visible[i-1]){
                continue;
            }
            ans.add(nums[i]);
            visible[i] = true;
            backTrack(nums,ans,result,visible);
            visible[i] = false;
            ans.remove(ans.size()-1);
        }
    }
}
