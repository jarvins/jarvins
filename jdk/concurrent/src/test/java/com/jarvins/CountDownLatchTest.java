package com.jarvins;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Question List:
 * 1,CountDownLatch的功能是什么?
 * 2,CountDownLatch的底层实现原理?
 */
public class CountDownLatchTest {

    @Test
    public void test_question_1() throws InterruptedException {
        /**
         * CountDownLatch
         * 同步器，在其他线程完成执行一个同步辅助，允许一个或多个线程等待，直到一组操作。
         *
         * CountDownLatch是一个Thread.join()的完美替代方案，同时具有更高的灵活性,
         * 让所有线程等待某一个操作，或者让所有线程达到某个状态触发另一个线程工作
         */
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            service.execute(() -> {
                try {
                    //do something...
                    System.out.println("子线程执行完毕");
                } finally {
                    //countDown()方法做好放在finally中，避免中间抛出异常无法执行countDown()导致某个线程永远等待
                    latch.countDown();
                }
            });
        }
        service.shutdown();
        latch.await();
        System.out.println("============");
        System.out.println("主线程执行完毕");
    }

    @Test
    public void test_question_2(){
        /**
         * CountDownLatch本质上是一个共享锁(但它并没有实现Lock接口)
         * @see CountDownLatch#CountDownLatch(int count)
         * count实际上的含义是:当前有count个线程获取了锁
         *
         * @see CountDownLatch#countDown()
         * 释放一个锁,每次释放status++{@link java.util.concurrent.CountDownLatch.Sync#tryReleaseShared(int)}
         *
         * @see CountDownLatch#await()
         * 尝试获取锁，只有当status = 0(所有锁都被释放)才会成功，并唤醒WAIYING的线程
         * 这里await()方法会响应中断并抛出异常
         */
    }
}
