package com.jarvins.nio;

import com.jarvins.Message;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class Client extends Thread {

    private final static CountDownLatch latch = new CountDownLatch(2);
    private final static SocketAddress ADDRESS = new InetSocketAddress(10086);
    private static boolean out = false;
    private static SocketChannel socketChannel;

    static {
        try {
            socketChannel = SocketChannel.open(ADDRESS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Getter
    private String user;

    public Client(String user) {
        this.user = user;
    }

    @SneakyThrows
    @Override
    public void run() {
        new Sender(socketChannel, user).start();
        new Receiver(socketChannel).start();
        latch.await();
        socketChannel.close();
        System.out.println("已退出聊天室");
    }

    private static class Sender extends Thread {
        private final static ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel;
        String user;

        public Sender(SocketChannel socketChannel, String user) {
            this.socketChannel = socketChannel;
            this.user = user;
        }

        @SneakyThrows
        @Override
        public void run() {
            Message register = Message.enterMessage(user);
            buffer.put(Message.serialize(register).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            Scanner scanner = new Scanner(System.in);
            while (socketChannel.isOpen()) {
                System.out.println("输入信息发送方昵称(all表示群发，quit表示退出聊天室):");
                String position = scanner.next();
                if (position.equals("quit")) {
                    Message quitMessage = Message.quitMessage(user);
                    buffer.put(Message.serialize(quitMessage).getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                    out = true;
                    buffer.clear();
                    break;
                } else {
                    System.out.println("输入发送内容:");
                    String msg = scanner.next();
                    Message message = Message.sendMessage(user, position, msg);
                    buffer.put(Message.serialize(message).getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                }
            }
            latch.countDown();
        }
    }

    private static class Receiver extends Thread {
        private final SocketChannel socketChannel;
        private final static ByteBuffer buffer = ByteBuffer.allocate(1024);

        public Receiver(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @SneakyThrows
        @Override
        public void run() {
            while (!out) {
                int len;
                while ((len = socketChannel.read(buffer)) != -1) {
                    Message message = Message.deSerialize(new String(buffer.array(), 0, len));
                    //退出消息q
                    if (message.getType() == -1) {
                        out = true;
                        socketChannel.close();
                        break;
                    } else {
                        buffer.clear();
                        System.out.println(message.getSender() + ": " + message.getMessage());
                    }
                }
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        assert args[0] != null;
        new Client(args[0]).start();
    }
}
