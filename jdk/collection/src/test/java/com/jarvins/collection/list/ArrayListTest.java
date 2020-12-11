package com.jarvins.collection.list;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Question List:
 * 1,ArrayList的底层实现是什么？
 * 2,ArrayList的add()方法,set()方法,remove()方法的细节?
 * 3,ArrayList删除特定元素因该如何删除?
 * 4,ArrayList的扩容机制?
 * 5,ArrayList是线程安全的吗?
 */
public class ArrayListTest {

    private List<Integer> list = null;

    @BeforeEach
    public void init() {
        list = new ArrayList<>(5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        //test for unsafe remove ！
        list.add(2);
    }

    @Test
    public void test_question_1() {
        /*
        ArrayList底层采用数组，但它统一使用Object[]来存储元素,当创建一个ArrayList时，如果
        调用空构造器，则默认创建一个空Object[](elementData = {})，如果调用有参构造器，则会
        创建对应容量的数组(这个过程发生在add()方法中而非构造器中)。
         */

        /*
        这里需要说明:ArrayList实现了RandomAccess接口，这个接口只是表示资源可以随机访问(复杂度O(1)),
        原因是数组的访问可以从初始下标 + 偏移量实现O(1)的随机访问复杂度
         */

        //这里给出初始化的一段bug代码,其原因即数据初始化size发生在add()方法中
        Assertions.assertThatThrownBy(() -> {
            List<Integer> l = new ArrayList<>(10);
            l.set(1, 2);
        }).isInstanceOf(IndexOutOfBoundsException.class).as("a simple bug example !");
    }

    @Test
    public void test_question_2() {
        /**
         * @see ArrayList#add(Object)
         * add()方法会添加一个元素到list中，在添加之前会先判断当前元素总量(包含当前加入的元素)和当前数组大小
         * 进行比较，如果元素总量 > 数组大小，则证明要扩容,扩容方式为1.5倍扩容,{@code int newCapacity = oldCapacity + (oldCapacity >> 1);}
         * 1.5倍扩容保证了略大量的数据插入不会引发大量重复的数组复制行为，提高效率
         * 扩容结束后则会将原数组赋值到扩容的新数组中,{@code elementData = Arrays.copyOf(elementData, newCapacity);}
         *
         * 如果初始使用无参构造器创建对象，则会默认创建空数组: {}
         * 此时当发生add()方法时，若检查发现当前数组是空数组,则会默认创建大小为10的空数组，并完成空复制
         * 数组动态扩容结束后，则会直接简单调用{@code elementData[size++] = e}实现插入
         *
         * 对于add()的重载实现
         * @see ArrayList#add(int, Object)
         * 其实现也同样很简单，即优先判断index合法性，然后扩容判断，底层数组整体右移1个单位{@code System.arraycopy(elementData, index, elementData, index + 1,size - index);}
         * 然后对index下标赋值即可
         *
         */

        /**
         * @see ArrayList#set(int, Object)
         * set()方法会替换原有下标的元素，首先它会检查index是否合法，然后直接对该下标插入元素，并返回oldValue
         */

        /**
         * @see ArrayList#remove(Object)
         * ArrayList由于底层采用数组，因此其remove实际是依靠数组整体偏移实现的:
         * 查找当前元素所在的下标index，调用{@code System.arraycopy(elementData, index+1, elementData, index,numMoved);}
         * 来实现元素的删除。
         * 由于ArrayList允许null值，因此删除的匹配区分了null和Object，但逻辑一致
         */
    }

    @Test
    public void test_question_3() {
        /*
        for循环删除
        remove()方法会删除元素，并将改元素后面的元素向前移动，而在for循环自增后，是直接会跳过index的元素遍历的，导致遍历不完全(可以在删除后i--保证下标回退，但是不建议这样写).
        1    2(index)   2   3   4   5

        remove  2(2 % 2 ==  0);
        1    2(index)   3   4   5

        finish cycle
        1    2   3(index)   4   5
         */
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 == 0) {
                list.remove(i);
            }
        }

