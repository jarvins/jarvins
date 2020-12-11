package com.jarvins.backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
 * <p>
 * 说明：解集不能包含重复的子集。
 * <p>
 * 示例:
 * <p>
 * 输入: nums = [1,2,3]
 * 输出:
 * [
 * [3],
 *   [1],
 *   [2],
 *   [1,2,3],
 *   [1,3],
 *   [2,3],
 *   [1,2],
 *   []
 * ]
 */
public class PowerSet {


    public static void main(String[] args) {
        PowerSet powerSet = new PowerSet();
        int[] nums = {1, 2, 3};
        List<List<Integer>> subsets = powerSet.subsets(nums);
        System.out.println();
    }

    public List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> ans = new ArrayList<>();
        backTrack(0, nums, ans, result);
        return result;
    }

    private void backTrack(int startIndex, int[] nums, List<Integer> ans, List<List<Integer>> result) {
        if (startIndex == nums.length) {
            result.add(new ArrayList<>(ans));
            return;
        }
        ans.add(nums[startIndex]);
        backTrack(startIndex + 1, nums, ans, result);
        ans.remove(ans.size() - 1);
        backTrack(startIndex + 1, nums, ans, result);
    }
}
