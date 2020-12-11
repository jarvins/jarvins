package com.jarvins.collection.list;


import java.util.List;

@Deprecated()
/**
 * 一个被弃用的线程安全类，没有理由再使用它，
 * 不需要同步则使用ArrayList
 * 需要同步则使用CopyOnWriteArrayList,或者Collections.synchronizedList()
 * @see java.util.concurrent.CopyOnWriteArrayList
 * @see java.util.Collections#synchronizedList(List)
 */
public class VectorTest {

    /*
    关于Vector和Collections.synchronizedList():
    Vectior底层和ArrayList区别不大，但为了保证线程安全，它直接在方法上加同步锁，导致锁粒度比较大，并发性能并不高
    Collections.synchronizedList()则是加对象锁(这里需要注意其并未对遍历操作加锁，遍历也必须对自己/外部传入对象加锁，保证锁的是同一个对象)

    本质上说二者的性能差距不大，但Collections.synchronizedList()支持各种list的拓展,拓展性较强
     */

}
