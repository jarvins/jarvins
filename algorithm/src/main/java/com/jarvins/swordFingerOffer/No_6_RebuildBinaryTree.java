package com.jarvins.swordFingerOffer;

/**
 * 根据二叉树的先序遍历和中序遍历结果构建一个符合条件的二叉树
 * 假设没有重复的数字
 * <p>
 * <p>
 * 方案:
 * 通过二叉树先序找到根，通过中序判断左子和右子，
 * 如果有左子，则preOrder[root+1] = leftChild
 * 如果有右子，则preOrder[root + 左子长度(中序可以判断出来) + 1] = rightChild
 */
public class No_6_RebuildBinaryTree {
    public static void main(String[] args) {
        int[] pre = {3, 2, 1, 4};
        int[] in = {1, 2, 3, 5};
        TreeNode treeNode = buildTree_1(pre, in);
        System.out.println();
    }

    public static TreeNode buildTree_1(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length < 1 || inorder == null || inorder.length < 1) return null;
        TreeNode root = new TreeNode(preorder[0]);
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == preorder[0]) {
                buildChild_1(root, 0, preorder, 0, i - 1, inorder, i + 1, inorder.length - 1);
            }
        }
        return root;
    }

    private static void buildChild_1(TreeNode node, int parentIndex, int[] preorder, int leftStart, int leftEnd, int[] inorder, int rightStart, int rightEnd) {
        //没有子节点
        if (leftStart > leftEnd && rightStart > rightEnd) {
            return;
        }
        //左节点
        if (leftStart <= leftEnd) {
            int leftValue = preorder[parentIndex + 1];
            node.left = new TreeNode(leftValue);
            for (int i = leftStart; i <= leftEnd; i++) {
                if (inorder[i] == leftValue) {
                    buildChild_1(node.left, parentIndex + 1, preorder, leftStart, i - 1, inorder, i + 1, leftEnd);
                } else if (i == leftEnd) {
                    throw new IllegalArgumentException("can not build a tree");
                }
            }
        }
        //右节点
        if (rightStart <= rightEnd) {
            int rightValue = preorder[parentIndex + leftEnd - leftStart + 2];
            node.right = new TreeNode(rightValue);
            for (int j = rightStart; j <= rightEnd; j++) {
                if (inorder[j] == rightValue) {
                    buildChild_1(node.right, parentIndex + leftEnd - leftStart + 2, preorder, rightStart, j - 1, inorder, j + 1, rightEnd);
                } else if (j == rightEnd) {
                    throw new IllegalArgumentException("can not build a tree");
                }
            }
        }
    }


    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
