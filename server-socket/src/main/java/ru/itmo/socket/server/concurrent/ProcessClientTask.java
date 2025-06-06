package ru.itmo.socket.server.concurrent;

import lombok.RequiredArgsConstructor;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ServerCommandContext;
import ru.itmo.socket.server.commands.impl.CommandHistory;

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
public class ProcessClientTask extends RecursiveTask<Void> {
    private final Socket clientSocket;
    private final ConcurrentSkipListSet<Integer> availableIds;
    private final AtomicInteger connectionCounter;

    // todo artur вынести в ThreadLocal чтобы удобно брать во всех командах
    private boolean authorized = false;
    private String login = "";

    private int clientId;


    @Override
    protected Void compute() {
        try {
            initThread();
            processConnection();
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        } finally {
            destroyThread();
        }
        return null;
    }

    /**
     * Чистим контекст пользователя
     */
    private void destroyThread() {
        availableIds.add(clientId); // finished processing this client
        connectionCounter.getAndDecrement();
        authorized = false;
        login = "";
        System.out.println("[Tech] [INFO] Клиент отключился, clientId = " + clientId);
    }

    /**
     * Инициализируем контекст пользователя
     */
    private void initThread() {
        clientId = availableIds.first(); // take first possible id
        availableIds.remove(clientId);

        connectionCounter.incrementAndGet(); // connection +1
        authorized = false;
        login = "unauthorized_" + clientId;
    }

    private void processConnection() throws IOException, ClassNotFoundException {
        if (clientSocket == null) {
            System.err.println("[Tech] [Error] Thread " + Thread.currentThread().getName() + " can't start working without clientSocket");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            System.out.println("[Tech] [INFO] Клиент подключился, clientId = " + clientId); // " + Thread.currentThread().getName() + "

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
