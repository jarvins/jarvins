package com.jarvins.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Question List:
 * 1,Hashtable的基本数据结构是什么样的？和HashMap有什么区别
 * 2,Hashtable是线程安全的么？如何实现的线程安全？会导致什么问题？
 * 3,Hashtable对key-value有什么要求？为什么？
 */
public class HashTableTest {

    Hashtable<Integer,Integer> hashtable;

    @BeforeEach
    public void init(){
        //默认调用new Hashtable(11,0.75f);
        hashtable = new Hashtable<>();
    }

    @Test
    public void test_question_1() {

        /**
         * Hashtable内部实现和HashMap一致，均为数组+链表，具体
         * @see HashMapTest#test_question_1()
         *
         * 区别：
         * Hashtable对于hashcode值相同的元素，做无限链表，如果插入的元素
         * 发生大量的hash冲突，导致某个桶的链表非常长，此时查找会非常耗时，
         * 而HashMap会在链表达到一定长度后变更为红黑树，提高查询和存储效率
         */
    }

    @Test
    public void test_question_2() {

        /**
         * Hashtable是安线程安全的，从设计之初就被设计成线程安全的类了，
         * 其主要保证线程安全的方式是对方法整体加synchronized锁，保证多线程
         * 场景下永远只有一个线程能操作Hashtable，相当于强制单线程，但这样对
         * 方法整体加锁导致并发场景下效率非常低下(尤其是频繁操作Hashtable),
         * 关于synchronized锁:
         * @see concurrent.com.jarvins.SynchronizedTest
         */
    }

    @Test
    public void test_question_3(){
        /**
         * hashTable的key,value均不允许null值,其原因如下:
         * put方法会优先检测value是否为空,然后会执行 key.hashCode()方法
         * @see Hashtable#put(Object, Object)
         */
        assertThatNullPointerException().isThrownBy(() -> hashtable.put(null,1)).as("null key is not valid for Hashtable");
        assertThatNullPointerException().isThrownBy(() -> hashtable.put(1,null)).as("null value is not  valid for Hashtable");
        //插入新key会返回null
        assertThat(hashtable.put(1,1) == null).as("a new key be putted in will return null").isTrue();
        //插入旧key会返回oldValue
        assertThat(hashtable.put(1,2) == 1).as("a old key be putted in will return old value").isTrue();
    }
}
