package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddIfMaxCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        LabWork labWork = (LabWork) args[0];
        // Если новый элемент больше максимального в коллекции, он будет добавлен.
        if (LabWorkTreeSetManager.getInstance().addIfMax(labWork)) {
            oos.writeUTF("Элемент добавлен, так как он максимальный.");
        } else {
            oos.writeUTF("Элемент не является максимальным, добавление не выполнено.");
        }
    }


    @Override
    public Class<?> getArgType() {
        return LabWork.class;
    }
}

