package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        Flat newFlat = (Flat) args[0];

        if (Storage.getInstance().add(newFlat)) {
            oos.writeUTF("Элемент успешно добавлен.");
        } else {
            oos.writeUTF("Не удалось добавить элемент.");
        }
    }


    @Override
    public Class<?> getArgType() {
        return Flat.class;
    }
}
