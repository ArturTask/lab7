package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RemoveByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        long id = Long.parseLong(String.valueOf(args[0]));

        if (Storage.getInstance().removeById(id)) {
            oos.writeUTF("Элемент с id " + id + " успешно удалён!");
        } else {
            oos.writeUTF("Элемент с id " + id + " не удален!");
        }
    }
}
