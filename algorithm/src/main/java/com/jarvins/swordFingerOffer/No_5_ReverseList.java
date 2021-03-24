package com.jarvins.swordFingerOffer;

import com.jarvins.util.ListNode;

import java.util.*;

/**
 * 给定链表,反向打印链表的结果
 * <p>
 * 方案:
 * 1,栈输出(不改变结构)
 * 2,递归(不改变结构)
 * 3,链表反转(改变了链表的结构)
 */
public class No_5_ReverseList {

    public static void main(String[] args) {
        ListNode list = ListNode.createList(new int[]{1, 2, 3, 4, 5, 6, 7, 8});

        System.out.println(Arrays.toString(method_1(list)));
        System.out.println(Arrays.toString(method_2(list)));
        System.out.println(Arrays.toString(method_3(list)));
    }

    private static int[] method_1(ListNode node) {
        if (node == null) {
            return new int[]{};
        }
        Deque<Integer> deque = new ArrayDeque<>();
        int count = 0;
        while (node != null) {
            count++;
            deque.push(node.val);
            node = node.next;
        }
        int[] arr = new int[count];
        count = 0;
        while (!deque.isEmpty()) {
            arr[count++] = deque.pop();
        }
        return arr;
    }

    private static int[] method_2(ListNode node) {
        List<Integer> list = new ArrayList<>();
        method_2_recursive(list, node);
        int[] arr = new int[list.size()];
        int count = 0;
        for (Integer integer : list) {
            arr[count++] = integer;
        }
        return arr;
    }

    private static void method_2_recursive(List<Integer> list, ListNode node) {
        if (node != null) {
            if (node.next != null) {
                method_2_recursive(list, node.next);
            }
            list.add(node.val);
        }
    }

    private static int[] method_3(ListNode node) {
        if (node == null) {
            return new int[]{};
        }
        ListNode reverse = null;
        int count = 0;
        while (node != null) {
            count++;
            ListNode next = node.next;
            if (reverse == null) {
                reverse = node;
                reverse.next = null;
            } else {
                node.next = reverse;
                reverse = node;
            }
            node = next;
        }
        int[] arr = new int[count];
        count = 0;
        while (reverse != null) {
            arr[count++] = reverse.val;
            reverse = reverse.next;
        }
        return arr;
    }
}
