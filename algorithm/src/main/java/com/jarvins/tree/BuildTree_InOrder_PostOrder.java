package com.jarvins.tree;

import com.jarvins.TreeNode;

/**
 * 根据一棵树的中序遍历与后序遍历构造二叉树。
 *
 * 注意:
 * 你可以假设树中没有重复的元素。
 *
 * 例如，给出
 *
 * 中序遍历 inorder = [9,3,15,20,7]
 * 后序遍历 postorder = [9,15,7,20,3]
 * 返回如下的二叉树：
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 */
public class BuildTree_InOrder_PostOrder {

    public static void main(String[] args) {
        int[] inorder = {9,3,15,20,7};
        int[] postOrder = {9,15,7,20,3};
        BuildTree_InOrder_PostOrder a = new BuildTree_InOrder_PostOrder();
        TreeNode node = a.buildTree(inorder, postOrder);
        System.out.println();

    }

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        int val = postorder[postorder.length - 1];
        for (int i = 0; i < inorder.length; i++) {
            if(inorder[i] == val){
                return build(inorder,postorder,0,i -1, i + 1,inorder.length -1);
            }
        }
        return null;
    }

    private TreeNode build(int[] inorder, int[] postorder, int leftStart, int leftEnd, int rightStart, int rightEnd) {
        int rootIndex = leftEnd + 1;
        int val = inorder[rootIndex];
        TreeNode node = new TreeNode(val);
        if(leftEnd >= leftStart) {
            //todo
        }
        if(rightEnd > rightStart){
            //todo
        }
        return node;
    }
}
