package com.jarvins.map;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Question List:
 * 1,HashMap的数据结构是怎样的？
 * 2,HashMap的初始构造器和容量问题?
 * 3,HashMap的key和value的限定？最好使用那些对象当做key？
 * 4,HashMap的扩容机制,链表插入方法？
 * 5,HashMap的链表转红黑树的细节？
 * 6,HashMap的put(),get(),remove()方法细节?
 * 7,hash冲突及解决方案？
 * 8,HashMap并发问题？
 */
public class HashMapTest {

    //由于HashMap细节不对外提供访问,这里主要以分析为主,附加简单的测试
    Map<Object, Object> hashMap = new HashMap<>();

    @Test
    public void test_question_1() {
        /**
         * HashMap使用数组+链表2种数据结构:
         * 数组:
         * 即桶,通过hash()来确定某个key-value应该放在哪个桶中，所有通过hash()函数结果相同的key-value会放在同一个桶的链表中，具体的hash()函数如下:
         * @see HashMap#hash(Object)
         *
         * 链表:
         * 用于解决hash冲突(其他方案如再哈希法,开放定址法)，所有HashMap.hash(Object key)相同的结果会被放入同一个链表中,这个链表是一个单项链表，不提供排序功能
         * @see HashMapTest#test_question_6()
         */

        //从hash()方法可以看到，HashMap是允许空值的，而且空key的键值对会被放入数组下标为0的位置
        assertThat(hashMap.put(null, 1) == null).as("this Node<k,v> will be putted in Array[0]").isTrue();
    }

    @Test
    public void test_question_2() {
        /**
         * HashMap在构造器时并未初始化底层数组，它仅仅只标识了threadHold变量(甚至不标识),直到第一次添加对象才会完成数组的构建
         *
         * @see HashMap#HashMap()
         * 无参构造器不会初始化threadHold变量，第一次添加元素默认使用16(默认值)去构建数组，即初始数组大小为16，最大容量为12(16 * 0.75)
         *
         * @see HashMap#HashMap(int)
         * @see HashMap.tableSizeFor(int cap);
         * 自定义容量，这个行为会覆盖默认容量设置，但这个行为会被tableSizeFor(int cap) 方法规范
         * 这个过程实际上是为了满足HashMap容量必须是2的幂，因此，会被如下转换:
         * 1 -> 1
         * 2 -> 2
         * 3 -> 4
         * 4 -> 4
         * 5 -> 8
         * 9 -> 16
         * ....
         * 最终创建的数组大小被重新计算的结果
         */
    }


    @Test
    public void test_question_3() {

        //HashMap对key,value没有任何限制
        //从hash()可以看出，当key == null 时，hash()的结果为0，表示这个元素放置在数组下标为0的链表中
        assertThat(hashMap.put(null, new Object()) == null).as("put key values null is valid for HashMap").isTrue();
        assertThat(hashMap.put(new Object(), null) == null).as("put value values null is valid for HashMap").isTrue();

        /*
        从使用上来说，任何Object都可以充当key,但如果要使用一个自定义类Person做key,我们若是重写了hashCode()方法和equals()方法,
        当我们无意中修改了对象的属性，导致hashCode被修改，最终致使key-value映射失效。
        因此，使用不可变类做key才能保证hashMap正常工作(对一个类重写其hashCode()方法和equals()方法是应该且必要的),
        所以常见的key是String,其不可变性，和hashCode算法的分散性可以让HashMap很好的工作
         */
        Person person = new Person("echo", 22);
        hashMap.put(person, 1);
        assertThat(person.hashCode() == 96329298).isTrue();
        person.setAge(24);
        assertThat(person.hashCode() == 96329300).as("hashCode is changed!").isTrue();
        assertThat(hashMap.get(person) == null).as("error occurs when object has been modify").isTrue();

    }

    @Test
    public void test_question_4() {
        /*
        关于HashMap,涉及2个参数:
        容量:指数组的大小
        装填因子:数组触发扩容的占比(即被使用的数组单元占总数组单元的比例)

        HashMap无参构造器默认0.75f的装填因子，HashMap默认16的初始容量大小，这里强制要求容量大小为2^n,比如传入11，最后会计算成16
        因此，默认情况下数组大小16，装填因子0.75f，当有13个桶被占用触发扩容机制(当前容量下，发生hash冲突的概率上升，hashMap性能达到瓶颈):

        HashMap扩容是一个非常消耗性能的行为:
        首先,数组被扩大为原来的2倍,2倍是 (capacity - 1) & hash() 决定的，这里拿16举例，其结果就是保留hash()结果的后4位(即hash()%16)，
        即从0000-1111,能完整覆盖0-15的下标，保证桶被合理使用且不会出现越界问题.
        然后对每个链表达到元素重新计算hash值(实际Node节点存储了改元素的hash值，所以只需要e.hash % (newCapatiey - 1))，放入新的桶链表中
        在使用之前预估一个数据体量，能有效的提高效率

        关于扩容的头插法和尾插法问题:
        原始:   A -> B -> C -> D
        头插:   D -> C -> B -> A  (每次元素都插在列表第一个位置)
        尾插:   A -> B -> C -> D  (每次都将新元素查到末尾)
        可以看出，头插会在扩容后改变链表元素顺序，尾插则不会，jdk8引起这个改变，是因为头插法在多线程下会形成闭环(A->B->A),而采用尾插则不会改变顺序，
        也不会引发闭环问题(但这个解决不能说HashMap可以用于多线程，因为大量非原子操作没保证原子性)
         */
    }

