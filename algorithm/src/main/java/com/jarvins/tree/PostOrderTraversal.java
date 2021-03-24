package com.jarvins.tree;

import com.jarvins.util.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 循环方式实现后续遍历
 * 考虑右子的问题,如果没有右子,直接添加该节点
 * 同时: 如果右子已经被使用过,也应该直接添加该节点(所以需要一个标识右子的节点,一旦被使用过,将该节点标识,)
 */
public class PostOrderTraversal {

    private static List<Integer> postOrderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();

        TreeNode tag = null;

        while (root != null || !queue.isEmpty()) {
            //添加左子
            while (root != null) {
                queue.push(root);
                root = root.left;
            }
            //检测右子
            root = queue.pop();
            //没右子
            if (root.right == null ||
                    //标识右子已经被使用过,此时需要添加当前父节点
                    tag == root.right) {
                result.add(root.val);
                tag = root;
                root = null;
            }
            //有右子
            else {
                queue.push(root);
                root = root.right;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Integer[] arr = {1,2,3,4,5,6,7};
        TreeNode tree = TreeNode.createTree(arr);
        List<Integer> list = postOrderTraversal(tree);
        System.out.println(list);
    }
}
