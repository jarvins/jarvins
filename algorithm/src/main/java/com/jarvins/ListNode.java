package com.jarvins;


public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public static ListNode createList(int[] arr) {
        if (arr == null || arr.length <= 0) throw new IllegalArgumentException("arr is empty!");
        ListNode result = null;
        ListNode pre = null;
        for (int i : arr) {
            ListNode node = new ListNode(i);
            if (result == null) {
                result = node;
                pre = node;
            }
            pre.next = node;
            pre = node;
        }
        return result;
    }
}