    @Test
    public void test_question_5() {
        /*
        当一个链表的长达达到8并且桶的长度达到64，会将链表转换红黑树，而当一颗红黑树的元素达到6，会再次转换成链表.
        转换红黑树的原因是: 链表只能顺序遍历，达到8时，其遍历的时间性能不如二叉树(O(n) -> O(logn))
        为什么是达到8转换，因为8个元素链表的平均查询时间持平红黑树，超过8个红黑树更效率
        为什么达到6个而不是7个进行树转链表，为了避免性能损耗，比如到达7个临界值，频繁添加删除
         */

        /**
         * @see HashMap.treeifyBin(Node<K,V>[] tab, int hash)
         * 关于红黑树
         * @see com.jarvins.tree.RedBlackTree
         */
    }

    @Test
    public void test_question_6() {

        /**
         * put()方法:
         * 1,计算hash值得到数组下标，没有hash冲突，直接放，发生冲突，顺着链表找key,找到覆盖value返回旧value(如果在putIfAbsent模式下不会覆盖)，没找到添加在链表末尾
         * 2,判断链表长度是否可转红黑树，可转就转
         * 3,判断是否需要扩容,需要执行resize()方法扩容
         *
         * 判断key重复的代码如下:
         * {@code (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))}
         *
         * @see HashMap.resize()
         * @see HashMap.putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict)
         *
         * get()方法:
         * 计算hash值得到下标，直接命中直接返回，未命中遍历链表(或数)，返回遍历结果
         *
         * remove()方法:
         * 计算hash值找到table的下标，通过table找到对应的节点，如果节点是链表，则顺序遍历查找该节点，如果是数节点，则树形查找该节点
         * 最后根据节点类型完成对应的删除
         * 关于红黑树的查找和删除细节
         * @see com.jarvins.tree.RedBlackTree
         */

    }

    @Test
    public void test_question_7() {

        /**
         * 注意:
         * 无论hash函数如何，都需要保证计算出来的index in [0, capacity-1],
         * 最好的方案是 hash & (capacity - 1)
         *
         *
         * int hashCode = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
         * hashCode = hashCode & (array.length - 1);
         * 关于: h ^ (h>>>16)
         * 为了降低hash碰撞的概率
         * 如果桶数量比较小，比如只有16个,最后计算下标时只占有4位(1111 = 16 - 1),那么直接进行与运算
         * 实际上就是取了hash的后4位,冲突的概率较高，而通过高16位与低16位异或，来增加hash的利用率，降低冲突
         */

        int h;
        Object[] array = new Object[8];
        Object key = new Object();
        Object value = new Object();
        int hashCode = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        hashCode = hashCode & (array.length - 1);

        //再hash法
        //如果发现当前下标已经有元素了，反复执行另一个hash函数H',直到不冲突为止
        while (array[hashCode] != null) {
            hashCode = hashCode & (hashCode * 31) & (array.length - 1);
        }
        array[hashCode] = value;

        //开放定址法,函数可以选择伪随机数(尽可能让hashCode分散)
        int i = 1;
        while (array[hashCode] != null && i <= array.length) {
            hashCode = (hashCode + i++) ^ (array.length - 1);
        }
        array[hashCode] = value;
    }

    @Test
    public void test_question_8() throws InterruptedException {
        /**
         * 这里给出一个并发的数据不可靠的例子:
         * 5个线程并发向map放入元素(i,i),在正确情况下，结束后map包含1000个元素，且对于任意key = i,value = i;
         * 然而下面的程序确会出现 key != value的情况
         * 其原因在于: put并不能保证并发的原子性
         */
        AtomicInteger integer = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable runnable = () -> {
            while (integer.get() < 1000) {
                hashMap.put(integer.get(), integer.getAndIncrement());
            }
            latch.countDown();
            ;
        };
        for (int i = 0; i < 5; i++) {
            executorService.execute(runnable);
        }
        latch.await();
        //找到一个异常key-value
        Map.Entry<Object, Object> entry = hashMap.entrySet().stream().filter(e -> ((int) e.getKey()) != ((int) e.getValue())).findFirst().orElse(null);
        assertThat(entry).as("there is a error mapping of key:%s,value:%s", entry.getKey(), entry.getValue()).isNotNull();
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
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age &&
                    Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
