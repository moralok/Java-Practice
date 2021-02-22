package com.moralok.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author moralok
 * @since 2021/2/22 3:36 下午
 */
public class HelloClient {

    public Message send(Message message, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return (Message) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("occur exception: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        HelloClient helloClient = new HelloClient();
        for (int i = 0; i < 3; i++) {
            Message contentFromClient = helloClient.send(new Message("content from client"), "127.0.0.1", 6666);
            System.out.println(contentFromClient);
        }
    }
}
