package com.jarvins.sort;

import java.util.Arrays;

/**
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {2};
        quickSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int low, int height) {
        int l = low;
        int h = height;
        while (l < h) {
            while (l < h && array[l] <= array[h]) {
                l++;
            }
            swap(array, l, h);
            while (l < h && array[h] > array[l]) {
                h--;
            }
            swap(array, l, h);
        }

        if (low < l - 1)
            quickSort(array, low, l - 1);
        if (h + 1 < height)
            quickSort(array, h + 1, height);

    }

    private static void swap(int[] array, int low, int height) {
        int tmp = array[low];
        array[low] = array[height];
        array[height] = tmp;
    }
}
