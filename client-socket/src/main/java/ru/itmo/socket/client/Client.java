package ru.itmo.socket.client;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.client.command.ClientCommandContext;
import ru.itmo.socket.client.command.impl.DisconnectClientCommand;
import ru.itmo.socket.client.command.impl.ExitCommand;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.util.SocketContext;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!processRemoteCommand(scanner)) {
                break;
            }
        }


    }

    /**
     * @return true - если любая команда кроме 'exit', false - если 'exit' и 'disconnect'
     */
    private static boolean processRemoteCommand(Scanner scanner) throws InterruptedException {
        String host = SocketContext.getHost();
        int port = SocketContext.getPort();
        boolean continueWork = true;

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println();
            System.out.println("Подключено к серверу " + host + ":" + port);

            System.out.println("Введите команду ");
            String stringCommand = scanner.nextLine();

            // парсим команду
            ClientCommand clientCommand = ClientCommandContext.getCommand(stringCommand);
            // это мы получаем доп параметры (если надо в конкретной команде)
            // например при добавлении пользователя
            Optional<Object> clientCommandParam = clientCommand.preProcess(scanner);

            // это если мы отключаемся от сервера - disconnect
            // или выключаем и сервер и клиент!
            if (clientCommand instanceof DisconnectClientCommand || clientCommand instanceof ExitCommand) {
                continueWork = false;
            }

            // добавляем доп аргументы если нужно к команде и отправляем на сервер
            Object arg = clientCommandParam.orElse(null);
            CommandDto request = new CommandDto(stringCommand, arg);

            oos.writeObject(request);
            oos.flush();
            System.out.println("Отправлено серверу: " + request);

            // Получаем ответ от сервера, вначале количество строк, потом сами строки
            // (это появилось из-за скриптов (команда execute_script), если скрипт, то там с сервера приходит несколько строк)
            int responseQuantity = Integer.parseInt(ois.readUTF());
            for (int i = 0; i < responseQuantity; i++) {
                String response = ois.readUTF();
                System.out.println("Строка #" + (i + 1) + ": \n");
                System.out.println("Получено от сервера: " + response);

                // если в скрипте на сервере будет exit, то он пришлет в сообщении AppExit
                if (response.contains("AppExit") || response.contains("DisconnectClient")) {
                    continueWork = false;
                    break;
                }
            }
        } catch (ConnectException cE) {
            System.err.println("Server unreachable, waiting for server to start...");
            Thread.sleep(5_000); // подождем перед повтором подключения
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
        return continueWork;
    }
}
