package com.jarvins.map;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Question List:
 * 1,LinkedHashMap和HashMap之前有什么区别？有哪些场景适用于LinkedHashMap？
 * 2,LinkedHashMap底层实现细节与HashMap有什么区别？
 * 3,使用LinkedHashMap实现LRU算法
 */
public class LinkedHashMapTest {

    private static final int CAPACITY = 4;
    private Map<Object,Object> linkedHashMap = new LinkedHashMap<>();

    @Test
    public void test_question_1(){
        /**
         * LinkedHashMap继承自HashMap
         * @see HashMapTest
         */
        assertThat(linkedHashMap).isInstanceOf(HashMap.class);
        /*
        不同点:
        HashMap是无序的，即迭代不保证顺序(hash决定了顺序不固定)，而LinkedHashMap保证顺序，LinkedHashMap支持2种顺序:
        保持插入顺序: 即遍历顺序是插入顺序
        保持访问顺序: 被访问过的元素向后排
        为了保证顺序, LinkedHashMap对entity的实现更为复杂(双向链表[维护顺序]+一个单向链表[维护hash冲突]):
        HashMap:
              |  hash |  key  |  value  |  next<K,V>  |
        LinkedHashMap:
              |  before<K,V>  |  hash |  key  |  value  |  next<K,V>  |  after<K,V>  |

        因此:
        在需要保证顺序的场景下使用LinkedHashMap有优势
         */

        //使用默认顺序
        Person p1 = new Person("Josh Bloch",21);
        Person p2 = new Person("Doug Lea",22);
        Person p3 = new Person("Arthur van Hoff",23);
        Person p4 = new Person("Neal Gafter",24);
        linkedHashMap.put(1,p1);
        linkedHashMap.put(2,p2);
        linkedHashMap.put(3,p3);
        linkedHashMap.put(4,p4);
        linkedHashMap.get(1);
        linkedHashMap.get(2);
        Iterator<Map.Entry<Object, Object>> i1 = linkedHashMap.entrySet().iterator();
        int index1 = 1;
        while (i1.hasNext()){
            assertThat(i1.next().getKey().equals(index1++)).as("first input, first output").isTrue();
        }

        //使用保持发放稳顺序
        linkedHashMap = new LinkedHashMap<>(11,0.75f,true);
        linkedHashMap.put(1,p1);
        linkedHashMap.put(2,p2);
        linkedHashMap.put(3,p3);
        linkedHashMap.put(4,p4);
        linkedHashMap.get(1);
        linkedHashMap.get(2);
        //此时顺序变成了3，4，1，2，即被访问过的元素向后排
        Iterator<Map.Entry<Object, Object>> i2 = linkedHashMap.entrySet().iterator();
        int index2 = 1;
        while (i2.hasNext()){
            assertThat(i2.next().getKey().equals(index2++)).as("last get, last output").isFalse();
        }
    }

    @Test
    public void test_question_2(){
        /**
         * 由于LinkedHashMap继承自HashMap，因此LinkedHashMap的很多行为和HashMap是一致的，
         * 但HashMap提供了3个回调方法以满足LinkedHashMap实现其特定的行为:
         *
         * afterNodeAccess(Node<K,V> p)
         * 满足访问顺序的回调，当LinkedHashMap被设置为accessOrder = true,每次访问该节点，都会调用这个方法将被访问的节点设置成tail节点
         *
         * afterNodeInsertion(boolean evict)
         * 是否删除最老元素(LRU),内部判断的removeEldestEntry()逻辑，如果返回ture,则删除eldestEntry(实际上就是head元素)
         * @see LinkedHashMapTest#putByLatestRecentUsed(Object, Object)
         *
         * afterNodeRemoval(Node<K,V> p)
         * 相比HashMap，LinkedHashMap的entry 多了2个节点。因此删除元素后为了维护节点之间的关系(LinkedHashMap的顺序关系，而非链表的顺序)，需要调用此方法
         *  A  <=>  B <=> C
         *  remove B;
         *  A <=> C
         *  但维护A,C的hash链表的工作是HashMap的职责
         *
         * LinkedHashMap复用了HashMap的put方法(通过上诉3个回调函数保证LinkedHashMap的特性，同时重写了newNode方法,用于维护双向链表)，
         * 但对get方法进行了重写，主要因为accessOrder参数是LinkedHashMap独有了，这里需要检验该参数行执行afterNodeAccess()方法
         *
         * 总结:
         * LinkedHashMap大量复用了HashMap的代码,通过3个自定义回调函数和重写部分方法，以达到LinkedHashMap的目的: 维护双向链表的顺序
         */
    }

    @Test
    public void test_question_3(){

        linkedHashMap = new LinkedHashMap<>(11,0.75f,true);
        /**
         * LRU算法全称:最近最少使用算法
         * 这里借助LinkedHashMap实现，具体实现见:
         * @see com.jarvins.other.LRUAlgorithm
         */
        Person p1 = new Person("Josh Bloch",21);
        Person p2 = new Person("Doug Lea",22);
        Person p3 = new Person("Arthur van Hoff",23);
        Person p4 = new Person("Neal Gafter",24);

        linkedHashMap.put(1,p1);
        linkedHashMap.put(2,p2);
        linkedHashMap.put(3,p3);
        linkedHashMap.put(4,p4);
        //使用一下调整顺序
        linkedHashMap.get(2);
        linkedHashMap.get(4);

        //添加元素
        Person p5 = new Person("Frank Yellin",24);
        Person p6 = new Person("Roland Schemers",24);
        Person p7 = new Person("Laurence P. G. Cable",24);

        /*
        LRU
        1 3 2 4
        1 3 4 2
        3 4 2 5
        4 2 5 6
         */
        assertThat(putByLatestRecentUsed(2,p5).toString().equals("Doug Lea")).isTrue();
        assertThat(putByLatestRecentUsed(5,p6).toString().equals("Josh Bloch")).isTrue();
        assertThat(putByLatestRecentUsed(6,p7).toString().equals("Arthur van Hoff")).isTrue();
    }

    /**
     * 另一个办法是继承LinkedHashMap，重写removeEldestEntry方法:
     * <pre>
     *     @override
     *     protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
     *         return size() > CAPACITY;
     *     }
     * </pre>
     */
    private  Object putByLatestRecentUsed(Object key, Object value){
        if(!(linkedHashMap instanceof LinkedHashMap)){
            throw new RuntimeException("only supported for type: " + LinkedHashMap.class);
        }
        LinkedHashMap<Object,Object> m = (LinkedHashMap<Object,Object>)linkedHashMap;
        if(m.size() < CAPACITY || m.get(key) != null){
            return linkedHashMap.put(key, value);
        }
        Map.Entry<Object, Object> next = m.entrySet().iterator().next();
        Object remove = m.remove(next.getKey());
        m.put(key,value);
        return remove;
    }

    private class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person person = (Person) o;
            return age == person.age &&
                    Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        // bad override! only for test
        @Override
        public String toString() {
            return name;
        }
    }
}
