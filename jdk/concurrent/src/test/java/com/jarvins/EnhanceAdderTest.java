package com.jarvins;


import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * 用于剖析Atomic类增强类型的工作原理,主要包含:
 * DoubleAccumulator,LongAccumulator,DoubleAdder,LongAdder
 * <p>
 * Question List:
 * 1,什么是伪共享?
 * 2,LongAdder实现同步的原理?其与AtomicLong的性能区别?
 * 3，LongAccumulator的作用和实现原理?
 */
public class EnhanceAdderTest {

    private final LongAdder adder = new LongAdder();

    @Test
    public void test_question_1() throws InterruptedException {
        /**
         * 伪共享:
         * 由于cpu的缓存最小单位是32字节(4个long型值，常见32 - 256),每次缓存更新都是从主内存加载32个字节到cache中，
         * 每当一个值失效时，cpu更新这个值的时候，会附带更新其所在缓存行的其他值(免费被更新到最新值)，
         * 但如果同一行的2个值同时被频繁更新写回主存，那么会产生一倍的额外主存加载数据的代价(每次a更新导致其他cpu缓存
         * 的a失效，必须重新对a，此时b也被更新了，而每次b被更新也会导致其他cpu的a失效，必须重新读a)
         *
         * java层面也一样:
         * @sun.misc.Contended 注解提供了强制解除伪共享问题的机制(字段前后增加128字节的padding)
         */

        //这里给出一个伪共享的例子:
        class T1 {
            volatile long x;
            volatile long y;

            public T1(long x, long y) {
                this.x = x;
                this.y = y;
            }
        }

        T1 t1 = new T1(0, 0);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                t1.x++;
            }
        }
        );
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                t1.y++;
            }
        }
        );

        long l1 = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("伪共享下耗时: " + (System.currentTimeMillis() - l1));

        class T2 {
            volatile long x;
            long x1, x2, x3, x4, x5, x6, x7;
            volatile long y;

            public T2(long x, long y) {
                this.x = x;
                this.y = y;
            }
        }

        T2 t2 = new T2(0, 0);
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                t2.x++;
            }
        }
        );
        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 100000000; i++) {
                t2.y++;
            }
        }
        );

        long l3 = System.currentTimeMillis();
        thread3.start();
        thread4.start();
        thread3.join();
        thread4.join();
        System.out.println("解除伪共享耗时: " + (System.currentTimeMillis() - l3));
    }

    @Test
    public void test_question_2() {
        /**
         * LongAdder的几个核心变量:
         * base(在并发较小的情况下使用，此时并发不会导致竞争)
         * NCPU(可使用的cpu数量)
         * Cell[] 竞争激烈情况下的数组
         *
         *
         * 初始状态下Cell[]数组是未被初始化的，此时线程多线程通过cas竞争修改base保证同步(这时LongAdder和AtomicLong的行为是一致的)
         * 并发量不大的情况下cas修改base操作不会失败，但是当并发达到一定量时，会出现cas失败的情况
         *
         * {@code
         *     public void add(long x) {
         *         Cell[] as; long b, v; int m; Cell a;
         *         //双向判断
         *         if ((as = cells) != null || !casBase(b = base, b + x)) {
         *             boolean uncontended = true;
         *             if (as == null || (m = as.length - 1) < 0 ||
         *                 (a = as[getProbe() & m]) == null ||
         *                 !(uncontended = a.cas(v = a.value, v + x)))
         *                 longAccumulate(x, null, uncontended);
         *         }
         *     }
         * }
         * 一旦出现cas失败，casBase()即会返回false,此时则会进入复杂的if判断:
         *
         *                                false
         *           celles == null ───────────────┐
         *                 │                       │
         *                true                     ↓                         false
         *                 │            cells[getProbe() & m] == null ───────────────┐
         *                 │                       │   // index = getProbe() & m     │
         *                 │                      true                               │                         true
         *                 │                       │                  cells[index].cas(v = a.value, v + x) ────────────→ return;
         *                 │                       │                                 │
         *                 │                       │                               false
         *                 │                       │                                 │
         *                 ├───────────────────────┴─────────────────────────────────┘
         *                 │
         *                 ↓
         *   longAccumulate(x, null, uncontended);
         *
         *
         *
         * longAccumulate(x, null, uncontended)很复杂，略微分析:
         * for(;;)死循环保证线程的更新一定会发生，只有更新成功才会退出
         * casCellsBusy()是伴随cells[]创建，扩容，cell创建都会调用的方法，其充当锁的角色，只有cas成功的线程才能执行之后的操作，后续finally中cellsBusy = 0意味着释放锁
         *
         * 当cells[]数组未被初始化时,casCellsBusy()成功的线程会创建容量为2的数组，并给随机下标(h & 1)创建一个cell(x).
         * 当cells[]已经被初始化过了，如果当前线程所在的cell未被初始化，则检查cellsBusy == 0 (说明当前没其他线程修改cells[]),让后创建cell(x)节点，casCellsBusy，并添加节点，最后释放锁
         *                        如果当前线程所在的cell被初始化了，再次尝试cas更新，成功返回
         *                                      如果失败，则检查cells的长度和NCPU(对于N核cpu,数组长度最大为N是合理的，此时不应该继续扩容了),或者cells数组是否已经扩容了，如果是则重新for循环
         *                                                    如果没有扩容且者没有达到NCPU,则casCellsBusy()扩容2倍，数组复制，最后释放锁
         *
         * 关于hash值的生成:
         * @see LongAdder#getProbe()
         * Cell中的PROBE,实际上是Thread类中的threadLocalRandomProbe的偏移量
         * 因此该方法实际返回的是Thread.threadLocalRandomProbe
         * 而Thread.threadLocalRandomProbe(初始0)则是在ThreadLocalRandom.current()方法中生成的:
         * 这里这个值只会被设置成2个值中的一个:1,0x9e3779b9
         * @see ThreadLocalRandom#current()
         * @see ThreadLocalRandom#localInit()
         *
         * 如上的hash值是不会发生变化的，但是cas失败至少有一种含义是:2个线程在竞争修改cells[index],因此cas失败的线程进入longAccumulate()方法中需要做一次修改hash的动作(未初始化，不存在cells[index]不做)
         * @see LongAdder#advanceProbe(int)
         * 这里假设初始hash值为1，10次变更的结果如下:
         * 270369
         * 67634689
         * -1647531835
         * 307599695
         * -1896278063
         * 745495504
         * 632435482
         * 435756210
         * 2005365029
         * -1378868364
         *
         *
         *
         * 大致主流程图:
         *
         *      ┌──────→   for(;;)   ←──────────────────────────────────────────────────────────────────────┐
         *      │           │                                                                               │
         *      │           ↓                           false                                        advanceProbe //重新计算PROBE值
         *      │        cells == null ───────────────────────────────────────┐                             ↑
         *      │           │                                                 │                             │
         *     false      true                                                ↓ //不存在这个元素               │           false
         *      │           │                                  cells[(cells.length - 1) & h]  == null  ─────────────────────────────────────┐
         *      │           ↓                                                 │                             │                               ↓  //再次尝试cas，成功直接退出
         *      └──── casCellsBusy()                                         true                           │               (a.cas(v = a.value, ((fn == null) ? v + x :             true
         *                  │                                                 │                             │                       fn.applyAsLong(v, x))))     ────────────────────────────────────→    END
         *                true                                                ↓             false           │                               │
         *                  │                                            cellsBusy == 0  ───────────────────│                              false
         *                  ↓                                                 │  //没有其他线程修改cells       │      true                     ↓  //检查是否发生扩容或者容量达到上限
         *           if (cells == as) {                                     true                            ├─────────────┬─  n >= NCPU || cells != as)
         *             //扩容前先检查没有其他线程扩容                              ↓  //创建元素                   │             │                 │
         *               Cell[] rs = new Cell[2];                      Cell r = new Cell(x);                │             │               false
         *               rs[h & 1] = new Cell(x);                             │              false          │             │                 │
         *               cells = rs;                                    casCellBusy() ──────────────────────┘             │  false          ↓
         *              }                                                     │                                           ├─────────  casCellBusy()
         *              //释放锁                                              true                                         │                 │
         *              finally{                                              │                                           │                true
         *               cellsBusy = 0                                        ↓                                           │                 ↓
         *              }                                            if ((rs = cells) != null &&                          │          if (cells == as) {
         *         //完成初始化，元素也插入了，可以退出                         (m = rs.length) > 0 &&                          │                //数组没被动过，扩容
         *                  │                                             rs[j = (m - 1) & h] == null) {                  └───────────────  Cell[] rs = new Cell[n << 1];
         *                  │                                          //这里需要重新获取，因为as只是当前线程中cells[]的引用                        for (int i = 0; i < n; ++i)
         *                  │                                          //cells[]扩容会导致引用变化，导致as不是最新的cells[]                          rs[i] = as[i];
         *                  │                                             rs[j] = r;                                                       cells = rs;
         *                  │                                            }                                                               }
         *                  │                                           //完成了添加元素，退出
         *                  │                                                  │
         *                  │──────────────────────────────────────────────────┘
         *                  ↓
         *                 END
         *
         * 其主要通过提供多个cas副本去降低CAS竞争失败的可能性，保证多线程下CAS空消耗CPU的可能性下降，提高效率
         * 但是其并不提供一致性保证，只能保证最终的一致性(sum函数并不提供原子的求和方式，同时刻的cell更新会被sum忽略)
         * @see LongAdder#sum()
         *
         * 和AtomicLong的性能比较:
         * 单线程下AtomicLong的性能 > LongAdder
         * 但随着线程数量增加，并发越多，LongAdder的性能优势越明显
         *
         */
    }

    @Test
    public void test_question_3() throws InterruptedException {
        /**
         * LongAccumulator提供了一个自定义双目运算器(accumulatorFunction)，允许自定义函数计算
         * 由于cas竞争失败的线程会做advanceProbe()重新计算hash值，所以当运算器涉及 * / 等运算时，结果有不确定性(竞争越激烈越不确定)
         */

        for (int j = 0; j < 10; j++) {
            LongAccumulator accumulator = new LongAccumulator((x, y) -> x * y + 1, 0);
            CountDownLatch latch = new CountDownLatch(10);
            ExecutorService service = Executors.newFixedThreadPool(10);
            Runnable r = () -> {
                for (int i = 0; i < 100; i++) {
                    try {
                        //模拟每个任务执行的之间不相同
                        Thread.sleep(ThreadLocalRandom.current().nextInt(0, 5));
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    accumulator.accumulate(i);
                }
                latch.countDown();
            };

            for (int i = 0; i < 10; i++) {
                service.execute(r);
            }
            latch.await();
            System.out.println(accumulator.get());
            service.shutdown();
        }
    }
}
