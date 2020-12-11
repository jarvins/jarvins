package com.jarvins.tree;

/**
 * 普通二叉树接口，所有基于二叉树的实现均需要继承此接口,
 * 接口方法描述了二叉树的所有性质
 */
public interface BinaryTree {

    //添加节点
    void put(Integer e);

    //删除节点
    boolean remove(Integer e);

    //先序遍历结果
    Integer[] preOrderTraversal();

    //中序遍历结果
    Integer[] inOrderTraversal();

    //后序遍历
    Integer[] postOrderTraversal();

    //层次遍历
    Integer[] levelOrderTraversal();

    //根节点
    Integer rootNode();

    //深度
    int treeDepth();

    //叶子节点的数量
    int leafNodeCount();

    /**
     * 是否完全能二叉树:
     * 对于某个节点E,有2个孩子节点，或者一个孩子节点且为左节点，且该孩子节点为叶子节点，或者没有孩子节点
     *              1
     *            /   \
     *          2      3
     *        /       / \
     *      4        6   7
     */
    boolean isCompleteBinaryTree();

    /**
     * 是否满二叉树: 深度为h，节点数为 2^h -1
     *              1
     *            /   \
     *          2      3
     *        /  \    / \
     *      4    5   6   7
     */
    boolean isFullBinaryTree();

    void printf();

}
