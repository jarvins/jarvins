package com.jarvins.recursion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给定一个整数 n，生成所有由 1 ... n 为节点所组成的 二叉搜索树 。
 *
 * 示例：
 *
 * 输入：3
 * 输出：
 * [
 *   [1,null,3,2],
 *   [3,2,null,1],
 *   [3,1,null,null,2],
 *   [2,1,3],
 *   [1,null,2,null,3]
 * ]
 * 解释：
 * 以上的输出对应以下 5 种不同结构的二叉搜索树：
 *
 *    1         3     3      2      1
 *     \       /     /      / \      \
 *      3     2     1      1   3      2
 *     /     /       \                 \
 *    2     1         2                 3
 */
public class GenerateTrees {

    public static List<TreeNode> generateTrees(int n) {
        if (n == 0) return new ArrayList<>();
        return creatChild(1, n);
    }

    private static List<TreeNode> creatChild(int min, int max) {
        if (min > max) {
            return null;
        } else if (min == max) {
            return Collections.singletonList(new TreeNode(min));
        } else {
            List<TreeNode> result = new ArrayList<>();
            for (int i = min; i <= max; i++) {
                List<TreeNode> leftChild = creatChild(min, i - 1);
                List<TreeNode> rightChild = creatChild(i + 1, max);

                if (leftChild != null && rightChild != null) {
                    for (TreeNode left : leftChild) {
                        for (TreeNode right : rightChild) {
                            TreeNode root = new TreeNode(i);
                            root.left = left;
                            root.right = right;
                            result.add(root);
                        }
                    }
                } else if (leftChild != null) {
                    for (TreeNode left : leftChild) {
                        TreeNode root = new TreeNode(i);
                        root.left = left;
                        result.add(root);
                    }
                } else if (rightChild != null) {
                    for (TreeNode right : rightChild) {
                        TreeNode root = new TreeNode(i);
                        root.right = right;
                        result.add(root);
                    }
                }
            }
            return result;
        }
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
