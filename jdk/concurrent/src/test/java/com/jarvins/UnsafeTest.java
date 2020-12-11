package com.jarvins;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Question List:
 * 1,Unsafe类方法主要分为哪几类?方法说明?
 */
public class UnsafeTest {

    private Integer integerValue = 1;
    private long longValue = 0xffffffff;

    @Test
    public void test_question_1() throws NoSuchFieldException {
        /**
         * Unsafe类的方法主要分为以下几类:
         * 堆外内存操作，JVM仅管理堆内存,这仅是内存中开辟的一小块用户内存，对堆外内存的使用，必须依赖Unsafe类
         * CAS操作，其底层依靠CPU的cmpxchg指令，真正的实现原子性变更，在多核CPU环境下(如果发生了并发cmpxchg)，该指令会锁住总线，导致其他cpu空转，保证临界资源的访问问题
         * 线程调度,控制线程的状态
         * Class操作，提供Class和它的静态字段的操作相关方法，包含静态字段内存定位、定义类、定义匿名类、检验&确保初始化
         * 对象操作，主要包含对象成员属性相关操作及非常规的对象实例化方式等相关方法
         * 内存屏障，禁止指令排序
         * 数组操作
         */
        Unsafe unsafe = reflectGetUnsafe();
        assert unsafe != null;


        //堆外内存操作
        //申请了一块内存，并为该内存分配了值'a',然后释放该内存(l为基地址)
        long memoryIndex = unsafe.allocateMemory(1);
        unsafe.putChar(memoryIndex,'a');
        Assertions.assertEquals(unsafe.getChar(memoryIndex),'a');
        unsafe.freeMemory(memoryIndex);


        //CAS操作
        //获取对象成员属性在内存地址相对于此对象的内存地址的偏移量
        long value = unsafe.objectFieldOffset(getClass().getDeclaredField("integerValue"));
        //修改当前对象地址 + 偏移量指向的物理地址的值，如果符合期望，则修改,否则不修改
        unsafe.compareAndSwapObject(this,value,1,5);
        Assertions.assertEquals(5, (int) integerValue);


        //线程挂起和恢复
        System.out.println("线程WAITING: 9s");
        Thread t = Thread.currentThread();
        new Thread(() -> {
            try{
                System.out.println("子线程休息3s,unpark父线程");
                Thread.sleep(3000);
                //如果这里不唤醒主线程，主线程会继续WAITING,直到结束
                unsafe.unpark(t);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
        unsafe.park(false, 9000000000L);


        //对象操作
        long v = unsafe.objectFieldOffset(getClass().getDeclaredField("longValue"));
        unsafe.getAndSetLong(this,v,0x11111111);
        Assertions.assertEquals(longValue,0x11111111);

        //内存屏障
        //禁止load操作重排序。屏障前的load操作不能被重排序到屏障后，屏障后的load操作不能被重排序到屏障前
        unsafe.loadFence();
        //禁止store操作重排序。屏障前的store操作不能被重排序到屏障后，屏障后的store操作不能被重排序到屏障前
        unsafe.storeFence();

        //数组操作(第一个元素的偏移地址 + index * 每个元素的占用大小 = index的偏移地址)
        Integer[] arr = new Integer[4];
        System.out.println("当前数组第一个元素的偏移地址:" + unsafe.arrayBaseOffset(arr.getClass()));
        System.out.println("当前数组每个元素的占用大小:" + unsafe.arrayIndexScale(arr.getClass()));
    }


    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
