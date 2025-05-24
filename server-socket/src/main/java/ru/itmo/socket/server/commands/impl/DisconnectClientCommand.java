package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class DisconnectClientCommand implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        System.out.println("Клиент отключен.");
        oos.writeUTF("DisconnectClient - Пока пока, клиент, отключение...");
    }
}
