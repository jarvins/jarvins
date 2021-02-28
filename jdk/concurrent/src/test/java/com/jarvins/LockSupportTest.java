package com.jarvins;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Question List:
 * 1,LockSupport是干什么的?
 * 2,LockSupport有哪些方法,底层实现原理是什么?
 * 3,基于LockSupport实现一个生产者/消费者模型?
 * 4,基于LockSupport实现一个先进先出的不可重入锁?
 * <p>
 * Annotate: 一下所有WAITING均包含(TIME_WAITING)
 */
public class LockSupportTest {

    int count;

    @Test
    public void test_question_1() {
        /**
         * LockSupport提供了线程WAITING和RUNNING的源语
         * 基于这个源语，可以实现线程通信的控制
         *
         * 类比Object的线程WAITING(wait())和RUNNING(notify())的源语
         */
    }

    @Test
    public void test_question_2() {
        /**
         * park()
         * @see LockSupport#park()
         * @see LockSupport#park(Object)
         * @see LockSupport#parkNanos(long)
         * @see LockSupport#parkNanos(Object, long)
         * @see LockSupport#parkUntil(long)
         * @see LockSupport#parkUntil(Object, long)
         *
         * park()方法会使某个线程进入WAITTING状态，禁止参与线程调度，如果持有许可，该方法会立即返回
         * 该方法会响应interrupt()方法，但是不会抛出{@link InterruptedException}
         *
         * park的所有方法都支持阻塞一个对象，一般都是this对象(因为调用park()的线程会进入WAITING状态)
         *
         * parkNanos()表示等待的纳秒数
         * parlUtil()表示等到绝对毫秒数
         */
        Thread thread = Thread.currentThread();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                //响应interrupt()但是不会抛出异常
                thread.interrupt();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
        LockSupport.park();
        System.out.println("ok");

        /**
         * unpark(Thread o)
         * @see LockSupport#unpark(Thread)
         *
         * unpark()让指定的线程唤醒，进入BLOCKING(没有竞争可能直接RUNNNING)
         * 如果线程没有持有许可，立即获得许可(这回导致下次请求许可会立即返回)
         */
        LockSupport.unpark(thread);
        LockSupport.park();
        System.out.println("ok");

        /**
         * 底层原理:
         * LockSupport基于JVM的内置对象:Parker(每个java线程均有一个Parker对象)
         *
         * Parker{
         *    private:
         *    volatile int _counter ;
         *    Parker * FreeNext ;
         *    JavaThread * AssociatedWith ; // Current association
         * }
         *
         * 当调用park()方法时:
         * if (Atomic::xchg(0, &_counter) > 0) return;  //counter>0，使用xchg指令置为0，然后直接返回
         * 等待前检查java线程是否是中断，如果中断直接返回
         * 最后等待nanos，返回(期间会不断检查_counter,也会响应interrupt)
         *
         * 当调用unpark()方法时:
         * 将_counter置位1
         * 检查之前的_counter值，如果小于1,则调用pthread_cond_signal唤醒在park中等待的线程
         * 如果等于1，pthread_mutex_unlock(_mutex)
         *
         * 可以得到如下结果:
         * 多次park()需要多次unpark()去唤醒(每次park()必须等待unpark())
         * 多次unpark()也只会响应一次park()
         * unpark()即使发生在park()之前，也会生效(导致下一次park()不会WAITING)
         */

        LockSupport.unpark( Thread.currentThread());
        LockSupport.park();
        System.out.println("ok");
    }

    @Test
    public void test_question_3() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        int capacity = 4;

        Runnable producer = () -> {
            while (true) {
                try {
                    lock.lock();
                    if (count < capacity) {
                        System.out.println("当前库存小于" + capacity + ",正在生产，库存为:" + ++count);
                        condition.signal();
                    } else {
                        System.out.println("当前库存达到上限:" + capacity + ",等待消费");
                        condition.await();
                    }
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable consumer = () -> {
            while (true) {
                try {
                    lock.lock();
                    if (count > 0) {
                        System.out.println("当前库存大于" + 0 + ",正在消费，库存为:" + --count);
                        condition.signal();
                    } else {
                        System.out.println("当前库存为0,等待生产");
                        condition.await();
                    }
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();
        Thread.sleep(100);
        System.out.println("模拟结束");
    }
}
