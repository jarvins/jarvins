package com.jarvins.collection;


import com.jarvins.map.MapInterfaceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Question List:
 * 1,Collection接口和Map接口的联系和区别?
 * 2,Collection接口有哪些主要方法?
 * 3,关于<T> T[] toArray(T[] a);的吐槽?
 */
public class CollectionInterfaceTest{

    private Collection<Integer> collection;

    public void init() {
        collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);
        collection.add(5);
    }

    @Test
    public void test_question_1() {
        /**
         * Collection<E>接口继承了Iterable<E>，说明其所有实现都是可以显示迭代的
         * 但并不意味着只有集合可以显示迭代，因此Iterable独立出来，成为了一个顶层接口
         * 真正的集合行为全部由Collection接口规定
         * @see MapInterfaceTest#test_question_1()
         */
    }

    @Test
    public void test_question_2() {
        /**
         * toArray方法支持Collection集合转数组
         * 其存在一个安全类型转换的变形，toArray(T[] arr)
         */
        init();
        Object[] array = collection.toArray();
        assertThat(array.length == 5).isTrue();
        assertThat((int) array[0] == 1).isTrue();

        /**
         * retainAll会保留所有参数中的元素，删除所有不包含的元素
         * @see Collection#retainAll(Collection)
         */
        List<Integer> list = Arrays.asList(1, 3, 5);
        collection.retainAll(list);
        assertThat(collection.contains(1)).isTrue();
        assertThat(collection.contains(4)).isFalse();
        assertThat(collection.size() == 3).isTrue();
    }

    @Test
    public void test_question_3(){
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        String[] array = new String[2];
        /*
        <T> T[] toArray(T[] a);接口本质上是将Collection<E>插入T[],
        这种行为在T 和 E无关时会导致ArrayStoreException,但对这个方法的实现会直接底层数组复制，
        或者:
            Object[] o = a;
            o[index++] = e.item;

        这种行为都屏蔽掉了java泛型编译期的类型检查，从而留下了隐藏的缺陷,如果将T改成E,或者 ? extends E,
        至少插入不会抛出异常
        todo 为什么Collection不定义这样一个接口: <E> E[] toArray(E[] a);?
         */
        Assertions.assertThrows(ArrayStoreException.class,() -> list.toArray(array));

    }

}
