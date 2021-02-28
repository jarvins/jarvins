package com.jarvins.collection.queue;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Question List:
 * 1,什么是阻塞队列，阻塞队列接口是做什么的？
 * 2,阻塞队列相对于队列提供了哪些增强工功能？
 * 3,ArrayBlockingQueue特点和原理？
 * 4,,LinkedBlockingQueue特点和原理？
 * 5.PriorityBlockingQueue特点和原理？
 *
 */
public class BlockingQueueTest {


    @Test
    public  void test_question_1(){
        /**
         * 阻塞队列是队列的阻塞形式，支持阻塞方法，继承了Queue接口，增强了Queue的功能
         *
         * 由于阻塞队列提供了阻塞方法，因此适合生产者消费者模型：
         * 生产者线程会一直不断的往阻塞队列中放入数据，直到队列满了为止。队列满了后，生产者线程阻塞等待消费者线程取出数据
         * 消费者线程会一直不断的从阻塞队列中取出数据，直到队列空了为止。队列空了后，消费者线程阻塞等待生产者线程放入数据
         */
    }

    @Test
    public void test_question_2(){
        /**
         * @see QueueTest#test_question_1()
         * 插入操作:
         * put(): 插入数据，如果队列已满，则阻塞等待，直到空间可用，如果期间被中断，抛出InterruptedException
         * offer(E e, long timeout, TimeUnit unit): 添加了最大等待时间,超时返回false
         *
         * 删除操作:
         * take(): 查询删除并返回队头元素，如果队列为空则一直阻塞，直到可返回元素，抛出InterruptedException
         * poll( long timeout, TimeUnit unit)): 添加了最大等待时间，超时返回false
         *
         */
    }

    @Test
    public  void test_question_3(){
        /**
         *@see  ArrayBlockingQueue
         * 底层使用数组当队列
         * 使用ReentrantLock保证同步，默认采用非公平锁(高效)
         * 生产者Condition : lock.newCondition()
         * 消费者Condition: lock.newCondition()
         *
         */

         BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        ExecutorService service = Executors.newFixedThreadPool(4);
        Runnable consumer = () -> {
            while (true) {
                if (queue.poll() != null) {
                    System.out.printf("线程%s消费了，剩余%s%n", Thread.currentThread().getName(), queue.size());
                }
            }
        };
        Runnable producer = () -> {
            while (true) {
                if (queue.offer(1)) {
                    System.out.printf("线程%s生产了，剩余%s%n", Thread.currentThread().getName(), queue.size());
                }
            }
        };
        service.execute(producer);
        service.execute(consumer);
        service.shutdown();
    }

    @Test
    public  void test_question_4(){
        /**
         * @see java.util.concurrent.LinkedBlockingQueue
         * 底层使用数链表，因此可以实现无界队列，但也支持有界队列
         * 使用ReentrantLock保证同步，默认采用非公平锁(高效)
         * 生产者Condition : putLock.newCondition()
         * 消费者Condition: takeLock.newCondition()
         *
         * 生产者和消费者分别上锁，可以实现真正的并行，提高存取效率
         *
         * 为什么ArrayBlockedQueue采用单锁，而LinkedBlockedQueue采用双锁？
         * 猜测:
         * Array底层数组实现，本身比较简单，而Linked底层需要封装成Node节点，维护的额外费用高，采用双锁并行提高效率
         * Array同样可以双锁实现，但是可能性能的提升并不客观，但提高的复杂性
         *
         */
    }

    @Test
    public void test_question_5(){
        /**
         * @see java.util.concurrent.PriorityBlockingQueue
         * 优先级队列，无界
         * 由数组实现最小堆
         */

        //todo 为什么是0987654321
        class TestClass implements Comparable<TestClass>{
            private int i;

            public TestClass(int i) {
                this.i = i;
            }

            @Override
            public int compareTo(TestClass o) {
                return 0;
            }
        }

        BlockingQueue<TestClass> queue = new PriorityBlockingQueue<>();
            for(int i = 0; i < 10; i++){
                queue.add(new TestClass(i));
            }
            while (!queue.isEmpty()){
                System.out.print(queue.poll().i);
            }
    }
}
