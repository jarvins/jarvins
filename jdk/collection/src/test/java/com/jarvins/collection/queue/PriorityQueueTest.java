package com.jarvins.collection.queue;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

/**
 * Question List:
 * 1,PriorityQueue有什么特性?
 * 2,PriorityQueue使用哪种方式存储元素?为什么要基于这种方式存储?
 * 3,PriorityQueue如何添加元素保证堆特性?
 * 4,PriorityQueue如何删除元素保证堆特性?
 */
public class PriorityQueueTest {

    @Test
    void test_question_1() {
        /**
         * PriorityQueue的元素必须是实现Comparable接口的，
         * 即使可比较的(因为添加元素的时候会将当前元素转成成Comparable实现，调用ComporaTo()方法进行比较排序)
         *
         * 任何时候PriorityQueue都是完全二叉树
         *
         * PriorityQueue具有堆的特性，且由于PriorityQueue支持比较器构建堆,因此它可以实现基本堆特性的任意堆
         * 堆:
         * 对于可比较规则M,存在广义上的大根堆和小根堆
         * 大根堆：对于规则M,父节点大于任意子节点
         * 小根堆：对于规则M,父节点小于任意子节点
         */
    }

    @Test
    void test_question_2() {
        /**
         * PriorityQueue使用数组来存储元素，其基于如下公式:
         * 若父节点下标为n,则:
         *         left = 2 * n + 1;
         *         right = 2 * n + 2;
         * 若某个节点的下标为n,则:
         *         parent = (n - 1) / 2
         * 基于如上的规则,很容易根据下标实现父子节点的关系绑定，而且数组的遍历的时间复杂度是O(1),满足堆的条件，且高效
         */
    }

    @Test
    void test_question_3() {
        /**
         * add()
         * @see java.util.PriorityQueue#add(Object)
         * @see java.util.PriorityQueue#siftUpUsingComparator(int, Object)
         *
         * 对于当前需要添加的元素x,其下标为k,为了保证堆序列，需要做向上比较:
         * 将当前元素与父元素比较，若满足条件，交换，直到下标为0(根元素)或不满足条件为止
         *
         * while(k > 0){
         * int parent = (k - 1)/2
         * if(v.compareTo(element[parent] < 0)
         *     break;  //不满足交换条件，结束
         * element[k] = element[parent];
         * k = parent;
         * }
         * element[k] = v;
         *
         *
         *
         * 对于当前状态，添加元素8:
         *                  10
         *          5                  9
         *      2       4          3        7
         *
         * 首先可以知道8的初始下标是:7
         * 其父元素是(7-1)/2 = 4,即2,且是2的左子
         * 然后做比较交换,进行2次比较交换，最终结果如下:
         *                  10
         *          8                  9
         *      5       4         3        7
         *    2
         */
    }

    @Test
    void test_question_4() {
        /**
         * remove()
         * @see PriorityQueue#remove(Object)
         * @see PriorityQueue#removeAt(int)
         *
         * 对于当前需要删除的元素x,其下标为k,为了保证顺序，需要做向下比较:
         * 将数组尾部的元素放置下标为k的位置,然后将其与子元素比较交换，直到满足条件为止
         *
         * int s = --size;
         * if (s == i) // 如果是最后一个元素，直接删除就好
         *     queue[i] = null;
         * else {
         *     E moved = (E) queue[s];  //数组的最后一个元素
         *     queue[s] = null;    //置空
         *     siftDown(i, moved);  //比较交换
         *
         *     //这里内部交换有一个判断: while(k < size >>> 1)
         *     //这个判断标识了非叶子节点,这是因为非叶子节点可以通过向下交换保证堆特性，而叶子节点是无法进行向下交换的，
         *     //而且，由于堆不保证子节点的关系，可能使用最后一个节点的反向替换无法保证堆特性,如下一个例子：
         *     //                   1
         *     //               5       2
         *     //             9   10  3   4
         *     //删除10这个元素，如果仅仅将4这个元素替换过来是无法保证堆特性的，
         *     //但是如果删除5这个元素，则可以通过将4与9，10进行比较的方式调整
         *
         *     //叶子节点未经过调整，则向上交换
         *     if (queue[i] == moved) {
         *          siftUp(i, moved);
         *          if (queue[i] != moved)
         *               return moved;
         *      }
         * }
         *
         *
         * 对于当前状态，删除元素10：
         *                  10
         *          5                  9
         *      2       4          3        7
         *
         * 末尾元素是7，将该位置元素置位null
         * 然后将7这位元素与10元素的子节点做比较交换,最终结果如下:
         *                   9
         *          5                  7
         *      2       4          3
         */
    }

}
