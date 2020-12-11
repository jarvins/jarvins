package com.jarvins.collection.set;

import com.jarvins.map.HashMapTest;
import org.junit.jupiter.api.Test;


/**
 * Question List:
 * 1,HashSet底层实现原理？
 * 2,HashSet的add(),remove()方法细节?
 * 3,HashSet如何保证不重复?
 */
public class HashSetTest {

    @Test
    public void test_question_1() {
        /**
         * HashSet<E>底层只依靠两个本地变量实现:
         * 1,HashMap<E,Object> map,实现HashSet的基础
         * 2,new Object() PRESENT,用于充当HashMap的Value值
         *
         * 因此可以看出来HashSet底层是完全依赖HashMap的
         * @see com.jarvins.map.HashMapTest
         *
         * 它的构造方式都满足HashMap的构造器,其实现的原理是:
         * 对每个值 E,构建这样的键值对 E - new Object();
         * 然后将其简化成一个HashMap去实现
         *
         * 由于HashMap实现了null key的支持，因此HashSet也支持null key
         */
    }

    @Test
    public void test_question_2() {
        /**
         * 由于HastSet底层完全依靠HashMap,其add()和remove()方法也依靠HashMap
         * @see HashMapTest#test_question_6()
         */
    }

    @Test
    public void test_question_3() {
        /**
         * Map接口直接规范了keySet()是唯一的，即有Set接口的属性，而HashSet即HasMap的ketSet部分，HashMap是如何保证Key唯一的:
         * 判断相同key的代码如下:
         * {@code (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))}
         * @see HashMapTest#test_question_6()
         */
    }
}
