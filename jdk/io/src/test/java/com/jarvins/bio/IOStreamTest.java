package com.jarvins.bio;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * Question List:
 * 1,正常单字节读取对比加缓存读取速度?
 * 2,扩大缓存读取速度?
 * 3,使用BufferStream对比使用缓存速度?
 * 4,增加Buffer大小对BufferStream效率的提升?对使用缓存的提升?BufferStream再加缓存呢?
 * 5,IO流乱码分析?
 */
public class IOStreamTest {

    //读取文本
    private static final String READ_PATH = "/Users/yangyang1/我的文档/Spring实战（第4版）.pdf";

    //输出地址
    private static final String WRITE_PATH = "/Users/yangyang1/我的文档/text.pdf";

    private static int len = 0;

    static {
        File file = new File(WRITE_PATH);
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test_question_1() throws Exception {
        /**
         * 普通的read()方法只是读取了一个字符到内存中，然后写到文本中，对于一个1kb的文件，会发生1024次读io操作和1024次写io操作
         * io操作磁盘都是内核程序才能访问的，所以这里会发生大量的os调用，非常浪费时间
         */
        byteReadAndWrite();
        arrayReadAndWrite(1024);
    }

    @Test
    public void test_question_2() throws Exception {
        /**
         * 增加缓存空间同样会增加os每次读取的数量，降低调用os的次数，提高效率
         */
        arrayReadAndWrite(1024);
        arrayReadAndWrite(8192);
    }

    @Test
    public void test_question_3() throws Exception {
        /**
         * BufferedInputStream会一次性通过os读取byte[]的数据到内存(默认8k，可自定义)
         * BufferedOutputStream.write(len)会先将读到的数据存入Buffer中，当Buffer满了之后一次性将Buffer写入文本中
         * @see BufferedOutputStream#write(int)
         * 理论上当BufferStream和使用byte[]一样大的时候,耗时是基本一致的，但由于BufferStream做了同步控制，效率比byte[]低
         * 这也直接导致了增加Buffer空间，效率提升也很小
         */
        onlyBufferReadAndWrite(1024);
        arrayReadAndWrite(1024);
    }

    @Test
    public void test_question_4() throws Exception {
        /**
         * 提升很小
         * 性能瓶颈在同步消耗
         */
        onlyBufferReadAndWrite(1024);
        onlyBufferReadAndWrite(8192);

        /**
         * 提升较大
         */
        arrayReadAndWrite(1024);
        arrayReadAndWrite(8192);

        /**
         * 基本和使用缓存一致
         */
        bufferAndArrayReadAndWrite(1024);
        bufferAndArrayReadAndWrite(8192);
    }


    @Test
    public void test_question_5() throws Exception {
        /**
         * 对于文本文件:
         * 在utf-8编码下的字节码是固定的,假设是A
         * 在gbk编码下的字节码也是固定的,假设B
         *
         * 当文本本身是gbk,读取出来的就是A字节数组,当它写入utf-8编码的文本时,输出到该文本是gbk编码,这时os会按utf-8的编码集进行解码,并存储
         * 但是: 此时编码并不是对应的gbk的编码集，所以解码失败乱码，因此,需要将该字节流转换成对应utf-8的编码字节流,才不会乱码
         * 因此字节流在处理文本时不方便转码,如果采用字符流，就可以轻松的实现编码转换,避免编码问题导致的乱码
         *
         *
         * 对于pdf:
         * 如下面的测试方法,试图用utf-8编码读取pdf文件，然后以utf-8编码写文件,看上去正确，但是会引起乱码,原因是:
         * 编码集只对文本文件有意义，pdf是非文本文件,当用utf-8进行编码转换后,该pdf文件的二进制文件流已经经过utf-8转换，最终成为了无法识别的文件
         *
         *
         * 所以:
         * 对文本文件的IO,考虑用字符流并设置编码
         * 对非文本文件的IO,使用字节流并不做任何编码转换
         */

        //这里是乱码
        encodedReadAndWriter(8192, StandardCharsets.UTF_8,StandardCharsets.UTF_8);
    }



    //单字节读取
    private void byteReadAndWrite() throws Exception {
        try (InputStream in = new FileInputStream(READ_PATH);
             OutputStream out = new FileOutputStream(WRITE_PATH)) {
            long start = System.currentTimeMillis();
            while ((len = in.read()) != -1) {
                out.write(len);
            }
            System.out.println("单字节读取耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    //加缓存
    private void arrayReadAndWrite(int size) throws Exception {
        try (InputStream in = new FileInputStream(READ_PATH);
             OutputStream out = new FileOutputStream(WRITE_PATH);
        ) {
            long start = System.currentTimeMillis();
            byte[] buffer = new byte[size];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            System.out.println("使用缓存:" + size + ",耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    //仅仅使用BufferStream
    private void onlyBufferReadAndWrite(int size) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(READ_PATH), size);
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(WRITE_PATH), size)
        ) {
            long start = System.currentTimeMillis();
            while ((len = in.read()) != -1) {
                out.write(len);
            }
            System.out.println("buffer缓存:" + size + ",耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    //使用BufferStream和array
    private void bufferAndArrayReadAndWrite(int size) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(READ_PATH), size);
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(WRITE_PATH), size)
        ) {
            long start = System.currentTimeMillis();
            byte[] buffer = new byte[size];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            System.out.println("buffer缓存:" + size + ",使用缓存:" + size + ",耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }


    //字符流编码
    private void encodedReadAndWriter(int size, Charset in, Charset out) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(READ_PATH), in);
             Writer writer = new OutputStreamWriter(new FileOutputStream(WRITE_PATH), out);
        ) {
            long start = System.currentTimeMillis();
            char[] buffer = new char[size];
            while ((len = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, len);
            }
            System.out.println("buffer缓存:" + size + ",使用缓存:" + size + ",耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
