package com.jarvins.tree;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个简单的二叉树实现，仅满足二叉树的基本特性，put()和remove()的实现很糟糕，不值得参考
 * 为了便于输出二叉树的图例，仅支持Integer,且保证数值在[0,99]
 * 数据量不宜过大，不要超过20个，否则输出显示太长换行显示不正确(或者修改ide设置不换行输出)
 */
public class SimpleBinaryTree implements BinaryTree {

    //初始容量
    private final static int DEFAULT_CAPACITY = 8;


    //已填充元素个数
    protected int nodeCount;

    //底层数组
    protected Node[] table;

    //根节点
    Node root;

    public SimpleBinaryTree() {
        this(DEFAULT_CAPACITY);
    }

    public SimpleBinaryTree(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        }
        init(Math.max(initialCapacity, DEFAULT_CAPACITY));
    }

    private void init(int initialCapacity) {
        table = new Node[initialCapacity];
    }

    /**
     * 这里做随机插入,同时使二叉树尽量饱满，避免线性结构
     * 仅仅添加了元素，没有严密的内部约束，是一个不好的实现
     *
     * @param e 插入的元素
     */
    @Override
    public void put(Integer e) {
        Node o = new Node(e);
        put(o, true);
    }

    /**
     * 如果包含相同的元素，总是从最后一个插入的开始删除
     * 仅仅删除了元素，没有严密的内部约束，是一个不好的实现
     *
     * @param e 删除的元素
     */
    @Override
    public boolean remove(Integer e) {
        if (root == null) {
            return false;
        }
        //包含该元素
        if (contains(e)) {
            Node node = popLastNode(e);
            Node leftChild = node.leftChild;
            Node rightChild = node.rightChild;
            Node parent = node.parent;

            //remove Node e
            preRemove(node);

            //root
            if (parent == null) {
                root = leftChild != null ? leftChild : rightChild;
                if (root == leftChild) {
                    put(rightChild, false);
                }
            } else {
                put(leftChild, false);
                put(rightChild, false);
            }
            return true;
        }
        return false;
    }

    @Override
    public Integer[] preOrderTraversal() {
        Integer[] array = new Integer[nodeCount];
        return preOrderTraversal(root, array, new AtomicInteger(0));
    }

    @Override
    public Integer[] inOrderTraversal() {
        Integer[] array = new Integer[nodeCount];
        return inOrderTraversal(root, array, new AtomicInteger(0));
    }

    @Override
    public Integer[] postOrderTraversal() {
        Integer[] array = new Integer[nodeCount];
        return postOrderTraversal(root, array, new AtomicInteger(0));
    }

    @Override
    public Integer[] levelOrderTraversal() {
        Integer[] array = new Integer[nodeCount];
        LinkedList<Node> list = new LinkedList<>();
        if (root == null) {
            return new Integer[0];
        }
        list.push(root);
        return levelOrderTraversal(array, list, new AtomicInteger(0));
    }

    @Override
    public Integer rootNode() {
        return root.getValue();
    }

    @Override
    public int treeDepth() {
        return treeDepth(root);
    }

    @Override
    public int leafNodeCount() {
        int count = 0;
        for (Node node : table) {
            if (node != null && node.leftChild == null && node.rightChild == null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isCompleteBinaryTree() {
        if (root == null) {
            return false;
        }
        for (Node node : table) {
            if (node == null) {
                return true;
            }
            //只有右子没有左子，返回false
            if (node.leftChild == null && node.rightChild != null) {
                return false;
            }
            //只有左子，没有右子，则左子必为叶子节点
            if (node.leftChild != null && node.rightChild == null) {
                Node leftChild = node.leftChild;
                if (leftChild.leftChild != null || leftChild.rightChild != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isFullBinaryTree() {
        return nodeCount > 0 && Math.pow(2, treeDepth()) - 1 == nodeCount;
    }

    @Override
    public void printf() {
        if (root == null) {
            System.out.println("这是一颗空二叉树");
        } else {
            if(treeDepth() > 10){
                System.out.println("树高度大于10,暂不显示!");
                return;
            }
            String format;
            double l;
            double width = Math.pow(2, treeDepth() - 1) * 6;
            LinkedList<Node> list = new LinkedList<>();
            list.push(root);
            while (list.size() > 0) {
                int j = list.size();
                l = width / (2 * j) - 1;
                if (l < 1) {
                    break;
                }
                while (j > 0) {
                    Node node = list.pollLast();
                    assert node != null;
                    format = node.getValue() == -1 ? "  " : String.format("%02d", node.getValue());
                    list.push(node.leftChild == null ? new Node(-1) : node.leftChild);
                    list.push(node.rightChild == null ? new Node(-1) : node.rightChild);
                    System.out.printf("%" + l + "s" + format + "%" + l + "s", " ", " ");
                    j--;
                }
                System.out.println();
            }
        }
    }

    int treeDepth(Node e) {
        if (e == null) {
            return 0;
        } else {
            return 1 + Math.max(treeDepth(e.leftChild), treeDepth(e.rightChild));
        }
    }

    private Integer[] preOrderTraversal(Node e, Integer[] array, AtomicInteger index) {
        if (e != null) {
            array[index.getAndIncrement()] = e.getValue();
            preOrderTraversal(e.leftChild, array, index);
            preOrderTraversal(e.rightChild, array, index);
        }
        return array;
    }

    private Integer[] inOrderTraversal(Node e, Integer[] array, AtomicInteger index) {
        if (e != null) {
            inOrderTraversal(e.leftChild, array, index);
            array[index.getAndIncrement()] = e.getValue();
            inOrderTraversal(e.rightChild, array, index);
        }
        return array;
    }

    private Integer[] postOrderTraversal(Node e, Integer[] array, AtomicInteger index) {
        if (e != null) {
            postOrderTraversal(e.leftChild, array, index);
            postOrderTraversal(e.rightChild, array, index);
            array[index.getAndIncrement()] = e.getValue();
        }
        return array;
    }

    private Integer[] levelOrderTraversal(Integer[] array, LinkedList<Node> list, AtomicInteger index) {
        if (list.size() > 0) {
            Node pop = list.pollLast();
            array[index.getAndIncrement()] = pop.getValue();
            if (pop.leftChild != null) {
                list.push(pop.leftChild);
            }
            if (pop.rightChild != null) {
                list.push(pop.rightChild);
            }
            levelOrderTraversal(array, list, index);
        }
        return array;
    }

    void preRemove(Node e) {
        if (e.leftChild != null) {
            e.leftChild.parent = null;
        }
        if (e.rightChild != null) {
            e.rightChild.parent = null;
        }
        if (e.parent != null) {
            if (e.parent.leftChild == e) {
                e.parent.leftChild = null;
            } else {
                e.parent.rightChild = null;
            }
        }
        e.leftChild = null;
        e.rightChild = null;
        e.parent = null;
    }

    private void put(Node e, boolean isNewNode) {
        if (isNewNode) {
            if (root == null) {
                root = e;
            } else {
                Node n = root;
                resize();
                while (n.leftChild != null && n.rightChild != null) {
                    int i = ThreadLocalRandom.current().nextInt(1, 11);
                    //偶数
                    if (i == (i >> 1) * 2) {
                        n = n.rightChild;
                    } else {
                        n = n.leftChild;
                    }
                }
                if (n.leftChild == null) {
                    n.leftChild = e;
                } else {
                    n.rightChild = e;
                }
                e.parent = n;
            }
            table[nodeCount++] = e;
        }

        //删除后重新插入
        else {
            if (e != null) {
                Node n = root;
                while (n.leftChild != null && n.rightChild != null) {
                    n = n.leftChild;
                }
                if (n.leftChild == null) {
                    n.leftChild = e;
                } else {
                    n.rightChild = e;
                }
                e.parent = n;
            }
        }
    }

    void resize(){
        if (nodeCount == table.length) {
            Node[] oldTable = table;
            init(table.length * 2);
            System.arraycopy(oldTable, 0, table, 0, nodeCount);
        }
    }

    boolean contains(Integer e) {
        for (Node node : table) {
            if (node != null && node.value.equals(e)) {
                return true;
            }
        }
        return false;
    }

    //contains()交由外部实现，此方法调用前置条件便是包含
    Node popLastNode(Integer e) {
        Node n = null;
        for (int i = nodeCount - 1; i >= 0; i--) {
            if (table[i].getValue().equals(e)) {
                n = table[i];
                if (i == nodeCount - 1) {
                    table[--nodeCount] = null;
                } else {
                    System.arraycopy(table, i + 1, table, i, --nodeCount - i);
                    table[nodeCount] = null;
                }
                break;
            }
        }
        return n;
    }


    protected static class Node {
        Integer value;
        Node leftChild;
        Node rightChild;
        Node parent;

        protected Node(Integer value) {
            this.value = value;
            this.leftChild = null;
            this.rightChild = null;
            this.parent = null;
        }

        protected Node(Integer value, Node parent, Node leftChild, Node rightChild) {
            this.value = value;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        protected Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format(" %2d ", value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node node = (Node) o;
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
