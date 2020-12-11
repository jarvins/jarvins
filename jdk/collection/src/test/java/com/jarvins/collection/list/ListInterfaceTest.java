package com.jarvins.collection.list;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

/**
 * Question List:
 * 1,List接口和Collection接口的区别?
 * 2,List接口有哪些实现?
 */
public class ListInterfaceTest {

    @Test
    public void test_question_1(){
        /**
         * 首先从代码层面，List接口重写了Collection的抽象方法，虽然方法一致，且方法注释基本一致，这个行为略有迷惑性，即java的继承
         * 优势在这里完全成为了重复代码(如Collection接口继承了Iterable接口，又在自己的接口中声明了iterator()方法)
         * 这种行为对于最顶层的接口是有意义的，即强调了这个接口的行为(基于List的实现应该将List当做顶层接口，而非Collection)
         *
         * 其次,List接口给出了针对于List的行为(这种行为是脱离了Collection的)，如:
         * @see List#clear()  清空List
         * @see List#indexOf(Object) 元素下标
         * @see List#get(int)   获取某一下标的元素
         * @see List#sort(Comparator)  排序
         * @see List#subList(int, int)  裁剪
         */
    }

    @Test
    public void test_question_2(){
        /**
         * List的常用主要实现:
         * @see ArrayListTest
         * @see LinkedListTest
         * @see VectorTest
         */
    }
}
