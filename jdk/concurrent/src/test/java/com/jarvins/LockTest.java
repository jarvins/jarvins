package com.jarvins;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Question List:
 * 1,Lock是如何保证并发的?
 * 2,Lock与synchronized的区别于联系?
 * 3,Lock为什么需要Condition?
 */
public class LockTest {

    private static final Object mutex = new Object();

    private final Lock lock = new ReentrantLock();
    private int count;

    @Test
    public void test_questionP_1() throws InterruptedException {
        /**
         * Lock底层上依赖于Unsafe类的CAS
         * @see UnsafeTest
         * @see LockSupportTest
         * @see AQSFrameworkTest
         */

        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        Runnable r = () -> {
            try {
                lock.lock();
                for (int i = 0; i < 100000; i++) {
                    count++;
                }
            } finally {
                latch.countDown();
                lock.unlock();
            }
        };
        for (int i = 0; i < 5; i++) {
            service.execute(r);
        }
        latch.await();
        Assertions.assertThat(count == 500000).isTrue();
    }

    @Test
    public void test_question_2() throws InterruptedException {
        /**
         * 表现的区别:
         * synchronized以关键字的方式内置于java语言中，而Lock则是以接口的方式暴露的。
         * synchronized在jvm层面保证了代码异常释放锁，而lock并未保证，为了保证锁释放(不然会死锁)，lock必须在finally中释放锁。
         * lock支持中断响应(lockInterruptibly())，而synchronized过程中等待的线程会一直等待，无法响应中断
         * lock可以通过返回值确定是否获取到锁(tryLock())
         *
         * 实现方式的区别:
         * synchronized通过jvm实现，在jvm层面通过MonitorObject和对象头的Mark Word来保证同步。
         * Lock则基于CAS保证同步。
         *
         * 通信方式:
         * synchronized通过Object.wait()/Object.notify()通信(基于ObjectMonitor.wait()和ObjectMonitor.notify())
         * @see SynchronizedTest#test_question_2()
         * Lock通过LockSupport对象通信
         * @see LockSupportTest
         *
         */

        //thread_2不响应中断信号
        Runnable r = () -> {
            synchronized (mutex) {
                System.out.println("当前线程:" + Thread.currentThread().getName() + "获取到锁");
                for (int i = 0; i < 1000000000; i++) {
                    for (int j = 0; j < 1000000000; j++) {
                        for (int k = 0; k < 100000000; k++) {
                            k++;
                        }
                    }
                }
            }
        };
        Thread thread_1 = new Thread(r);
        Thread thread_2 = new Thread(r);

        thread_1.start();
        thread_2.start();
        thread_2.interrupt();

        //thread_4响应中断信号
        Runnable run = () -> {
            try {
                lock.lockInterruptibly();
                System.out.println("当前线程:" + Thread.currentThread().getName() + "获取到锁");
                for (int i = 0; i < 1000000000; i++) {
                    for (int j = 0; j < 1000000000; j++) {
                        for (int k = 0; k < 100000000; k++) {
                            k++;
                        }
                    }
                }
            } catch (InterruptedException exception) {
                System.out.println("当前线程:" + Thread.currentThread().getName() + "等待锁期间被中断");
            } catch (Exception e){
                System.out.println("当前线程:" + Thread.currentThread().getName() + "发生异常，释放锁");
                lock.unlock();
            }
        };

        Thread thread_3 = new Thread(run);
        Thread thread_4 = new Thread(run);
        thread_3.start();
        thread_4.start();
        thread_4.interrupt();
        Thread.sleep(5000);
    }

    @Test
    public void test_question_3(){
        /**
         * synchronized的通信机制依靠Object.wait()/Object.notify()
         * 这种通信机制依赖于其获取了对应的监视对象的锁
         *
         * 而Lock机制的锁并不依赖monitor对象，这时采用Object的通信机制将会发生错误,
         * 因此Lock需要一套属于自身的通信方式，即Condition,其底层采用LockSupport
         * @see LockSupportTest
         */
    }
}
