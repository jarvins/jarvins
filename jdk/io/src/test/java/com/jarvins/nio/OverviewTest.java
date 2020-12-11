package com.jarvins.nio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Question List:
 * 1,NIO与BIO有什么区别?有什么相同的地方?
 * 2,NIO的几个核心组件和其功能?
 * 3,使用NIO实现一个文件读写?
 */
public class OverviewTest {

    //读取文本
    private static final String READ_PATH = "/Users/yangyang1/我的文档/Spring实战（第4版）.pdf";

    //输出地址
    private static final String WRITE_PATH = "/Users/yangyang1/我的文档/text.pdf";

    @Test
    public void test_question_1() {
        /**
         * 相同点:
         * NIO与BIO都是同步到的,而非异步
         *
         * 不同点:
         * BIO面向流，NIO面向缓冲区
         * BIO是阻塞的，而NIO是非阻塞的(这点在网络IO中会体现出来,文件IO不支持非阻塞模式)
         * BIO是一次一个字节的读取数据(调用os消耗时间和资源，后面用NIO优化过),NIO是面向缓冲区以块的形式读取数据
         *
         */
    }

    @Test
    public void test_question_2() {
        /**
         * NIO有3大组件:
         *
         * Buffer
         * 缓冲区用于读写数据,其有4个核心属性:
         * capacity: 容量(描述了缓冲去能容纳的数据的最大数量，一旦被设置无法修改，因为底层是数组)
         * limit: 上界(代表了缓冲区里面一共有多少数据量)
         * position: 位置(下一个要被读或者写的位置,会更具put()和get()函数自动更新)
         * mark: 标记(记录上一次读写的位置,用于读写切换)
         *
         * ByteBuffer buffer = ByteBuffer.allocate(10);
         *          0   0   0   0   0   0   0   0   0   0   0
         *          ↑                                       ↑
         *        position = 0                    capital  = limit = 10
         *
         * buffer.put("hello".getBytes());
         *          h   e   l   l   o   0   0   0   0   0   0
         *                              ↑                   ↑
         *                           position = 5   capital  = limit = 10
         *
         * buffer.flip();  //反转流，变成读buffer
         *          h   e   l   l   o   0   0   0   0   0   0
         *          ↑                   ↑                   ↑
         *         position = 0     limit = 5          capital = 10
         *
         * byte a = buffer.get();
         * byte b = buffer.get();
         *          h   e   l   l   o   0   0   0   0   0   0
         *                  ↑           ↑                   ↑
         *            position = 2   limit = 5          capital = 10
         *
         *
         * 关于Direct Buffer和Heap Buffer:
         * Direct Buffer
         * 直接分配在系统内存中,不需要花费将数据从内存拷贝到java内存的成本.
         * 由于其直接分配在系统内存中，不受GC管理，不会自动回收
         * Heap Buffer
         * 分配在Java Heap中，受GC管理生命周期,不需要额外的维护,但是需要将数据做拷贝
         *
         *
         * Channel
         * 数据通道，为双向数据通道，即既能向里面写数据，又能从里面读数据,对比BIO的InputStream和OutputStream都是单向数据通道
         * 它帮助系统以较小的代价保持数据传输的处理，但它并真正存放数据，他需要结合缓冲区
         * 分为2类:
         * FileChannel(通过FileInputStream,FileOutputStream和RandomAccessFile获得,不支持非阻塞模式)
         * SelectableChannel(通过Socket获得，支持非阻塞模式)
         *
         *
         * Selector(针对网络IO,NIO最大的亮点)
         * 对比BIO的网络编程实现:
         * @see com.jarvins.bio.Server
         * @see com.jarvins.bio.Client
         *
         * @see com.jarvins.nio.Server
         * @see com.jarvins.nio.Client
         * 多路复用器,相对于传统IO在接受请求是必须由一个线程去维护socket的连接(如果有1W个用户连接，需要1W个线程，显然是无法支持的)
         * 但在NIO中可以使用Selector统一管理IO请求，而不需要单独为每个请求分配一个线程去管理
         * 并发较大的情况下，一个线程管理不过来可以用多个线程管理，每个管理m个通信请求
         *
         * 每当客户端连接到服务器，selector会记录这个请求，服务端查询请求记录，可以获取到当前请求连接的客户端，
         * 并将该客户端的channel注册到选择器，并添加感兴趣的事件(比如可读),之后selector每次轮询是否有满足事件
         * 的通道可操作，如果有，就可以操作他们
         */
    }

    @Test
    public void test_question_3() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(new File(READ_PATH));
             FileOutputStream outputStream = new FileOutputStream(new File(WRITE_PATH))) {
            FileChannel readChannel = inputStream.getChannel();
            FileChannel writeChannel = outputStream.getChannel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (readChannel.read(allocate) > 0) {
                allocate.flip();
                writeChannel.write(allocate);
                allocate.clear();
            }
        }
    }
}
