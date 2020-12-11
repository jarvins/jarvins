package com.jarvins.map;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Question List:
 * 1,Map和Collection有关系？有什么区别？
 * 2,Map接口有哪些主要方法？
 * 3,Map有哪些常见的实现,每种实现具体的使用场景是什么?
 */
public class MapInterfaceTest<K,V>{

    Map<Integer,String> map;

    public void init(){
        map = new HashMap<>();
        map.put(1,"a");
        map.put(2,"b");
        map.put(3,"c");
        map.put(4,"d");
        map.put(5,"e");
        map.put(6,"f");
        map.put(7,"g");
        map.put(8,"h");
        map.put(9,"i");
    }

    @Test
    public void test_question_1(){
        /*
        联系:
        Map接口和Collection是2个独立的接口，其都是Java集合类的顶层
        接口(Collection<E>继承了Iterable<E>).
        区别：
        Map接口实现的是k-v类型数据的存储，而Collection接口实现的是对象类型数据的存储.
        Map不能包括2个相同的键(这个行为是map存在的根本约束，即使可以实现一个有多个相同的key,
        但这种行为违反了map的本质)，而Collection普遍含义下，只是描述了一个对象的集合，
        因此可以包含多个相同的对象(即使是同一个引用)
         *
         */

    }

    @Test
    public void test_question_2(){
        init();
        /**
         * forEach()方法接受一个BiConsumer函数，实现遍历<k,v>的操作
         * @see Map#forEach(BiConsumer)
         */
        map.forEach((k,v) -> map.replace(k,String.valueOf(k)));
        assertThat(map.get(1).equals("1")).as("after biConsumer,value has changed").isTrue();

        init();
        /**
         * compute是对<k,v>的BiFunction,其有2个拓展实现:
         * computeIfAbsent,computeIfPresent
         * @see Map#compute(Object, BiFunction)
         * @see Map#computeIfAbsent(Object, Function)
         * @see Map#computeIfPresent(Object, BiFunction)
         */
        BiFunction<Integer,String,String> biFunction = (k,v) -> k + v;
        assertThat(map.compute(1,biFunction).equals("1a")).as("biFunction implement: 1 + \"a\" = \"1a\"").isTrue();
        Function<Integer,String> function = k -> k + "0000";
        map.computeIfAbsent(1,function);
        assertThat(map.get(1).equals("1a")).as("key 1 already exists,so function don't effect").isTrue();
        map.computeIfAbsent(0,function);
        assertThat(map.get(0).equals("00000")).as("key 0 don't exist,so the function effects").isTrue();

        init();
        /**
         * merge函数会将旧value和新value通过BiFunction进行替换
         * V oldValue = map.get(key);
         * V newValue = (oldValue == null) ? value :
         *              remappingFunction.apply(oldValue, value);
         * if (newValue == null)
         *     map.remove(key);
         * else
         *     map.put(key, newValue);
         * @see Map#merge(Object, Object, BiFunction)
         */
        BiFunction<String,String,String> bFunction = (v1,v2) -> v1 + v2;
        map.merge(1,"000",bFunction);
        map.merge(0,"000",bFunction);
        assertThat(map.get(1).equals("a000")).as("key 1 exists,function effects").isTrue();
        assertThat(map.get(0).equals("000")).as("key 0 not exists, function don't effect").isTrue();
    }

    @Test
    public void test_question_3(){
        /**
         * Map的常用主要实现有:
         * @see HashTableTest
         * @see HashMapTest
         * @see LinkedHashMapTest
         * @see TreeMapTest
         * @see ConcurrentHashMapTest
         */
    }

}
