package com.jarvins;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
public class Message implements Serializable {

    String sender;
    String position;
    String message;
    int type;

    public Message(String sender, String position, String message, int type) {
        this.sender = sender;
        this.position = position;
        this.message = message;
        this.type = type;
    }

    public static Message enterMessage(String sender) {
        return new Message(sender, null, null, 1);
    }

    public static Message quitMessage(String sender) {
        return new Message(sender, null, null, -1);
    }

    public static Message sendMessage(String sender, String position, String message) {
        return new Message(sender, position, message, 0);
    }

    //服务端发送客户端
    public static Message notOnLineMessage(String sender, String receiver) {
        return new Message("服务器", sender, receiver + "未上线", Integer.MIN_VALUE);

    }

    //服务端转发消息
    public static Message forwardMessage(String sender, String message) {
        return new Message(sender, null, message, Integer.MAX_VALUE);
    }

    public static String serialize(Message message) {
        return JSON.toJSONString(message) + '\n';
    }

    public static Message deSerialize(String str) {
        return JSON.parseObject(str, Message.class);
    }
}
