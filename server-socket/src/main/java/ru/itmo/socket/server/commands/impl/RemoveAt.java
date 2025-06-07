package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RemoveAt implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        long index = Long.parseLong(String.valueOf(args[0]));

        if (Storage.getInstance().getList().remove(index)) {
            oos.writeUTF("Элемент с id " + index + " успешно удалён!");
        } else {
            oos.writeUTF("Элемент с id " + index + " не удален!");
        }
    }
}
