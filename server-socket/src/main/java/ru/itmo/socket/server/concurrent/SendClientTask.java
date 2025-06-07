package ru.itmo.socket.server.concurrent;

import lombok.RequiredArgsConstructor;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class SendClientTask implements Runnable {
    private final int clientId;
    private final String login;
    private final boolean authorized;
    private final int userId;
    private final Connection currentConnection;
    private final ServerCommand serverCommand;

    private final ObjectOutputStream oos;
    private final CommandDto commandDto;
    private final AtomicBoolean active;
    private final String commandName;

    @Override
    public void run() {
        try {
            UserContext.inheritContext(clientId, login, authorized, userId);
            DbUserContext.inheritConnection(currentConnection);
            try {
                // выполняем команду!
                serverCommand.execute(oos, commandDto.getArg());
            } catch (AppExitException exitException) {
                // AppExitException бросается если команда ExitCommand (через Exception, потому что в скрипте может быть exit => надо обработать сразу же)
                active.compareAndSet(true, false);
            }
            oos.flush();
            System.out.printf("Отправлено клиенту [%s] вывод команды: %s%n", UserContext.getLogin(), commandName);
        } catch (Exception e) {
            System.err.printf("Произошла ошибка при отправке ответа команды %s пользователю %s%n", commandName, UserContext.getLogin());
            e.printStackTrace();
        } finally {
            UserContext.destroyUserContextWithoutDisconnect();
        }
    }
}
