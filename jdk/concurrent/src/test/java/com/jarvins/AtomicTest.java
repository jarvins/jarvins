package com.jarvins;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.*;

/**
 * Question List:
 * 1,Atomic的并发机制和同步方法有什么区别?CAS机制的原理和问题?
 * 2,Atomic基础类型如何保证并发?
 * 3,Atomic数组类型如何保证并发?
 * 4,Atomic引用类型如何保证并发?
 * 5,Atomic对象的属性如何保证并发?
 * 6,Atomic增强类型如何保证并发?
 */
public class AtomicTest {

    @Test
    public void test_question_1() {
        /**
         * 关于乐观锁和悲观锁(注意这是个概念，并不是某种理论或工具)
         * 乐观锁:
         * 认为事情永远向好的方向发展，并发不会发生，可以肆无忌惮的并发。
         * 乐观锁基于CAS控制并发问题。
         *
         * 悲观锁:
         * 认为事情永远会出现并发问题，任何不到位的并发控制都会导致坏的结果。
         * 悲观锁基于临界资源的并发访问控制控制并发问题。
         *
         * Atomic和并发机制的区别:
         * synchronized便是悲观锁的一种实现，完全控制被修饰的代码块成为临界资源，保证永远只有一个线程在执行该代码块(粗粒度的并发控制，将整个的代码块封装成一个原子操作，但实际上只有极少地方会有并发问题)
         * 而Atomic类(整个concurrent包)都是乐观锁的实现，从原子并发行为上达到细粒度的并发控制(即只控制真正的并发的地方，其他地方不作任何并发控制)
         *
         * CAS的机制:
         * java层面的CAS抵赖Unsafe类的native方法,Unsafe则是直接调用c语言代码，c代码直接调用汇编程序，这是OS层面的调用，最后OS会完成对cpu对外暴露接口的调用，最终完成cpu级别的CAS操作(cmpxchg指令)
         * cmpxchg指令触发LOCK信号，锁住数据总线，保证只有当前cpu会去修改该变量，并写回内存(此时为了保证缓存的一致性，其他cpu的缓存数据已经失效了)，这样能真正实现多处理器并发操作，保证并发安全(synchronized只是使用了这个机制实现了一个粗粒度的锁)
         * @see UnsafeTest#test_question_1()
         *
         * ABA问题:
         * 并发出现值
         *
         * 解决方案(判断当前对象已被修改):
         * 标签方式，对每次修改都修改标签，即使数据在ABA之间没发生变化，但标签已经变了，这时进行CAS操作是失败的(标签预期值发生了变化)
         *
         */
    }

    @Test
    public void test_question_2() throws InterruptedException {
        /**
         * 基本类型(以AtomicLong为例):
         * AtomicBoolean,AtomicInteger,AtomicLong
         *
         * incrementAndGet()方法
         *  ↓
         * return unsafe.getAndAddLong(this, valueOffset, 1L) + 1L;
         *  ↓
         * {@code
         *     public final long getAndAddLong(Object var1, long var2, long var4) {
         *         long var6;
         *         do {
         *         //获取对应值的最新写回值
         *             var6 = this.getLongVolatile(var1, var2);
         *             //cas修改，增加var4，如果成功，则退出，失败,则不断自旋cas竞争修改
         *         } while(!this.compareAndSwapLong(var1, var2, var6, var6 + var4));
         *
         *         //这里乐意看出返回的是旧值,因此incrementAndGet()方法拿到返回值后 +1L
         *         return var6;
         *     }
         * }
         *
         */
        AtomicLong longValue = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(5);
        Runnable r = () -> {
            for (int i = 0; i < 100000; i++) {
                longValue.incrementAndGet();
            }
            latch.countDown();
        };
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        latch.await();
        Assertions.assertThat(longValue.get() == 500000).isTrue();
    }

