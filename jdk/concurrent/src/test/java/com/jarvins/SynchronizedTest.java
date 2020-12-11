package com.jarvins;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Question List:
 * 1,synchronized可以锁哪些东西，分别都有什么效果?
 * 2,synchronized底层的实现机制?
 * 3,Object对象的通信机制?
 * 4,Thread对象的状态机制?
 */
public class SynchronizedTest {

    @Test
    public void test_question_1() throws InterruptedException {
        /**
         * synchronized可以作用3个地方:
         * 1,实例方法
         * 当修饰实例方法时，实际上是对当前对象实例进行加锁，进入同步代码块时需要获得当前实例的锁
         * 2,静态方法
         * 当修饰静态方法时，实际上是对当前类加锁(所有类在加载的时候都会创建对应的Class对象,且该对象唯一)
         * 3,同步代码快
         * 对指定的对象进行加锁，进入同步代码快之前需要获得指定对象的对象锁
         */

        /**
         * 这里给出分别使用3种方式进行同步，然后并发执行4个任务，2个并发修改静态变量，2个并发修改私有变量
         * 然而结果却不是预期的400000,原因是:
         * 同步代码块和实例方法锁的是2个对象，并不构成真正的同步,即两个线程分别都可以同时获得锁，并行修改count变量
         * 但这个方式说明，可以最小粒度的去控制同步，即真正需要同步的代码才同步，2个没有竞争的方法可以实现不同步
         */
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        Runnable r1 = () -> {
            for (int i = 0; i < 100000; i++) {
                notStaticSynchronizedMethod();
            }
            latch.countDown();
        };

        Runnable r2 = () -> {
            for (int i = 0; i < 100000; i++) {
                staticSynchronizedMethod();
            }
            latch.countDown();
        };

        Runnable r3 = () -> {
            for (int i = 0; i < 100000; i++) {
                objectSynchronizedMethod();
            }
            latch.countDown();
        };
        pool.execute(r1);
        pool.execute(r2);
        pool.execute(r2);
        pool.execute(r3);
        latch.await();
        Assertions.assertThat(count).isLessThan(200000);
        Assertions.assertThat(staticCount).isEqualTo(200000);
    }

