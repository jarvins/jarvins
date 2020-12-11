package com.jarvins.collection.set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Question List:
 * 1,LinkedHashSet和HashSet有什么区别?它实现了什么特殊性质?
 */
public class LinkedHashSetTest {

    @Test
    public void test_question_1(){
        /**
         * LinkedHashSet与HashSet的关系，与LinkedHashMap和HashMap的关系一样,HashSet存在一个标识构造器:
         * @see java.util.HashSet#HashSet(int, float, boolean)
         * 用于创建底层基于LinkedHashMap的Set集合
         *
         * 实际上，LinkedHashSet依靠LinkedHashMap实现了访问顺序的控制(HashMap的hash值计算破坏顺序)
         * 但是它不支持访问顺序
         * @see LinkedHashSet#LinkedHashSet()
         */


        /**
         * 对比LinkedList
         * LinkedList并没做任何顺序控制，其依托于底层链表的实现自带顺序保证
         */

        Set<String> set1 = new HashSet<>(5);
        Set<String> set2 = new LinkedHashSet<>(5);
        String[] arr = new String[]{"Josh Bloch","Doug Lea","Arthur van Hoff","Neal Gafter"};
        for (int i = 0; i < arr.length; i++) {
            set1.add(arr[i]);
            set2.add(arr[i]);
        }
        Assertions.assertThat(Arrays.toString(set1.toArray()).equals("[Josh Bloch, Doug Lea, Arthur van Hoff, Neal Gafter]")).as("HashSet can not keep sequence!").isFalse();
        Assertions.assertThat(Arrays.toString(set2.toArray()).equals("[Josh Bloch, Doug Lea, Arthur van Hoff, Neal Gafter]")).as("LinkedHashSet can keep sequence!").isTrue();

    }
}
