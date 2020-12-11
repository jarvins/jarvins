package com.jarvins.collection.queue;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Question List:
 * 1,LinkedList如何实现Deque的各种操作?
 */
public class LinkedListTest {

    private static Deque<Integer> deque = new LinkedList<>();

    @Test
    public void test_question_1(){
        /**
         * 基于链表的实现比较简单，主要涉及以下几个函数:
         * node(int index);
         * unlinked(E e)
         * linked(E e);
         * @see com.jarvins.collection.list.LinkedListTest#test_question_4()
         *
         */
    }
}
