package ru.itmo.socket.client;

import ru.itmo.socket.common.entity.Message;
import ru.itmo.socket.common.util.SocketContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String host = SocketContext.getHost();
        int port = SocketContext.getPort();
        boolean stop = false;

        while (!stop) {
            try (Socket socket = new Socket(host, port);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println("Подключено к серверу " + host + ":" + port);

                System.out.print("Введите ваше имя: ");
                String name = scanner.nextLine();

                if (name.equalsIgnoreCase("exit")) {
                    stop = true;
                }

                System.out.print("Введите сообщение: ");
                String text = scanner.nextLine();

                // Создаем и отправляем сообщение
                Message message = new Message(name, text);
                oos.writeObject(message);
                oos.flush();
                System.out.println("Отправлено серверу: " + message);

                // Получаем ответ от сервера
                Message response = (Message) ois.readObject();
                System.out.println("Получено от сервера: " + response);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка клиента: " + e.getMessage());
            }
        }
    }
}
