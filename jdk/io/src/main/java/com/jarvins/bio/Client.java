package com.jarvins.bio;

import com.jarvins.Message;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Client extends Thread {


    private static final int DEFAULT_PORT = 10086;
    private static final CountDownLatch latch = new CountDownLatch(2);
    private static boolean out = false;
    private static Socket socket;

    static {
        try {
            socket = new Socket("localhost", DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //客户端昵称
    private final String user;

    public Client(String user) {
        this.user = user;
    }

    @SneakyThrows
    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Message enter = Message.enterMessage(user);
        writer.write(Message.serialize(enter));
        writer.flush();
        new Sender(user, writer).start();
        new Receiver(reader).start();
        latch.await();
        socket.close();
        System.out.println("退出聊天室");
    }

    private static class Sender extends Thread {

        String user;
        BufferedWriter writer;

        public Sender(String user, BufferedWriter writer) {
            this.user = user;
            this.writer = writer;
        }

        @SneakyThrows
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("输入信息发送方昵称(all表示群发，quit表示退出聊天室):");
                String position = scanner.next();
                if (position.equals("quit")) {
                    if (!socket.isOutputShutdown()) {
                        Message message = Message.quitMessage(user);
                        writer.write(Message.serialize(message));
                        writer.flush();
                    }
                    break;
                } else {
                    System.out.println("输入发送内容:");
                    String msg = scanner.next();
                    if (!socket.isOutputShutdown()) {
                        Message message = Message.sendMessage(user, position, msg);
                        writer.write(Message.serialize(message));
                        writer.flush();
                    }

                }
            }
            latch.countDown();
        }
    }


    private static class Receiver extends Thread {

        BufferedReader reader;

        public Receiver(BufferedReader reader) {
            this.reader = reader;
        }


        @SneakyThrows
        @Override
        public void run() {
            while (!out) {
                if (!socket.isInputShutdown()) {
                    String message = reader.readLine();
                    Message msg = Message.deSerialize(message);
                    //退出消息
                    if (msg.getType() == -1) {
                        out = true;
                        socket.close();
                    } else {
                        System.out.println(msg.getSender() + ": " + msg.getMessage());
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

