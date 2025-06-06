package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        LabWork updatedLabWork = (LabWork) args[0];
        long id = updatedLabWork.getId();

        if (LabWorkTreeSetManager.getInstance().updateById(id, updatedLabWork)) {
            oos.writeUTF("Элемент успешно обновлён.");
        } else {
            oos.writeUTF("Ошибка обновления элемента с id = " + id + ", элемент не найден.");
        }
    }

    @Override
    public Class<?> getArgType() {
        return LabWork.class;
    }
}



