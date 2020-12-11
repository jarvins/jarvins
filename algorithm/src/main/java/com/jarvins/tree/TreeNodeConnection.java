package com.jarvins.tree;

/**
 * 给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：
 * <p>
 * struct Node {
 * int val;
 * Node *left;
 * Node *right;
 * Node *next;
 * }
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
 * <p>
 * 初始状态下，所有next 指针都被设置为 NULL。
 * <p>
 * eg:
 * 1
 * 2          3
 * 4     5    6       7
 * <p>
 * <p>
 * result:
 * 1 --> null
 * 2    -->     3 --> null
 * 4 --> 5  ---> 6  --> 7 --> null
 * <p>
 * 思路:
 * 1，层次遍历,让后构建关系
 * 2，依靠上一层构建的next关系，能获得当前层的所有节点,实现空间复杂度为1的解决方案
 */
public class TreeNodeConnection {

    public Node connect(Node root) {
        if (root == null || root.left == null) {
            return root;
        }
        Node lastNode = root;
        Node node = lastNode.left;
        while (lastNode.left != null) {
            lastNode.left.next = lastNode.right;
            if (lastNode.next == null || lastNode.right == null) {
                break;
            }
            lastNode.right.next = lastNode.next.left;
            lastNode = lastNode.next;
        }
        connect(node);
        return root;
    }


    private static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        private Node() {
        }

        private Node(int _val) {
            val = _val;
        }

        private Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }
}
