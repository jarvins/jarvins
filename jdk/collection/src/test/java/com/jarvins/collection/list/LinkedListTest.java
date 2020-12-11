package com.jarvins.collection.list;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Question List:
 *  1,LinkedList实现了哪些接口?
 *  2,LinkedList的底层实现是如何的?
 *  3,LinkedList和Arraylist的区别?哪种场景应该使用哪种数据结构?
 *  4,LinkedList的add(),get(),set(),remove()方法的实现细节?
 *  5,LinkedList遍历删除问题?
 *  6,LinkedList是否线程安全?
 */
public class LinkedListTest {

    private LinkedList<Integer> list = new LinkedList<>();

    @BeforeEach
    public void init(){
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.add(8);
        list.add(8);
    }

    @Test
    public void test_question_1(){
        Assertions.assertThat(list instanceof List).isTrue();
        Assertions.assertThat(list instanceof Deque).isTrue();
        Assertions.assertThat(list instanceof AbstractSequentialList).isTrue();
        /*
        LinkedList一共实现了4个接口，其中2个标记接口: Cloneable, Serializable，
                                     2个功能接口: List,Deque,
                                     继承了AbstractSequentialList

        List接口规范了List集合的行为;
        Deque接口是双向队列接口:
                    --------------------------
              <==>     1    2   3   4    5      <==>
                    --------------------------
        AbstractSequentialList继承自AbstractList(规范了List的骨架实现)，它的设计基于这样的思考:
        对于以数组为底层实现的List(ArrayList),可以依靠数组的机制实现RandomAccess(AbstractList骨架方法)，而对于底层非数组的实现，
        如链表(LinkedList),需要给与一个独立的数据访问骨架(由于链表只支持顺序访问，因此其骨架方法强调Sequential),其实现希望依托于
        迭代器，因此所有实现都是基于列表迭代器ListIterator
        但是: 尽管LinkedList的内部类实现了ListIterator，其增删改查的操作都是基于链表的，而非基于AbstractSequentialList骨架实现
        todo LinkedList为什么不直接依靠ListIterator,而是基于链表实现,显得继承AbstractSequentialList毫无意义?
         */
    }

    @Test
    public void test_question_2(){
        /*
        LinkedList底层采用双向链表，同时外层使用一个头指针和一个尾指针:

                first                            tail
                  |                                |
                  V                                V
                Node<E>           Node<E>        Node<E>
       null <-- pre               pre            pre
                value    <--->    value   <--->  value
                next              next           next   --> null

         */
    }

    @Test
    public void test_question_3(){
        /*
        ArrayList底层依靠数组可以实现RandomAccess，其元素访问速度都是O(1)，单纯的add()方法比较快速，但add(index)方法会比较耗时，因为会导致
        后size - index的元素向后偏移1位(删除同理向前偏移)
        LinkedList底层依靠链表实现SequentialAccess，由于具有Deque的特性，访问元素的速度是O(1)~O(n/2)，越靠近链表中间越耗时，但add(index)成本
        比ArrayList低很多(尤其在元素很多的时候)

        在插入/删除频繁的场景，ArrayList的性能较差，应该使用LinkedList
        而在元素变化不频繁的场景，仅大量查询操作，ArrayList能实现O(1)的查询，性能比LinkedList好
         */

        //set()效率对比
        List<Integer> l1 = new ArrayList<>(10);
        for (int i = 0; i < 10000; i++) {
            l1.add(i);
        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            l1.set(5555,i);
        }
        long t2 = System.currentTimeMillis();

        List<Integer> l2 = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            l2.add(i);
        }
        long t3 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            l2.set(5555,i);
        }
        long t4 = System.currentTimeMillis();

        Assertions.assertThat(t2 - t1 < 100).isTrue();
        Assertions.assertThat(t4 - t3 > 3000).isTrue();

        //然而当量比较小的时候，LinkedList比ArrayList快

        //add(index)效率对比
        List<Integer> l3 = new ArrayList<>(1000000);
        for (int i = 0; i < 10000; i++) {
            l3.add(i);
        }
        long t5 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            l3.add(5555,i);
        }
        long t6 = System.currentTimeMillis();


        List<Integer> l4 = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            l4.add(i);
        }
        long t7 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            l4.add(5555,i);
        }
        long t8 = System.currentTimeMillis();

        Assertions.assertThat(t6 - t5 > 40000).isTrue();
        Assertions.assertThat(t8 - t7 < 15000).isTrue();
    }

    @Test
    public void test_question_4() {
        /**
         * add()
         * add()会直接在链表尾部插入新元素，由于tail指针存在，插入速度非常快，复杂度为O(1)(addFirst(),addLast()同理)
         *
         * addAll()
         * addAll()本质上和for循环addLast()没区别
         *
         * add(index)
         * 这里涉及如下方法：
         * @see LinkedList#node(int)
         * 它会根据idnex距离头尾指针的距离选择从头遍历还是从尾遍历，保证获取index下标的element复杂度在O(size/2)
         * 然后做头尾指针操作完成插入，其复杂度为O(1)
         * 因此如果插入元素的位置越靠近链表中间，耗时越多
         *
         * get(index)
         * 其本质是node(index).item，所以同add(index)
         *
         * remove()
         * 删除第一个元素,实际调用removeFirst()
         *
         * remove(index)
         * node(idnex)获取index对应的Node节点
         * unlink(Node)关闭节点头尾指针引用，连接相邻节点
         *
         * 当然所有涉及index的操作优先做index的合法性校验
         *
         */
    }

    @Test
    public void test_question_5(){
        /**
         * @see ArrayListTest#test_question_3()
         *
         * 这里需要注意: 虽然LinkedList没有显示检查modCount，但forEach本质上仍然是迭代器，一旦进行迭代器操作，就会检查modCount
         */
        Assertions.assertThatThrownBy(() -> list.forEach(e -> {
            if(e % 2 == 0){
                list.remove(e);
            }
        })).isInstanceOf(ConcurrentModificationException.class).as("forEach is essentially an iterator !");

    }

    @Test
    public void test_question_6() throws InterruptedException {
        /**
         * LinkedList同样不是线程安全的，这里给一段代码:
         * 并发插入做指针变更时并不保证同步，导致大量节点丢失
         * 同时多线程修改list会导致modCount变更异常，删除操作非常容易产生ConcurrentModificationException
         */
        List<Integer> list = new LinkedList<>();
        ExecutorService service = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        AtomicInteger integer = new AtomicInteger(0);
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                list.add(integer.getAndIncrement());
            }
            latch.countDown();
        };

        for (int i = 0; i < 3; i++) {
            service.submit(runnable);
        }

        latch.await();
        Assertions.assertThat(list.size() < 3000).isTrue();
    }

}
