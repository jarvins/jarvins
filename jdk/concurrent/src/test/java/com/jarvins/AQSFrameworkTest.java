package com.jarvins;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.*;

/**
 * Question List:
 * 1,AQS是什么?它如何规范起整个同步组件的内在行为?
 * 2,AQS如何实现同步的,其框架的整体实现原理是什么?
 * 3,AQS的对条件变量的支持?
 */
public class AQSFrameworkTest {

    @Test
    public void test_question_1() {
        /**
         * AQS：AbstractQueuedSynchronizer
         * 队列同步器，基于乐观锁构建同步组件的基础框架,注意它只是一个骨架方法，
         * 它规范了基于乐观锁实现同步的组件的内在行为，但是对于具体的行为差异(公平，可重入)未规范
         *
         * AQS主要的工作:
         * 1,维护同步状态
         * 2,等待队列的维护与管理
         * 3,线程的阻塞与唤醒
         *
         * 基于AQS骨架，一共实现了3种锁:
         * 独占锁/共享锁(AQS规范的2种锁):一次只能被一个线程所持有为独占锁，否则为共享锁
         * 公平锁/非公平锁(基于AQS的自定义实现):多个线程按照申请顺序获得锁为公平锁，否则为非公平锁
         * 可重入锁/不可重入锁(基于AQS的自定义实现): 允许一个线程同时多次获取同一把锁为可重入锁，否则为不可重入锁
         *
         * @see java.util.concurrent.locks.AbstractQueuedSynchronizer
         * @see SynchronizedTest java内置悲观锁
         * @see LockTest 乐观锁总览
         * @see LockSupportTest 乐观锁同步通信机制
         *
         * AQS本质上是一个先进先出(FIFO)的双向队列,队列中的成员为Node,其结构如下:
         *
         *                              ┌──────────────────────────────AQS QUEUE───────────────────────────────────┐
         *                              │                                                                          │
         *      ┌───────────┐     prev  │  ┌───────────┐     prev     ┌───────────┐     prev     ┌───────────┐     │
         *      │   Head    │  ─────────┼→ │   Node    │  ──────────→ │   Node    │  ──────────→ │   Tail    │     │
         *      │           │  ←────────┼─ │           │  ←────────── │           │  ←────────── │           │     │
         *      └───────────┘     next  │  └───────────┘     next     └───────────┘     next     └───────────┘     │
         *                              │                                                                          │
         *                              └──────────────────────────────────────────────────────────────────────────┘
         *
         * Node{
         * volatile int waitStatus;   //描述了当前节点的后继节点的状态
         * Node next;   //后置节点
         * Node prev;   //前置节点
         * Threead thread;  //当前线程
         * Node nextWaiter;  //
         * }
         *
         * waitStatus的4种状态:
         *      CANCELLED = 1,取消状态
         *      SIGNAL = -1,等待触发状态
         *      CONDITION = -2,等待条件状态
         *      PROPAGATE = -3,状态需要向后传播
         *
         * state: 同步器的状态,线程同步的关键是对state值的操作，某个线程CAS修改state成功，则可以认为当前线程持有锁
         *
         * 状态变更:
         * @see AbstractQueuedSynchronizer#getState() 获取当前同步器的状态值
         * @see AbstractQueuedSynchronizer#setState(int) 设置当前同步器的状态值
         * @see AbstractQueuedSynchronizer#compareAndSetState(int, int) CAS修改状态值
         *
         * 独占锁获取/释放:
         * @see AbstractQueuedSynchronizer#tryAcquire(int)
         * @see AbstractQueuedSynchronizer#tryAcquireNanos(int, long)
         * @see AbstractQueuedSynchronizer#acquire(int)
         * @see AbstractQueuedSynchronizer#tryRelease(int)
         * @see AbstractQueuedSynchronizer#release(int)
         *
         * 共享锁获取/释放:
         * @see AbstractQueuedSynchronizer#tryAcquireShared(int)
         * @see AbstractQueuedSynchronizer#tryAcquireSharedNanos(int, long)
         * @see AbstractQueuedSynchronizer#acquireShared(int)
         * @see AbstractQueuedSynchronizer#tryReleaseShared(int)
         * @see AbstractQueuedSynchronizer#releaseShared(int)
         *
         * //调用此方法的线程是否是独占锁的持有者
         * @see AbstractQueuedSynchronizer#isHeldExclusively()
         *
         * 基于以上3组方法，规范了整个AQS同步器的内在行为
         * 其中只有tryAcquire(),tryRelease(),tryAcquireShared(),tryReleaseShared()为骨架实现(不是抽象实现)，交由其子类自定义实现
         * 其他方法均由AQS完全定义，因此AQS的所有实现都保持了高度的一致性，仅依靠自身独立方法表现出特定的差异性
         * 基于完整的AQS体系，实现的Lock接口可简单的依赖lock(),unlock()实现同步控制，最终为所有线程安全的实现提供了同步保障
         * @see ReentrantLock
         * @see java.util.concurrent.ArrayBlockingQueue
         * @see ExecutorsTest
         *
         */

    }

