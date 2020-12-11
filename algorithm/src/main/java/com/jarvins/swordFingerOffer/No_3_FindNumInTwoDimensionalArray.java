package com.jarvins.swordFingerOffer;

/**
 * 给定一个有序的二维数字，保持向右递增，向下递增
 * 判断某个数字是否在数组中
 * <p>
 * 方案:
 * 1,二分查找
 * 2,区间搜索(推荐方案，右上角向左下角缩小范围,行级递增，列级递减)
 */
public class No_3_FindNumInTwoDimensionalArray {
    public static void main(String[] args) {
//        int[][] arr = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};
//        int[][] arr = {{}};
        int[][] arr = {{1, -1}};
        System.out.println(method_1(arr, 0, 0, arr[0].length, -1));
        System.out.println(method_2(arr, 0, arr[0].length - 1, -1));
    }


    private static boolean method_1(int[][] arr, int row, int start, int end, int num) {
        if (arr == null || arr.length == 0 || arr[0].length == 0) return false;
        if (row < arr.length) {
            int middle = (start + end) >> 1;
            if (arr[row][middle] == num) {
                return true;
            }
            if (arr[row][middle] < num) {
                if (middle < end && middle > start) {
                    return method_1(arr, row, middle, end, num);
                }
            } else {
                if (middle < end && middle > start) {
                    return method_1(arr, row, start, middle, num);
                }
            }
            return method_1(arr, row + 1, 0, end, num);
        }
        return false;
    }

    private static boolean method_2(int[][] arr, int row, int column, int num) {
        if (arr == null || arr.length == 0 || arr[0].length == 0) return false;
        while (row < arr.length && column >= 0) {
            if (arr[row][column] == num) {
                return true;
            } else if (arr[row][column] > num) {
                column--;
            } else {
                row++;
            }
        }
        return false;
    }

}
