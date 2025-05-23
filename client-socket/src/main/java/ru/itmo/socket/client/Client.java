package ru.itmo.socket.client;

import ru.itmo.socket.common.entity.Message;
import ru.itmo.socket.common.util.SocketContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (connectToServer(scanner)) break;
            }
        }
    }

    private static boolean connectToServer(Scanner scanner) throws InterruptedException {
        String host = SocketContext.getHost();
        int port = SocketContext.getPort();

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Подключено к серверу " + host + ":" + port);

            System.out.println("Введите команду ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                return true;
            }

            System.out.print("Введите сообщение: ");
            String text = scanner.nextLine();

            // Создаем и отправляем сообщение
            Message message = new Message(command, text);
            oos.writeObject(message);
            oos.flush();
            System.out.println("Отправлено серверу: " + message);

            // Получаем ответ от сервера
            Message response = (Message) ois.readObject();
            System.out.println("Получено от сервера: " + response);

        }
        catch (ConnectException cE){
            System.err.println("Ошибка клиента: " + cE.getMessage());
            Thread.sleep(5_000); // подождем перед повтором подключения
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
