package com.jarvins.tree;

/**
 * 有序二叉树
 */
public interface SortedBinaryTree extends BinaryTree {


    //最小的节点
    Integer minValue();

    //最大的节点
    Integer maxValue();
}