    @Test
    public void test_question_2() {
        /**
         * 一个对象在堆内存的存储方式如下:
         *
         *       ┌──────────────────┐     ┌─── Mark Word，存储对象的hashCode、锁信息或分代年龄或GC标志等信息
         *       │   Object header  │─────┼─── Class Pointer 类型指针指向对象的类元数据，JVM通过这个指针确定该对象是哪个类的实例
         *       ├──────────────────┤     └─── (如果是数组，还有一个length)
         *       │ Instance variable│─── 实例属性(常量，类信息，静态变量存在方法区)，存放属性数据信息，包括父类的属性信息
         *       ├──────────────────┤
         *       │    Fill data     │─── 填充信息，保证字节对齐，满足JVM的要求(JVM要求对象起始地址必须是8字节的整数倍)
         *       └──────────────────┘
         *
         * 完整的64位JVM的Mark Word(部分):
         * ┌───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
         * │ [无锁态]  unused(25 bit)     │           hashCode(31 bit)           │ unused(1 bit) │  年龄分代(4 bit)  │ 偏向锁位【1】 │ 锁标志位[01]  │
         * ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
         * │ [偏向锁态]   当前线程指针 JavaThread* (54 bit)          │  Epho(2 bit) │ unused(1 bit) │  年龄分代(4 bit)  │ 偏向锁位【1】 │ 锁标志位[01]  │
         * ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
         * │ [轻量级锁,自旋锁态]                                 指向线程栈中Lock Record的指针(62 bit)                                 │ 锁标志位[00]  │
         * ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
         * │ [重量级锁态]                                             指向重量级锁(Mutex)的指针(62 bit)                               │ 锁标志位[10]  │
         * ├───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┤
         * │ [GC标记态]                                            CMS过程用到的标记信息(62 bit)                                     │ 锁标志位[11]  │
         * └───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
         * 这里需要清楚:
         * 对象在不同的状态，Mark Word的格式是不固定的(比如还有COOPs && CMS free block....),占用8 bytes
         * 同样Class Pointer也使用8 bytes描述，但jvm默认开启指针压缩(+UseCompressedOops)，变成4 bytes
         * 如果有数组，则数组占用4 bytes(标识数组长度)
         *
         * 对于实例数据域:
         * java对象引用占用4 bytes，其余基本类型占用标准字节
         *
         * 最后，为了保证总大小为8 bytes的倍数，填充域自动对齐字节
         *
         */

        /**
         * monitor对象:
         * 每个对象实例都会有一个monitor对象生成，其实现由JVM的ObjectMonitor实现，它可以与对象一起创建，销毁
         * 其结构大致如下:
         *
         * ObjectMonitor(){
         * _owner  //指向持有ObjectMonitor对象的线程
         * _count  //锁重入次数
         * _EntryList //处于等待锁block状态的线程
         * _WaitSet  //处于wait状态的线程(wait状态会释放锁,object.wait()最终会调用ObjectMonitor.wait()使线程被封装成ObjectWaiter节点放入该链表，
         *             object.notify()会将第一个ObjectWaiter节点放入_EntryList集合(这里不绝对，跟策略有关，略复杂))
         * }
         * ┌───────────────────────────────────────────────────────────────────────────────┐
         * │          _EntryList         │          _Owner         │        _WaitSet       │
         * ├───────────────────────────────────────────────────────────────────────────────┤
         * │   Thread_1_0                │      (只能有一个线程）     │      Thread_3_1       │
         * │                                    Thread_2_1     ←─────(acquire)             │
         * │   Thread_1_1      (acquire)──────→               (release)──────→             │
         * │                             │                         │     Thread_3_2        │
         * └─────────  ↑    ──────────────────────────    │      ──────────────────────────┘
         *             │                                  ↓
         *      enter(tryAcquire)                   release and exit
         *
         */

        /**
         * 偏向锁(标志位01):
         * 基于这样的事实:经过研究发现，在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，因此为了减少同一线程获取锁(会涉及到一些CAS操作,耗时)的代价而引入偏向锁
         * 当对象从无锁状态去访问临界资源，会尝试对对象加锁(修改Mark Word的线程指针，这是个cas操作),成功之后，该对象的对象头从无锁状态变成偏向锁状态(头结构发生了变化)
         * 偏向锁不会自己去释放锁，只有当其他线程去获取锁时，cas修改锁对象的Mark Word的线程指针，成功之后原线程放偏向锁，新线程获取偏向锁
         * 如果是同一个线程反复获取释放锁，实际上二次操作是没任何额外负担的，因为锁对象的Mark Word的线程指针一直指向当前线程
         * 如果两个线程竞争cas修改线程指针，此时锁会升级成轻量级锁。
         *
         *
         * //升级
         * 1,在一个安全点停止拥有锁的线程
         * 2,遍历线程栈，如果存在锁记录的话，需要修复锁记录和Markword，使其变成无锁状态
         * 3,唤醒当前线程，将锁升级成轻量级锁
         *     ├─→   (1.线程在自己的栈桢中创建锁记录 Lock Record
         *     ├─→   (2,将锁对象的对象头的Mark Word复制到创建的锁记录中
         *     └─→   (3,将锁对象的Mark Word替换Lock Record的指针,同时将Lock Record的own指向Mark Word
         *
         *
         * 轻量级锁(标志位00):
         * 基于这样的事实:对绝大部分的锁，在整个同步周期内都不存在竞争,即cas操作往往都只有一个线程在执行
         * 自旋锁策略:
         * 基于这样的事实:同步代码执行时间很短很短，未获取到锁的线程只需略微等待就能获取到锁
         * 轻量级锁获取失败的线程并不会立即阻塞，因为直接通过OS去阻塞线程(由运行态转换成内核态)，然后恢复到运行态(恢复线程上下文信息)非常耗时，会极大降低性能
         * 此时,未获取锁的线程会自旋(循环判断能否获取锁，保证线程不会被阻塞),直到成功获取锁
         * 自旋锁的问题:
         * 如果同步代码快执行很耗时，自旋线程长时间空消耗cpu，降低效率
         * 如果很多线程同时在cas自旋锁，有的线程会多次竞争失败，增加空消耗cpu的时间
         *
         * 自旋锁必须被设置自旋次数，即cas竞争次数，如果在n次内都竞争不到锁，说明此时的竞争条件很多，轻量级锁不适合当前的临界资源访问场景
         * 此时轻量级锁会升级为重量级锁
         *
         *
         * 重量级锁(10):
         * 基于这样的事实:当前的并发很大，必须借助OS去真正阻塞未获取到锁的对象了，避免cpu被重复占用
         * 其实现是依靠monitor对象的，当一个线程获取到重量级锁(monitorenter指令)时，会cas修改monitor的owner指针，并将count值变更为1,当释放重量级锁(monitorexit指令)时，count恢复成0
         * 这里编译器会保证monitorenter指令和monitorexit指令匹配，即使程序发生异常无法主动释放锁
         * 在线程获取重量级锁期间，其他竞争锁的线程直接阻塞，被放入_EntryList集合，而主动wait()的线程会释放锁，进入_WaitSet集合，wait()结束后再次获取锁
         *
         * 锁释放:
         * 偏向锁不会主动释放锁，但是轻量级锁和重量级锁都是会主动释放锁的，退出同步块后会恢复到无锁状态
         * 此时需要保证锁的递进问题，即进入轻量级(重量级)状态的锁不能回到偏向锁状态,因此，其偏向锁位会被置位0，表示不可偏向，初始无锁状态的偏向锁位是1，表示可偏向
         *
         * 可重入问题:
         * monitor对象的_count是对可重入的实现，即同一个线程多次获取同一个对象的锁，其_count位会自增，退出同步块会自减
         * 这样设计是避免在同步块中调用同步方法导致死锁问题
         *
         */
    }

