package com.jarvins.swordFingerOffer;

/**
 * 给定一个增序数组旋转k次的旋转数组，比如:
 * {0,1,2,3,4,5} ──3──> {3,4,5,0,1,2}
 * 找出数组的最小值
 * 要求复杂度小于O(logN)
 * <p>
 * 方案:
 * 二分查找,3种情况(0,1,2,3,4,5,6,7,8,9):
 *
 *          /9                    /9                      /9
 *         /8 high               /8 mid                  /8
 *        /7                    /7                      /7 low
 *       /6                    /6                                    /6 high
 *      /5                    /5                                    /5
 *     /4                    /4                                    /4
 *    /3  mid               /3 low                                /3
 *   /2                               /2 high                    /2
 *  /1                               /1                         /1  mid
 * /0 low                           /0                         /0
 *
 * 出现相等的情况让high自减
 */
public class No_10_MinInRotateArray {

    public static void main(String[] args) {
        int[] arr = {10, 1, 10, 10, 10};
        System.out.println(min(arr));
    }

    private static int min(int[] arr) {
        if (arr == null || arr.length == 0) return -1;
        if (arr.length < 3) return Math.min(arr[0], arr[arr.length - 1]);
        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid] > arr[high]) {
                low = mid + 1;
            } else if (arr[mid] < arr[high]) {
                high = mid;
            } else {
                high--;
            }
        }
        return arr[high];
    }
}
