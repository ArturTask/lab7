package ru.itmo.socket.server;

import ru.itmo.socket.common.entity.Message;
import ru.itmo.socket.common.util.SocketContext;
import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.commands.CommandsContext;
import ru.itmo.socket.server.commands.impl.CommandHistory;
import ru.itmo.socket.server.commands.impl.ExitCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.server.manager.XmlCollectionLoader;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        int port = SocketContext.getPort();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

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

                }
                catch (EOFException eofException){
                    System.err.println("Клиент неожиданно прекратил соединение, не удалось прочитать команду");
                }
                catch (ClassNotFoundException e) {
                    System.err.println("Ошибка при чтении объекта: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void prevMain(){
        LabWorkTreeSetManager manager = LabWorkTreeSetManager.getInstance();
        new XmlCollectionLoader(manager, "collection.txt").load();
        run();
    }


    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в приложение! Введите 'help' для получения списка команд.");

        while (true) {
            System.out.print("> ");
            String commandName = scanner.nextLine().trim();
            Command command = CommandsContext.getCommand(commandName);

            if (command != null) {
                command.execute();
                CommandHistory.addCommand(commandName);
                if (command instanceof ExitCommand) {
                    break;
                }
            } else {
                System.out.println("Команда не найдена! Введите 'help' для получения списка команд.");
            }
        }
    }

}
