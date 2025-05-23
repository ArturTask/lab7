package ru.itmo.socket.server;

import ru.itmo.socket.common.entity.Message;
import ru.itmo.socket.common.util.SocketContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = SocketContext.getPort();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

                    // Чтение объекта от клиента
                    Message clientMessage = (Message) ois.readObject();
                    System.out.println("Получено от клиента: " + clientMessage);

                    // Создание ответного сообщения
                    Message serverResponse = new Message("Сервер", "Привет, " + clientMessage.getSender() +
                            "! Ваше сообщение: " + clientMessage.getText());

                    // Отправка ответа клиенту
                    oos.writeObject(serverResponse);
                    oos.flush();
                    System.out.println("Отправлено клиенту: " + serverResponse);

                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка при чтении объекта: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }
}