    @Test
    public void test_question_2() {
        /**
         * 现在在如下场景分析:
         * 线程A获得了锁，在执行中(执行耗时很长),线程B,线程C获取锁失败
         *
         *
         * 独占锁模式下:
         * 当A线程获取锁成功:
         * @see AbstractQueuedSynchronizer#acquire(int)
         * A线程tryAcquire()成功(修改state状态值)，并且设置当前获取锁的线程是当前线程({@link AbstractQueuedSynchronizer#setExclusiveOwnerThread(Thread)},由于只有一个线程能CAS成功，所以只有一个线程能完成这个操作)，直接返回，表示A线程获得了锁，不会阻塞或者等待，执行A线程的后续流程
         *
         * B线程tryAcquire()失败，将当前线程封装成Node.EXCLUSIVE的Node节点插入AQS队列尾部{@link AbstractQueuedSynchronizer#addWaiter(AbstractQueuedSynchronizer.Node)}(竞争修改，不断自旋直到成功，enq()方法中可以看出head实际上只是个空节点)
         * 然后将当前线程置于WAITING状态{@link AbstractQueuedSynchronizer#acquireQueued(AbstractQueuedSynchronizer.Node, int)}(这里有个小优化，如果发现前置节点是head,就再尝试获取一次锁[说明当前线程现在是很有可能拿到锁的,如果这里不做一次尝试，可能会出现head释放,B线程addNode还未完成,head不会发出信号，等到B节点添加完成后，永远等不到唤醒信号]，成功了直接退出),
         * 在进入WAITING状态前做一次检查操作({@link AbstractQueuedSynchronizer#shouldParkAfterFailedAcquire(AbstractQueuedSynchronizer.Node, AbstractQueuedSynchronizer.Node)}),
         * 目的是将AQS队列的前驱CANCELED状态的节点干掉，并设置当前节点的前驱节点转态为SINGAL(由于是无限循环，第二次进来时检查前驱结点状态是SINGAL,直接返回true),最后进入WAITING状态{@link AbstractQueuedSynchronizer#parkAndCheckInterrupt()}
         *
         * C线程也会如此操作被封装成节点放入队列进入WAITING状态
         * 此时是这样：
         *          head  <===>   ThreadB   <===>    ThreadC
         *         SIGNAL           0                   0
         *
         * 当A线程释放锁:
         * @see AbstractQueuedSynchronizer#release(int)
         * A线程tryRelaease(),检查自己是否是独占锁的拥有者{@code Thread.currentThread() == getExclusiveOwnerThread()}。不是则抛出IllegalMonitorStateException,是则设置exclusiveOwnerThread = null(这里显然不需要做并发控制，只有一个线程能通过if测试)，最后设置state值
         * 然后修改头节点的waitStatus(因为即将唤醒后继节点，当前节点的waitStauts不应该是SIGNAL),查找AQS队列中的下一个线程，唤醒该节点(在这里，这个节点就是线程B,{@link AbstractQueuedSynchronizer#unparkSuccessor(AbstractQueuedSynchronizer.Node)})
         *
         *
         * 这里有2个细节问题:
         *
         * 1,release()释放时有如下代码:
         * {@code
         *  if (tryRelease(arg)) {
         *    Node h = head;
         *    if (h != null && h.waitStatus != 0)
         *       unparkSuccessor(h);
         *     return true;
         *    }
         *    return false;
         *     }
         * }
         * 这里只有释放后发现 h.waitStatus!=0 才会唤醒下一个线程(这里的含义是，完成释放动作检查是否后继节点处于等待signal状态，有才唤醒，没有就不需要)
         * 而signal是WAITING的线程设置的，最开始head = new Node();此时waitStatus = 0,B线程马上要被WAITING,因此它要设置head.waitStauts = -1，设置完成后才会调用LockSupport.park();
         * 既然检查到head.waitStauts == 0，说明B线程还没进入等待，自然不需要唤醒它
         * 而当B线程完成设置后重新进入循环(此时B的waitStatus = -1)，第二次尝试获得锁时一定会成功(FIFO,此时C线程的前驱节点是B,它没有资格去tryAcquire())
         * B线程获得许可，退出WAITING状态(此时依旧在for(;;)中)，此时B线程tryAcquire()成功，设置自己为head节点，此时C节点处于WAITING状态
         * 等待B线程执行完,由于B.waitStatus = -1(同理，这个状态是C在park之前修改的，如果没修改成-1,B就释放锁，则证明此时C没有进入park状态)，它会唤醒C线程
         *
         * 2,unparkSuccessor()方法是倒序寻找非cancel节点并唤醒
         * 当head.next节点的状态是CANCEL时，是从tail向前遍历找到非CANCEL节点的
         * 因为cancel会从AQS队列中移除当前节点(修改next节点，但不修改prev节点)，如果正向遍历，遍历到cancel节点，可能next节点是null导致遍历失败
         * 而如果倒序遍历，prev节点是不会导致中断的
         *
         *
         * 共享锁模式下(这里假设只允许1个线程共享,实际可以指定可共享的数量):
         *
         * 当A获得锁成功:
         * @see AbstractQueuedSynchronizer#acquireShared(int)
         * A线程tryAcquireShared()成功，直接返回
         * B线程,C线程失败，进入AQS队列{@link AbstractQueuedSynchronizer#doAcquireShared(int)},这里节点被封装成了Node.SHARED插入AQS队列尾部(这里逻辑和独占锁逻辑一致)
         * 这里有个和独占锁不同的地方:
         * 当B线程(head.next)在AQS队列中尝试获取锁成功时，并不是直接返回，而是检查当前可用共享锁数量(返回值 = 0表示当前是最后一把共享锁， > 0 则表示当前仍有空闲的共享锁)
         * 如果发现仍有空闲共享锁，则需要对共享锁实时传播(这是独占锁没有的过程):
         * @see AbstractQueuedSynchronizer#setHeadAndPropagate(AbstractQueuedSynchronizer.Node, int)
         * 如果是允许多线程共享，那么B线程唤醒时就必须唤醒C线程(此时链式效应，C会唤醒D,D唤醒E...直到没有可用的共享锁)而不能交由release时唤醒后继线程,对比独占锁思路，可能出现下面问题:
         * 第一个线程释放锁,第一个WAITING的线程被唤醒，准备设置head为自己，但此时第二个线程释放锁，此时head仍是原head，waitStatus = 0,
         * 不唤醒后继线程,而第一个WAITING的线程获得锁时发现这是最后一把锁，也不会唤醒后继节点,但此时2个共享位置就只能被获取一个，出现共享问题。
         *         Thread_1        Thread_2                 WAITING_1          WAITING_2
         *           │                 │                        │                  │
         *           ↓                 │                        │                  │
         *         release             │                        │                  │
         *           ↓                 ↓                        │                  │
         *  waitStauts SIGNAL=>0    release                     │                  │
         *           ↓                 ↓                        │                  │
         *   unpark(WAITING_1)   waitStatus = 0                 ↓                  │
         *           ↓                 ↓                 tryAcquire() = 0          │
         *          end               end                       ↓                  │
         *                                                  setHead()              │
         *                                                      │                  │
         *                                                      ↓                  │
         *                                                   release               │
         *                                                      │                  │
         *                                                      ↓                  │
         *                                                     end                 ↓ //此时只有在THREAD_1释放锁才能唤醒THREAD_2
         *                                                                    tryacQuire()
         *
         * 当A线程释放锁时:
         * @see AbstractQueuedSynchronizer#releaseShared(int)
         * A释放锁成功，这里需要保证信号向后传递
         * @see AbstractQueuedSynchronizer#doReleaseShared()
         * 如果head节点是SIGNAL，则需要唤醒下一个节点，并设置当前节点的waitStatus = 0
         * 如果head节点waitStatus = 0，则需要标识当前节点为PROPAGATE状态
         * 完成释放的条件是: 节点状态修改成功，且当前修改的节点是当前AQS队列的头结点
         *
         * 这里可能存在多个线程同时CAS但不出现竞争，即waitStatue变化如下: SIGNAL -> 0(唤醒后继节点) -> PROPAGATE,如果允许共享锁可被共享n次:
         * 若n = 1，则同时只有一个线程占有，不会变成PROPAGATE
         * 若n > 1. 则同时可以有n个线程占有，一个线程释放则变成0，多个线程释放则变成PROPAGATE，用于信号向后传播
         */
    }

