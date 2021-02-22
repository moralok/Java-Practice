package com.moralok.socket;

import org.apache.log4j.spi.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author moralok
 * @since 2021/2/22 3:25 下午
 */
public class HelloServer {

    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket;
            while ((socket = server.accept()) != null) {
                System.out.println("client connected");
                TimeUnit.SECONDS.sleep(10);
                try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                    Message message = (Message) objectInputStream.readObject();
                    System.out.println("server receive message: " + message.getContent());
                    message.setContent("new content");
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("occur exception: " + e.getMessage());
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("occur exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        HelloServer helloServer = new HelloServer();
        helloServer.start(6666);
    }
}
