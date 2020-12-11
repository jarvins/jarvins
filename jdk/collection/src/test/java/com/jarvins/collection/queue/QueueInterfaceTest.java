package com.jarvins.collection.queue;

import org.junit.jupiter.api.Test;

import java.util.Deque;

/**
 * Question List:
 * 1,Queue是一种怎样的数据结构？他有什么性质?
 * 2,常见的队列有哪些?
 * 3,队列的方法说明?
 */
public class QueueInterfaceTest {


    @Test
    public void test_question_1(){
        /*
        队列是一种先进先出的数据结构，它与栈相反(先进后出)，它有一个入口，一个出口(不相同)
        队列的高级实现:双向队列则支持同时在入口和出口(插入/删除)数据，实现双向队列的同时可实现栈结构
        队列在并发使用较多，并发模块会详细讲解
         */
    }

    @Test
    public void test_question_2(){
        /**
         * Queue集合的常用实现有:
         * @see java.util.ArrayDeque
         * @see java.util.LinkedList
         * 实际上这两个实现是Deque基于链表和数组的两种实现方式
         */
    }


    @Test
    public void test_question_3(){

        /**
         * 队列有很多命名方法,这里给出常用的队列(双向队列)的发方法:
         *
         * 构成队列结构:
         * @see Deque#add(Object) 在队列的尾部添加元素,等价addLast(),同理，addFirst()是队列头部添加元素
         * @see Deque#offer(Object)  在队列的尾部添加元素,如果容量满了，则返回失败,等价offerLast()(在容量不限制的实现中如ArrayDeque，offer()和add()没任何区别)，同理offerFirst()是队列头部offer()
         * @see Deque#poll() 返回队列头元素，删除，等价pollFirst(),同理，pollLast()返回并删除队尾元素
         * @see Deque#remove(Object) 从头部开始删除第一个等价元素，同理，removeFirst()删除队首元素，removeLast()删除队尾元素
         * @see Deque#getFirst() 返回队头元素，同理getLast()返回队尾元素
         *
         *
         * 构成栈结构:
         * @see Deque#push(Object) 在队列头添加元素
         * @see Deque#pop() 返回队列头元素，删除
         * @see Deque#peek() 返回队列头元素，但不删除
         *
         */
    }
}
