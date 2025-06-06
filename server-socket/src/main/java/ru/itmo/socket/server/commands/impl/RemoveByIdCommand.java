package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RemoveByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        long id = Long.parseLong(String.valueOf(args[0]));

        if (LabWorkTreeSetManager.getInstance().removeById(id)) {
            oos.writeUTF("Элемент с id " + id + " успешно удалён!");
        } else {
            oos.writeUTF("Не удалось удалить элемент с id " + id);
        }
    }
}
