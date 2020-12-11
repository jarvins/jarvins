package com.jarvins;

import org.junit.jupiter.api.Test;

/**
 * Question List:
 * Java Thread的6种状态和转换关系图?
 */
public class ThreadModuleTest {

    @Test
    public void test_question_1() {
        /*
        Thead的6种状态:
        NEW     //创建
        RUNNABLE   //运行中
        BLOCKED   //阻塞
        WAITING   //等待
        TIMED_WAITING  //超时等待
        TERMINATED  //执行完毕

                                                                   ┌───────────────────────────────────────────────────────────────────────────────┐
                                                                   │                   thread was selected by thread scheduler to run              │
                                                                   │  Runnable            ┌──────────────────────────────────────┐                 │
                                                                   │                      │                                      ↓                 │
                               ┌───────────┐   thread.start()      │                ┌─────────────┐                      ┌─────────────┐           │   thread terminated    ┌────────────┐
                start  ─────→  │    New    │  ────────────────→    │                │    READY    │                      │   RUNNING   │           │  ───────────────────→  │ TERMINATED │  ────────────→ end
                               └───────────┘                       │                └─────────────┘                      └─────────────┘           │                        └────────────┘
                                                                   │                      ↑          Thread.yield()               │                │←──┐                           ↑
                                                                   │                      └───────────────────────────────────────┘                │   │                           │
                                                                   │                       thread was suspended by thread scheduler                │   │                           │
                                                                   └───────────────────────────────────────────────────────────────────────────────┘   │                           │
                                                                        │                                                                 ↑            │                           │
                                                                        │                                                                 │            │                           │
                                                                        │                           ┌───────────────────────┐             │            │                           │
                                                                        │                           │                       │             │            │                           │
                                                                        │       Thread.sleep()      │                       │             │            │                           │
                                                                        ├─────────────────────────→ │                       │             │            │                           │
                                                                        │    Object.wait(timeout)   │                       │   timeout   │            │                           │
                                                                        ├─────────────────────────→ │                       ├─────────────┘            │                           │
                                                                        │    Thread.join(timeout)   │                       │                          │   Thread Terminated       │
                                                                        ├─────────────────────────→ │     TIMED_WAITING     ├──────────────────────────│───────────────────────────┤
                                                                        │  LockSupport.parkNanos()  │                       │  Object.notifyAll()      │                           │
                                                                        ├─────────────────────────→ │                       ├──────────────────────┐   │                           │
                                                                        │  LockSupport.parkUntil()  │                       │ Condition.signalAll()│   │                           │
                                                                        ├─────────────────────────→ │                       │                      │   │                           │
                                                                        │                           │                       │   Object.notify()    │   │                           │
                                                                        │                           │                       ├──────────────────────┤   │                           │
                                                                        │                           │                       │  Condition.signal()  │   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │                           └───────────────────────┘                      │   │                           │
                                                                        │                                                                          │   │                           │
                                                                        │                           ┌───────────────────────┐                      │   │                           │
                                                                        │                           │                       │  Object.notifyAll()  │   │                           │
                                                                        │                           │                       ├──────────────────────┤   │                           │
                                                                        │                           │                       │ Condition.signalAll()│   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │    Object.wait()          │                       │    Object.notify()   │   │                           │
                                                                        ├─────────────────────────→ │                       ├──────────────────────┤   │                           │
                                                                        │    Thread.join()          │                       │   Condition.signal() │   │                           │
                                                                        ├─────────────────────────→ │         WAITING       │                      │   │                           │
                                                                        │     LockSupport.park()    │                       │                      │   │    Thread Terminated      │
                                                                        ├─────────────────────────→ │                       ├──────────────────────┼───│───────────────────────────┤
                                                                        │                           │                       │                      │   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │                           └───────────────────────┘                      │   │                           │
                                                                        │                                                                          │   │                           │
                                                                        │                           ┌───────────────────────┐                      │   │                           │
                                                                        │                           │                       │                      │   │                           │
                                                                        │ wait for lock to enter    │                       │                      │   │                           │
                                                                        │ synchro block or method   │                       │                      │   │                           │
                                                                        ├─────────────────────────→ │                       │                      │   │                           │
                                                                        │                           │                       │ ←────────────────────┘   │                           │
                                                                        │                           │                       │     lock acquired        │                           │
                                                                        │ wait for lock to enter    │         BLOCKED       │ ─────────────────────────┘                           │
                                                                        │ synchro block or method   │                       │                              Thread Terminated       │
                                                                        └─────────────────────────→ │                       ├──────────────────────────────────────────────────────┘
                                                                                                    │                       │
                                                                                                    │                       │
                                                                                                    │                       │
                                                                                                    │                       │
                                                                                                    └───────────────────────┘
         */
    }
}