        /**
         * forEach循环删除会抛出ConcurrentModificationException
         * forEach操作中会记录当前ArrayList的modCount{@code final int expectedModCount = modCount;}
         * 而remove操作会使modCount自增，而forEach循环中会判断expectedModCount == modCount
         * 不等则会退出循环，在循环外显式抛出ConcurrentModificationException
         *
         * 实际上forEach的lambda本质上也是iterator迭代器
         */
        init();
        Assertions.assertThatThrownBy(() -> list.forEach(e -> {
            if (e.equals(3)) {
                list.remove(e);
            }
        })).isInstanceOf(ConcurrentModificationException.class).as("remove a element in forEach will cause ConcurrentModificationException !");


        /**
         * Iterator显示迭代搭配list.remove(),抛出ConcurrentModificationException
         * 这里原因同forEach删除
         * @see ArrayList#forEach(Consumer)
         * iterator.next()每次会检查expectedModCount == modCount,不等显式抛出ConcurrentModificationException
         */
        init();
        Iterator<Integer> iterator = list.iterator();
        Assertions.assertThatThrownBy(() -> {
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                if (next % 2 == 0) {
                    list.remove(next);
                }
            }
        }).isInstanceOf(ConcurrentModificationException.class).as("wrong use iterator !");


        /*
        增强for循环，原因同迭代器循环，增强for循环底层编译成迭代器循环
         */
        init();
        Assertions.assertThatThrownBy(() -> {
            for (Integer i : list) {
                if (i % 2 == 0) {
                    list.remove(i);
                }
            }
        }).isInstanceOf(ConcurrentModificationException.class).as("same reason as iterator !");


        //正确的删除方式

        /*
        倒序for循环，保证元素不被遗漏
         */
        init();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) % 2 == 0) {
                list.remove(i);
            }
        }

        /*
        显示迭代器删除，调用迭代器删除方法，而非list.remove()
         */
        init();
        Iterator<Integer> iterator1 = list.iterator();
        while (iterator1.hasNext()) {
            if (iterator1.next() % 2 == 0) {
                iterator1.remove();
            }
        }

        /**
         * removeIf()删除
         * @see ArrayList#removeIf(Predicate)
         */
        init();
        list.removeIf(e -> e % 2 == 0);
        System.out.println();
    }

    @Test
    public void test_question_4() {
        /**
         * @see ArrayListTest#test_question_2()
         * 如果扩容后容量 > Integer.MAX_VALUE - 8，则直接增大到Integer.MAX_VALUE
         */
    }

    @Test
    public void test_question_5() throws InterruptedException {
        /**
         * ArrayList不是线程安全的，其add方法和remove方法等均没做同步控制,这里给出一段代码:
         * 5个线程同时向list添加数据，理论上会添加50000个数据，但由于
         * {@code elementData[size++] = e;}并不是原子操作，导致实际并没有添加到50000个元素
         * 但最后一个元素必定是50000
         *
         * 同时多线程修改list会导致modCount变更异常，删除操作非常容易产生ConcurrentModificationException
         */
        AtomicInteger atomicInteger = new AtomicInteger(1);
        CountDownLatch latch = new CountDownLatch(5);
        List<Integer> ll = new ArrayList<>(50000);
        ExecutorService service = Executors.newFixedThreadPool(5);

        Runnable r = () -> {
            for (int i = 0; i < 10000; i++) {
                ll.add(atomicInteger.getAndIncrement());
            }
            latch.countDown();
        };
        for (int i = 0; i < 5; i++) {
            service.execute(r);
        }
        latch.await();
        Assertions.assertThat(ll.size() == 50000).as("real size must smaller than 50000 !").isFalse();
        Assertions.assertThat(ll.get(ll.size() - 1) == 50000).as("last element  must 50000 !").isTrue();
    }
}