    @Test
    public void test_question_3() throws InterruptedException {
        /**
         * Object对象的通信方法:
         * wait(); //释放锁
         * notify(); //不释放，执行完释放锁
         * notifyAll(); //不释放，执行完释放锁
         *
         * 这3个方法均要在同步块中执行，因为这几个方法依赖于monitor对象
         * {@code
         *  synchronized(o){
         *      o.wait();
         *      o.notify();
         *      o.notifyAll()
         *  }
         * }
         */

        int maxGoodCount = 4;


        Thread comsumer = new Thread(() -> {
            while (true) {
                synchronized (mutex) {
                    if (count < 1) {
                        System.out.println("当前没有存货!");
                        try {
                            mutex.wait();
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }
                    if (count >= 1) {
                        System.out.println("当前有存货:" + count + ",正在消费，剩余:" + --count);
                        mutex.notify();
                    }
                }
            }
        });

        Thread producer = new Thread(() -> {
            while (true) {
                synchronized (mutex) {
                    if (count < maxGoodCount) {
                        System.out.println("当前存货小于" + maxGoodCount + "，正在生产,剩余" + ++count);
                        mutex.notify();
                    }
                    if (count == maxGoodCount) {
                        System.out.println("当前库存满，等待消费!");
                        try {
                            mutex.wait();
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });

        comsumer.start();
        producer.start();
        //保证主线程0.1s不停
        Thread.sleep(100);
        System.out.println("模拟结束");
    }

    @Test
    public void test_question_4() throws InterruptedException {
        /**
         * Thread中断:
         * @see Thread#interrupted()
         * 测试当前线程是否被中断，并且会清除中断标志位。
         *
         * @see Thread#isInterrupted();
         * 测试线程对象是否被中断，不清除中断状态。
         *
         * @see Thread#interrupt()
         * 仅仅设置中断标志，但不会中断该线程，如果该线程处于WAITING(TINE_WAITING)状态，会抛出{@link InterruptedException}
         */

        Thread t = new Thread(() -> {
            System.out.println("当前子线程正在运行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException exception) {
                System.out.println("子线程被中断");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException exception) {
                System.out.println("子线程被中断");
            }
            System.out.println("子线程结束");
        });

        t.start();
        System.out.println("子线程是否被中断:" + t.isInterrupted());
        t.interrupt();
        System.out.println("子线程是否被中断:" + t.isInterrupted());
        Thread.sleep(2000);
        System.out.println("子线程是否被中断:" + t.isInterrupted());
        Thread.sleep(5000);
    }

    //锁对象
    private final Object mutex = new Object();


    //类属性
    private static int staticCount;

    //对象属性
    private int count;

    //同步实例方法
    private synchronized void notStaticSynchronizedMethod() {
        count++;
    }

    //同步静态方法
    private static synchronized void staticSynchronizedMethod() {
        staticCount++;
    }

    //锁对象
    private void objectSynchronizedMethod() {
        synchronized (mutex) {
            count++;
        }
    }


}

