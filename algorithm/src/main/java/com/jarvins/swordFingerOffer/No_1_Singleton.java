package com.jarvins.swordFingerOffer;

/**
 * 单例模式，唯一一个可以很少代码可以写出来的设计模式
 * 下面给出几种实现方式，并指出优缺点
 */

public class No_1_Singleton {

}

final class Singleton {

    private static Singleton instates = null;

    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    /**
     * 构造器检查静态成员
     * 优点: 静态属性，由于静态属性类共享，只会被初始化一次
     * 缺点: 但是多线程下会失效
     *
     * @return instance
     */
    public static Singleton Instance1() {
        if (instates == null) {
            instates = new Singleton();
        }
        return instates;
    }

    /**
     * 加锁
     * 优点: 线程安全的方式
     * 缺点: 效率低下，每次都是同步
     */
    public static synchronized Singleton Instance2() {
        if (instates == null) {
            instates = new Singleton();
        }
        return instates;
    }

    /**
     * 双重检查锁优化
     * 优点: 同步效率上升
     * 缺点: 实现复杂
     */
    public static Singleton Instance3() {
        if (instates == null) {
            synchronized (Singleton.class) {
                if (instates == null) {
                    instates = new Singleton();
                }
            }
        }
        return instates;
    }

    /**
     * 常量
     * 优点：简单，多线程有效
     * 缺点：无法抵挡反射攻击
     * <p>
     * 改进方案:
     * //无法抵抗序列化攻击
     *
     * @see com.jarvins.SerializableClazzTest
     * private Singleton() {
     * if( INSTANCE != null){
     * throw new AssertionError();
     * }
     * }
     */
    public static Singleton Instance4() {
        return INSTANCE;
    }

}


/**
 * 枚举
 * 优点: 最佳方案(不会受到序列化问题，不会被反射攻击，多线程安全)
 * 缺点: 不能继承，不能拓展
 */
enum SingletonEnum {
    INSTANCE;

    public void doSomeThing() {
        // do something...
    }
}
