package com.jarvins.collection.queue;

import org.junit.jupiter.api.Test;

import java.util.Queue;

/**
 *  Question List:
 *  1,队列接口的方法说明
 */
public class QueueTest {

    @Test
    public void test_question_1(){
        /**
         * 插入操作:
         * add()，offer()
         * 通常offer()比add()方法更好,add()方法在无法插入时会抛出异常，而offer()返回false
         *
         * 移除操作:
         * remove()，poll()
         * 同样，poll()操作比remove()方法更好，remove()方法在无法删除时抛出异常，而pol()返回false
         *
         * 检查操作:
         * element()，peek()
         *  同样，peek()操作比element()方法更好，element()方法在没有元素的时候抛出异常，而peek()返回false
         */
    }
}
