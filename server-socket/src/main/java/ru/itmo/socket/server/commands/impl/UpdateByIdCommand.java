package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        Flat updatedFlat = (Flat) args[0];
        long id = updatedFlat.getId();

        if (!Storage.getInstance().containsId(id)) {
            oos.writeUTF("Элемент с id " + id + " не обновлен.");
            return;
        }

        // Получаем текущий элемент
        Flat currentFLat = Storage.getInstance().getAllElements().stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElse(null);

        if (Storage.getInstance().updateById(id, updatedFlat)) {
            oos.writeUTF("Элемент успешно обновлён.");
        } else {
            oos.writeUTF("Ошибка обновления элемента с id " + id);
        }
    }

    @Override
    public Class<?> getArgType() {
        return Flat.class;
    }
}



