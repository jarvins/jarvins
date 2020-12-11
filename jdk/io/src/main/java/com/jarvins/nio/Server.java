package com.jarvins.nio;

import com.jarvins.Message;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread {

    private final static int DEFAULT_PORT = 10086;
    private final static SocketAddress ADDRESS = new InetSocketAddress(DEFAULT_PORT);
    private final static int BUFFER_SIZE = 1024;
    private final static ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    private final Map<String, SocketChannel> clientMap = new ConcurrentHashMap<>();

    private static ServerSocketChannel server;
    private static Selector selector;
    private static int len;


    static {
        try {
            server = ServerSocketChannel.open();
            server.bind(ADDRESS);
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已启动,监听端口:" + DEFAULT_PORT + "......");
        while (server.isOpen()) {
            int select = selector.select(1000);
            if (select == 0) {
                System.out.println("当前没有已准备好的通道...");
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                handleKey(next);
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel channel = serverSocketChannel.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                while ((len = channel.read(buffer)) > 0) {
                    buffer.flip();
                    String msg = new String(buffer.array(), 0, len);
                    buffer.clear();
                    Message message = Message.deSerialize(msg);
                    //连接
                    if (message.getType() == 1) {
                        addClient(message, channel);
                    }
                    //退出
                    else if (message.getType() == -1) {
                        removeClient(key, message);
                    }
                    //转发
                    else {
                        forwardMessage(message);
                    }
                }
            }
        }
    }

    private void addClient(Message message, SocketChannel channel) {
        String sender = message.getSender();
        clientMap.put(sender, channel);
        System.out.println(sender + "已进入聊天室");
    }

    private void removeClient(SelectionKey key, Message message) throws IOException {
        SocketChannel channel = clientMap.get(message.getSender());
        buffer.put(Message.serialize(Message.quitMessage(message.getSender())).getBytes());
        buffer.flip();
        channel.write(buffer);
        buffer.clear();
        key.cancel();
        clientMap.remove(message.getSender());
        if (clientMap.isEmpty()) {
            server.close();
            System.out.println("用户已全部退出聊天室，服务器关闭....");
        }
    }

    private void forwardMessage(Message message) throws IOException {
        String sender = message.getSender();
        String position = message.getPosition();
        Message msg = Message.forwardMessage(sender, message.getMessage());
        if (position.equals("all")) {
            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                String k = entry.getKey();
                SocketChannel v = entry.getValue();
                if (!k.equals(sender)) {
                    buffer.put(Message.serialize(msg).getBytes());
                    buffer.flip();
                    v.write(buffer);
                    buffer.clear();
                }
            }
        } else {
            SocketChannel channel = clientMap.get(position);
            if (channel == null) {
                SocketChannel sendChannel = clientMap.get(sender);
                buffer.put(Message.serialize(Message.notOnLineMessage(sender, position)).getBytes());
                buffer.flip();
                sendChannel.write(buffer);
                buffer.clear();
            } else {
                buffer.put(Message.serialize(msg).getBytes());
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