    @Test
    public void test_question_3() throws InterruptedException {
        /**
         * 首先明确一个问题:
         * 独占锁涉及资源竞争问题，需要条件变量控制时必须使用锁，因此独占锁有条件变量的实现
         * 共享锁不涉及资源竞争问题，使用条件变量时不需要锁，共享锁没有条件变量的实现(共享锁不需要支持条件变量)
         *
         *
         * ConditionObject(独占锁的条件变量实现)是AQS内部的条件变量对象，其实现Condition interface,用于完成线程间通信
         * Condition.await()/Condition.sign()和Object.wait()/Object.notify()一样需要获取锁才能调用
         * 原因不一样:Object的通信机制是依靠monitor对象实现的，而AQS则依赖于LockSupport，其await()方法需要释放锁，而其sign()之前需要检测当前线程是否是锁持有者
         *
         * AQS队列维护的未获得锁的线程
         * Condition队列维护的是等待信号的线程(由于await()会释放锁，所以获取锁之后Condition队列元素会进入AQS队列)
         *
         * @see java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject#await()
         * 先入条件队列队尾(入队时先移除CANCEL节点，同时当前节点定义为CONDITION){@link AbstractQueuedSynchronizer.ConditionObject#addConditionWaiter()}
         * 释放当前节点的锁(考虑到独占锁可以重入，这里进行一次性释放)，并返回释放前的state{@link java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject#fullyRelease(AbstractQueuedSynchronizer.Node)}
         * 然后判断当前节点是否在AQS队列中，如果不在队列中才将当前线程park，等待singal信号
         * 待接收到信号后，线程被唤醒
         * 线程被唤醒后已经在同步队列了(退出while循环)，检查是否被中断(因为park响应中断，但不抛出异常，这里要明确是收到singal信号恢复的，还是被中断恢复的)
         * 进入正常的AQS等待状态(由于已经入队，只需要执行挂起，锁获取，恢复重入的操作{@link AbstractQueuedSynchronizer#acquireQueued(AbstractQueuedSynchronizer.Node, int)})
         *
         *
         * @see AbstractQueuedSynchronizer.ConditionObject#signal()
         * 先检查当前线程是否是锁的持有者，不是直接抛出异常
         * 然后查找条件队列的队头元素，对该线程发出signal信号，直到成功为止(期间先将节点入队AQS，并设置waitStatus = SIGNAL,最后unpark,保证唤醒后await()检查节点一定在AQS队列中)
         * 对于signalAll而言，即对当前队列节点发出信号直到队列没有元素为止
         *
         */

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                lock.lock();
                condition.signal();
                lock.unlock();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
        lock.lock();
        //此时主线程会释放锁，让子线程获取到锁,并发出唤醒信号
        condition.await();
        System.out.println("收到信号");
        lock.unlock();
    }
}
