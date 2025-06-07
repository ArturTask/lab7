package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class GroupContingById implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException{
        Flat groupFlat = (Flat) args[0];
        long id = groupFlat.getId();

        if (!Storage.getInstance().containsId(id)) {
            oos.writeUTF("Элемент с id " + id + " не найден.");
        }

        Map<Long, Long> grouped = Storage.getInstance().groupCountingById(id);

        if (grouped.isEmpty()) {
            oos.writeUTF("Ошибка группировки элементов с id " + id);
        } else {
            oos.writeUTF("Группировка элементов по ID (остаток от деления на 10):");
            grouped.forEach((key, count) -> {
                try {
                    oos.writeUTF("Группа ID % 10 = " + key + "; элементов: " + count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
