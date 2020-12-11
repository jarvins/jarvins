package com.jarvins.collection.queue;

import com.jarvins.map.HashMapTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Question List:
 * 1,ArrayDeque底层是如何实现的?扩容机制?
 * 2,ArrayDeque的双向队列操作实现细节?
 */
public class ArrayDequeTest {


    private static Deque<Integer> deque = new ArrayDeque<>(5);

    @Test
    public void test_question_1(){
        /**
         * ArrayDeque底层使用的数组，其本质是对ArrayList实现了Deque接口
         *
         * ArrayDeque支持无参构造，会默认创建空间为16的数组，而有参构造则会对容量进行计算，转换成2^n的容量并初始化数组
         * 这一点和HashMap很相似
         * 区别在于HashMap仅标识容量，但不会创建数组，而ArrayDeque会直接初始化数组
         *
         * 扩容方式:
         * 当且仅当数组满了才发生扩容(head == tail)，扩容1倍
         * @see HashMapTest#test_question_2()
         */
    }

    @Test
    public void test_question_2(){
        /**
         * ArrayDeque使用head和tail来标识队列的头和尾，但为了保证高效(避免头插不断移动数组),ArrayDeque被设计成循环数组
         * 初始状态: head = tail = 0,elementData[8]
         * 假设现在尾插5个元素:1,2,3,4,5
         * 其结果如下:
         * head   tail
         * ↓       ↓
         * 1 2 3 4 5 0 0 0
         * 此时若再尾插一个元素:5
         * 则只需要elementData[tail++] = 5
         *
         * 现在继续头插3个元素:6,7,8
         * 其结果如下:
         *          tail
         *           ↓
         * 1 2 3 4 5 8 7 6
         *           ↑
         *          head
         *
         * 此时head = tail,触发扩容机制(尾插的元素移动到头部，头部的元素拼接到后面,并修改head,tail):
         * head
         * ↓
         * 8 7 6 1 2 3 4 5 0 0 0 0 0 0 0 0
         *                 ↑
         *                tail
         *
         * 此时若再次尾插一个元素9，然后头插一个元素10，结果如下:
         *                  tail
         *                   ↓
         * 8 7 6 1 2 3 4 5 9 0 0 0 0 0 0 10
         *                               ↑
         *                             head
         *
         */


        /**
         * add()
         * {@code
         *      elements[tail] = e;
         *      tail = (tail + 1) & (elements.length - 1)
         *  }
         *   由于扩容机制(elements.length = 2^n)，elements.length - 1 = 0...1111
         *   因此tail = tail + 1;
         *
         *  addFirst()
         *  {@code
         *      elements[head = (head - 1) & (elements.length - 1)] = e;
         *  }
         *  初始head = 0,head - 1 = -1(11111111)
         *  因此头插的元素实际上是从数组尾部(length - 1)插入的
         *  此后head = head - 1;
         *
         *  poll()
         *  {@code
         *      E result = (E) elements[h];
         *      head = (h + 1) & (elements.length - 1);
         *  }
         * 由于head标识头插的最新元素，并且是从后向前插入
         * 所以elements[head]即poll()的结果
         * 同时head后移一位,标识当前最新的tail
         * 这里的巧妙之处在于:由于扩容时，头插数组和尾插数组交替了，但(h + 1) & (elements.length - 1)始终能保证head指向头结点
         *
         * pollLast()
         * {@code
         *      int t = (tail - 1) & (elements.length - 1);
         *      E result = (E) elements[t];
         * }
         * pollLast()则是一个poll()的反向过程
         * 这里只需注意tail标识的下一个存放下标，而head则表示当前存放下标
         *
         * 关于remove(),get()等方法主要思想同上
         */
    }
}
