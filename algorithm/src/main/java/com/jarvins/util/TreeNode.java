package com.jarvins.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 完全二叉树格式,空则使用null
 */
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }

    public static TreeNode createTree(Integer[] arr) {
        if (arr == null || arr.length < 1 || arr[0] == null) throw new IllegalArgumentException("arr is empty!");
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode root = new TreeNode(arr[0]);
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            int index = i;
            for (; i < ((1 << size) + index); ) {
                TreeNode poll = queue.poll();
                if (i < arr.length && arr[i] != null) {
                    TreeNode left = new TreeNode(arr[i++]);
                    queue.offer(left);
                    poll.left = left;
                } else {
                    i++;
                }
                if (i < arr.length && arr[i] != null) {
                    TreeNode right = new TreeNode(arr[i++]);
                    queue.offer(right);
                    poll.right = right;
                } else {
                    i++;
                }
                if (i >= arr.length) {
                    return root;
                }
            }
        }
        return null;
    }
}
