package com.jarvins.bio;

import com.jarvins.Message;
import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread {

    private static final int DEFAULT_PORT = 10086;

    private final ExecutorService service = Executors.newFixedThreadPool(10);

    private final ServerSocket serverSocket;

    private final Map<Integer, Writer> clientsMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> clientNameMap = new ConcurrentHashMap<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_PORT);
    }

    private void addClient(Socket socket, String user) throws IOException {
        int port = socket.getPort();
        if (!clientsMap.containsKey(port)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientsMap.put(port, writer);
            clientNameMap.put(user, port);
            System.out.println(user + "已进入聊天室");
        }
    }

    private void removeClient(Socket socket, Message message) throws IOException {
        int port = socket.getPort();
        String sender = message.getSender();
        Writer writer = clientsMap.get(port);
        //向客户端发送退出消息
        Message msg = Message.quitMessage(sender);
        writer.write(Message.serialize(msg));
        writer.flush();
        clientsMap.remove(port);
        clientNameMap.remove(sender);
        socket.close();
        System.out.println(sender + "已退出聊天室");
        //用户都溜了
        if (clientNameMap.isEmpty() && clientsMap.isEmpty()) {
            serverSocket.close();
            System.out.println("用户已全部退出聊天室，服务器关闭....");
        }
    }

    private void forwardMessage(Socket socket, Message message) throws IOException {
        int port = socket.getPort();
        String sender = message.getSender();
        String receiver = message.getPosition();
        Message msg = Message.forwardMessage(sender, message.getMessage());
        //群发
        if (receiver.equals("all")) {
            for (Map.Entry<Integer, Writer> entry : clientsMap.entrySet()) {
                Integer k = entry.getKey();
                Writer v = entry.getValue();
                if (k != port) {
                    v.write(Message.serialize(msg));
                    v.flush();
                }
            }
        } else {
            Integer receiverPort = clientNameMap.get(receiver);
            if (receiverPort != null) {
                Writer writer = clientsMap.get(receiverPort);
                if (writer != null) {
                    writer.write(Message.serialize(msg));
                    writer.flush();
                }
            } else {
                msg = Message.notOnLineMessage(sender, receiver);
                Writer writer = clientsMap.get(port);
                writer.write(Message.serialize(msg));
                writer.flush();
            }
        }
    }

    @SneakyThrows
    @Override
    public void start() {
        System.out.println("服务器已启动,监听端口:" + DEFAULT_PORT + "......");
        while (true) {
            try {
                Socket accept = serverSocket.accept();
                service.execute(new ServerRunnable(accept));
            } catch (SocketException e) {
                //关闭线程池
                service.shutdown();
                System.out.println("服务器已关闭");
                break;
            }
        }
    }

    private class ServerRunnable implements Runnable {

        Socket socket;

        public ServerRunnable(Socket socket) {
            this.socket = socket;
        }

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                if (!socket.isClosed()) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream())
                    );
                    String msg = reader.readLine();
                    Message message = Message.deSerialize(msg);
                    //连接
                    if (message.getType() == 1) {
                        addClient(socket, message.getSender());
                    }
                    //退出
                    else if (message.getType() == -1) {
                        //
                        removeClient(socket, message);
                        //转发
                    } else {
                        forwardMessage(socket, message);
                    }
                } else {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}