    @Test
    public void test_question_3() throws InterruptedException {

        /**
         * 数组类型(以AtomicIntegerArray为例):
         * AtomicInterArray,AtomicLongArray,AtomicReferenceArray
         *
         * 先研究下其static方法:
         * {@code
         *     static {
         *         //获取每个数组元素占用的长度
         *         int scale = unsafe.arrayIndexScale(int[].class);
         *         if ((scale & (scale - 1)) != 0)
         *             throw new Error("data type scale not a power of two");
         *         //shift表示的当前每个元素的字节偏移量
         *         //scale = 4(int数组每个元素占用4个字节，其字节偏移量是2，方便位移操作)
         *         shift = 31 - Integer.numberOfLeadingZeros(scale);
         *     }
         *
         *     //获取下标为index的元素的真实偏移量
         *     private static long byteOffset(int i) {
         *     //这里实际当前下标的真实偏移量(第n个元素实际的偏移量 = n * 4 + base = n << shift + base)
         *         return ((long) i << shift) + base;
         *     }
         * }
         *
         * getAndAdd(i,add)
         * ↓
         * return unsafe.getAndAddInt(array, checkedByteOffset(i), delta);
         * //checkedByteOffset(i)实际是做一个下标检查[0,length)，然后调用byteOffset(获取真实下标)
         * ↓
         * //最后unsafe.getAndAddInt()就是CAS过程了
         *
         */
        AtomicIntegerArray array = new AtomicIntegerArray(4);
        CountDownLatch latch = new CountDownLatch(5);
        Runnable r = () -> {
            for (int i = 0; i < 4; i++) {
                array.getAndAdd(i,20);
            }
            latch.countDown();
        };
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        latch.await();

        for (int i = 0; i < 4; i++) {
            Assertions.assertThat(array.get(i) == 100).isTrue();
        }
    }

    @Test
    public void test_question_4() throws InterruptedException {

        /**
         * 引用类型(以Atomic)
         * AtomicReference,AtomicStampedReference(带版本号的引用类型),AtomicMarkableReference(带有标记位的引用类型)
         *
         * //内部实现邮票的对象
         * {@code
         * private static class Pair<T> {
         * final T reference;
         * final int stamp;
         *      }
         * }
         *
         * compareAndSet()
         * {@code
         *   Pair<V> current = pair;
         *   return
         *      expectedReference == current.reference &&
         *      expectedStamp == current.stamp &&
         *      ((newReference == current.reference &&
         *      newStamp == current.stamp) ||
         *      casPair(current, Pair.of(newReference, newStamp)));
         * }
         * 最后使用Unsafe做对象原子修改操作:
         * UNSAFE.compareAndSwapObject(this, pairOffset, cmp, val);
         */

        TestClazz clazz = new TestClazz(18,"Doug Lea");
        AtomicInteger integer = new AtomicInteger(0);
        AtomicStampedReference<TestClazz> reference = new AtomicStampedReference<>(clazz,integer.get());
        CountDownLatch latch = new CountDownLatch(3);
        Runnable r = () -> {
            for (int i = 0; i < 100; i++) {
                reference.compareAndSet(clazz, clazz.addAge(), integer.get(), integer.incrementAndGet());
            }
            latch.countDown();
        };
        new Thread(r).start();
        new Thread(r).start();
        new Thread(r).start();
        latch.await();
        Assertions.assertThat(clazz.getAge() < 318).isTrue();
        /**
         * 这里分析下为什么age < 318(可能等于)
         * 相比于getAndSet()这类方法(CAS失败重试)，compareAndSet()仅做一次CAS，它是可能失败的，由于integer是可靠的更新，
         * 可能存在某几次CAS是失败的
         */
    }

    @Test
    public void test_question_5(){

        /**
         * 对象的属性(只支持Integer属性字段):
         * AtomicIntegerFieldUpdater(对象的属性是整型),AtomicLongFieldUpdater(对象的属性是长整型),AtomicReferenceFieldUpdater(对象的属性是引用类型)
         *
         * 利用反射构造Field对象，整体实现和AtomicInteger差不多
         */
        TestClazz clazz = new TestClazz(18,"Doug Lea");
        AtomicIntegerFieldUpdater<TestClazz> updater = AtomicIntegerFieldUpdater.newUpdater(TestClazz.class,"name");
        //这里会抛出异常
        updater.getAndIncrement(clazz);
    }

    @Test
    public void test_question_6(){
        /**
         * 增强类型:
         * DoubleAccumulator,LongAccumulator,DoubleAdder,LongAdder
         * @see EnhanceAdderTest
         */
    }

}
class TestClazz{
    int age;
    String name;

    public TestClazz(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public TestClazz addAge(){
        age++;
        return this;
    }

    @Override
    public String toString() {
        return "TestClazz{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
