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

        if (!LabWorkTreeSetManager.getInstance().containsId(id)) {
            oos.writeUTF("Элемент с id " + id + " не найден.");
            return;
        }

        // Получаем текущий элемент
        LabWork currentLabWork = LabWorkTreeSetManager.getInstance().getAllElements().stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElse(null);

        oos.writeUTF("Текущие данные элемента:\n" + currentLabWork);

        if (LabWorkTreeSetManager.getInstance().updateById(id, updatedLabWork)) {
            oos.writeUTF("Элемент успешно обновлён.");
        } else {
            oos.writeUTF("Ошибка обновления элемента с id " + id);
        }
    }

    @Override
    public Class<?> getArgType() {
        return LabWork.class;
    }
}



