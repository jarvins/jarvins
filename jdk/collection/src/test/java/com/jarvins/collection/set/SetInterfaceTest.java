package com.jarvins.collection.set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Question List:
 * 1,Set集合和Collection有什么联系?它和List集合有什么区别?
 * 2,常用的Set集合有哪些?
 */
public class SetInterfaceTest {

    @Test
    public void test_question_1(){
        /*
        Set集合继承自Collection接口，但它和List接口表现出不同的性质:
        List是单纯的元素集合，而Set是唯一元素集合，即集合中不允许出现相同的元素。
         */
        List<Integer> list = new ArrayList<>();
        Set<Integer> set = new HashSet<>();

        list.add(1);
        list.add(1);
        Assertions.assertThat(list.size() == 2).isTrue();

        set.add(1);
        set.add(1);
        Assertions.assertThat(set.size() == 1).isTrue();
    }

    @Test
    public void test_question_2(){
        /**
         * Set集合主要常用实现有:
         * @see HashSetTest
         * @see LinkedHashSetTest
         * @see TreeSetTest
         */
    }
}

