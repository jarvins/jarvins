package com.jarvins.swordFingerOffer;

/**
 * 给定一个二叉树和一个节点，如何找出中序遍历的下一个节点?
 * 节点有个指向父节点的指针
 * <p>
 * 方案:
 * 如果该节点有右子，则下一个节点是右子树最左的那个节点
 * 如果该节点没有右子，且它是父节点的左子，则下一个节点是父节点
 * 如果该节点没有右子，且它是父节点的右子，则下一个节点是
 */
public class No_7_BinaryNextNode {

    private static int getNext(TreeNode node) {
        if (node.right != null) {
            TreeNode t = node.right;
            while (t.left != null) {
                t = t.left;
            }
            return t.value;
        } else if (node.parent != null) {
            if (node.parent.left == node) {
                return node.parent.value;
            } else {
                TreeNode t = node.parent;
                while (t.parent != null) {
                    if (t.parent.left == t) {
                        return t.parent.value;
                    }
                    t = t.parent;
                }
            }
        }
        return -1;
    }

    private static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;
        TreeNode parent;

        public TreeNode(int value) {
            this.value = value;
        }
    }
}

