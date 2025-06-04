package ru.itmo.socket.server;

import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.common.util.SocketContext;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ServerCommandContext;
import ru.itmo.socket.server.commands.impl.CommandHistory;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.server.manager.XmlCollectionLoader;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        // загружаем из файла collection.txt изначальные значения
        LabWorkTreeSetManager manager = LabWorkTreeSetManager.getInstance();
        new XmlCollectionLoader(manager, "collection.txt").load();

        int port = SocketContext.getPort();

        // стартуем сервер
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен и ожидает подключения...");

            // ждем подключений
            acceptConnection(serverSocket);

        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void acceptConnection(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        try (Socket clientSocket = serverSocket.accept();
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

            while (true) {
                // в цикле обрабатываем команды с клиентской стороны подключения клиентов
                if (!sendToClient(ois, oos)) {
                    break;
                }
            }
        } catch (EOFException eofException) {
            System.err.println("Клиент неожиданно прекратил соединение, не удалось прочитать команду");
        }
    }

    /**
     * метод обработки запросов от клиента
     *
     * @return true - если любая команда кроме 'exit', false - если 'exit'
     */
    private static boolean sendToClient(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        boolean continueWorking = true;

        // Чтение запроса от клиента (в объект)
        CommandDto commandDto = (CommandDto) ois.readObject();
        System.out.println("Получено от клиента: " + commandDto);

        String commandName = commandDto.getCommandName();
        ServerCommand serverCommand = ServerCommandContext.getCommand(commandName);

        if (serverCommand != null) {
            // кидаем клиенту количество строк вывода (которое ему надо будет считать) из-за СКРИПТА (команда execute_script),
            // потому что в скрипте может быть много команд :(
            // у других команд по умолчанию - 1 строка вывода, можно если че переопределить для любой команды
            int numberOfOutputLines = serverCommand.getNumberOfOutputLines(commandDto.getArg());
            oos.writeUTF(String.valueOf(numberOfOutputLines));

            try {
                // выполняем команду!
                serverCommand.execute(oos, commandDto.getArg());
            } catch (AppExitException exitException) {
                // AppExitException бросается если команда ExitCommand
                // (через Exception, потому что в скрипте может быть exit => надо обработать сразу же)
                continueWorking = false;
            }
            CommandHistory.addCommand(commandName);
        } else {
            System.out.println("Команда не найдена! Введите 'help' для получения списка команд.");
        }
        oos.flush();
        System.out.println("Отправлено клиенту вывод команды: " + commandName);


        return continueWorking;
    }
}
