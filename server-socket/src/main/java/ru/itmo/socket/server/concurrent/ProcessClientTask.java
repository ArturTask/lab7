package ru.itmo.socket.server.concurrent;

import lombok.RequiredArgsConstructor;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ServerCommandContext;
import ru.itmo.socket.server.commands.impl.CommandHistory;
import ru.itmo.socket.server.commands.impl.ExitCommand;
import ru.itmo.socket.server.commands.impl.LoginCommand;
import ru.itmo.socket.server.commands.impl.RegisterCommand;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class ProcessClientTask implements Runnable {
    private final Socket clientSocket;
    private final ConcurrentSkipListSet<Integer> availableIds;
    private final AtomicInteger connectionCounter;


    @Override
    public void run() {
        try {
            initThread();
            processConnection();
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        } finally {
            destroyThread();
        }
    }

    /**
     * Чистим контекст пользователя
     */
    private void destroyThread() {
        availableIds.add(UserContext.getClientId()); // finished processing this client
        connectionCounter.getAndDecrement();
        System.out.printf("[Tech] [INFO] Клиент [%s] отключился %n", UserContext.getLogin());
        UserContext.destroyUserContext();
    }

    /**
     * Инициализируем контекст пользователя
     */
    private void initThread() {
        int clientId = availableIds.first(); // take first possible id
        availableIds.remove(clientId);
        connectionCounter.incrementAndGet(); // connection +1

        UserContext.initUserContext(clientId);
        System.out.printf("[Tech] [INFO] Клиент [%s] подключился %n", UserContext.getLogin()); // " + Thread.currentThread().getName() + "
    }

    private void processConnection() throws IOException, ClassNotFoundException {
        if (clientSocket == null) {
            System.err.println("[Tech] [Error] Thread " + Thread.currentThread().getName() + " can't start working without clientSocket");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                // в цикле обрабатываем команды с клиентской стороны подключения клиентов
                if (!sendToClient(ois, oos)) {
                    break;
                }
            }

        } catch (SocketException ignore) {
            // ignore connection attempts of finished jars
        } catch (EOFException eofException) {
            System.err.println("Клиент неожиданно прекратил соединение, не удалось прочитать команду");
        } finally {
            clientSocket.close();
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
        System.out.printf("Получено от клиента [%s]: %s%n", UserContext.getLogin() , commandDto);

        String commandName = commandDto.getCommandName();
        ServerCommand serverCommand = ServerCommandContext.getCommand(commandName);

        if (serverCommand != null) {
            // кидаем клиенту количество строк вывода (которое ему надо будет считать) из-за СКРИПТА (команда execute_script),
            // потому что в скрипте может быть много команд :(
            // у других команд по умолчанию - 1 строка вывода, можно если че переопределить для любой команды
            int numberOfOutputLines = serverCommand.getNumberOfOutputLines(commandDto.getArg());
            oos.writeUTF(String.valueOf(numberOfOutputLines));

            // проверка на авторизацию
            if(!(serverCommand instanceof LoginCommand || serverCommand instanceof RegisterCommand || serverCommand instanceof ExitCommand)
                    && !UserContext.getAuthorized()){
                System.err.printf("[Tech] [ERROR] Неавторизованный пользователь %s не может выполнять ничего кроме login/register%n", UserContext.getLogin());
                oos.writeUTF("Неавторизованный пользователь не может выполнять ничего кроме login/register");
                oos.flush();
                return true; // skip this loop
            }

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
        System.out.printf("Отправлено клиенту [%s] вывод команды: %s%n",UserContext.getLogin() , commandName);


        return continueWorking;
    }


}
