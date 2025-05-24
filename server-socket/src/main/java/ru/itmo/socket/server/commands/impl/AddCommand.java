package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        LabWork newLabWork = (LabWork) args[0];

        if (LabWorkTreeSetManager.getInstance().add(newLabWork)) {
            oos.writeUTF("Элемент успешно добавлен.");
        } else {
            oos.writeUTF("Не удалось добавить элемент.");
        }
    }


    @Override
    public Class<?> getArgType() {
        return LabWork.class;
    }
}
