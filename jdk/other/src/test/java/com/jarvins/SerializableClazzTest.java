package com.jarvins;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
public class SerializableClazzTest implements Serializable{

    //序列化id保证了序列化和反序列化的成功(即序列化的UID和反序列化的ID必须是一个才能成功)
    private static final long serialVersionUID = 1024L;

    private static final String PATH = "/Users/yangyang1/testSerializable.txt";

    public static final SerializableClazzTest clazz = new SerializableClazzTest("pi",8,3.1415926,LocalDate.of(-429, 1, 1));

    //transient关键字不会在序列化保留，前提是未实现readResolve()方法
    String stringValue;
    Integer intValue;
    Double doubleValue;
    LocalDate localDate;

    private static String staticStringValue = "default value";

    public SerializableClazzTest(){}

    private SerializableClazzTest(String stringValue, Integer intValue, Double doubleValue, LocalDate localDate) {
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.doubleValue = doubleValue;
        this.localDate = localDate;
    }

    /**
     * 这里参照ArrayList的序列化和反序列化过程:
     * 为了保证数组只存放少量对象，但是数组容量太大，导致序列化多个null对象，元素被transient修饰
     * 为了保证元素能正确被序列化(因为元素都被transient修饰了，所以常规的default会导致所有元素都不被序列化)，手动实现了readObject,writeObject，即自定义序列化方法
     * 自定义方法是在invokeWriteObject(invokeReadObject)通过反射调用的
     */

    //默认调用的是ObjectInputStream.defaultReadObject()方法，但如果实现此方法，会调用此方法
//    private void readObject(ObjectInputStream in){
//    }

    //默认调用的是ObjectOutputStream.defaultWriteObject()方法，但如果实现此方法，会调用此方法
//    private void writeObject(ObjectOutputStream out){
//    }

    private SerializableClazzTest serializableAndRead(){
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PATH));
             ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PATH))) {
            outputStream.writeObject(clazz);
            return (SerializableClazzTest)inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //此方法会保证单例，其会改变反序列化的方式(正常是通过反射构造新对象返回的，此方法给与了反序列化的新方案)
//    private Object readResolve(){
//        return clazz;
//    }

    @Test
    public void readWriteObj() {
        SerializableClazzTest obj = Objects.requireNonNull(serializableAndRead());
        //输出初始对象
        System.out.println(clazz);
        //输出反序列化结果
        System.out.println(obj);
        //反序列化的非单例
        System.out.println("是否单例?" + (clazz == obj));
    }

    //静态变量不参与序列化，静态变量不属于对象属性，而是类的状态属性
    @Test
    public void staticSerializable(){
        SerializableClazzTest obj = Objects.requireNonNull(serializableAndRead());
        //修改staticStringValue值
        staticStringValue = "after modify";

        //这里编译器都提示直接使用staticStringValue
        System.out.println(obj.staticStringValue);
    }

}
