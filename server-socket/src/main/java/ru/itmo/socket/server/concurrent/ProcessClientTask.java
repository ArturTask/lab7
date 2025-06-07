package ru.itmo.socket.server.concurrent;

import lombok.RequiredArgsConstructor;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ServerCommandContext;
import ru.itmo.socket.server.commands.impl.ExitCommand;
import ru.itmo.socket.server.commands.impl.LoginCommand;
import ru.itmo.socket.server.commands.impl.RegisterCommand;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class ProcessClientTask implements Runnable {
    private static final ExecutorService sender = Executors.newCachedThreadPool();

    private final Socket clientSocket;
    private final ConcurrentSkipListSet<Integer> availableIds;
    private final AtomicInteger connectionCounter;
    private final AtomicBoolean active = new AtomicBoolean(false);


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
        active.set(true);

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

            while (active.get()) {
                // в цикле обрабатываем команды с клиентской стороны подключения клиентов
                sendToClient(ois, oos);
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
     * <p>
     * active true - если любая команда кроме 'exit', false - если 'exit' (ставит в поле класса)
     */
    private void sendToClient(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException {

        // Чтение запроса от клиента (в объект)
        CommandDto commandDto = (CommandDto) ois.readObject();
        System.out.printf("Получено от клиента [%s]: %s%n", UserContext.getLogin(), commandDto);

        String commandName = commandDto.getCommandName();
        ServerCommand serverCommand = ServerCommandContext.getCommand(commandName);

//        // ВСЕГДА кидаем клиенту количество строк вывода (которое ему надо будет считать) из-за СКРИПТА (команда execute_script), потому что в скрипте может быть много команд :( у других команд по умолчанию - 1 строка вывода, можно если че переопределить для любой команды
        int numberOfOutputLines = serverCommand.getNumberOfOutputLines(commandDto.getArg());
        oos.writeUTF(String.valueOf(numberOfOutputLines));


        // проверка на авторизацию
        if (!(serverCommand instanceof LoginCommand || serverCommand instanceof RegisterCommand || serverCommand instanceof ExitCommand)
                && !UserContext.getAuthorized()) {
            System.err.printf("[Tech] [ERROR] Неавторизованный пользователь %s не может выполнять ничего кроме login/register/exit%n", UserContext.getLogin());
            oos.writeUTF("Неавторизованный пользователь не может выполнять ничего кроме login/register/exit");
            oos.flush();
            return; // skip this loop
        } else if (serverCommand instanceof LoginCommand || serverCommand instanceof RegisterCommand || serverCommand instanceof ExitCommand) {
            // execute immediately
            try {
                // выполняем команду!
                serverCommand.execute(oos, commandDto.getArg());
            } catch (AppExitException ignore) {
            }
            oos.flush();
            System.out.printf("Отправлено клиенту [%s] вывод команды: %s%n", UserContext.getLogin(), commandName);
            return;
        }

        Integer clientId = UserContext.getClientId();
        Boolean authorized = UserContext.getAuthorized();
        String login = UserContext.getLogin();
        Integer userId = UserContext.getDbUserId();
        Connection currentConnection = DbUserContext.getConnection();

        // отправка ответа асинхронно
        sender.submit(new SendClientTask(clientId, login, authorized, userId, currentConnection, serverCommand, oos, commandDto, active, commandName));
    }


}
