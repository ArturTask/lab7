package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class RemoveLower implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        Storage list = Storage.getInstance();
        double area = Double.parseDouble(String.valueOf(args[0]));
        List<Flat> filtered = Storage.getInstance().filterLessThanArea(area);
        if (filtered.isEmpty()) {
            oos.writeUTF("Нет элементов с area меньше " + area);
        } else {
            StringBuilder answer = new StringBuilder();
            filtered.forEach(flat -> {
                list.remove(flat);
                answer.append("\nКвартира с id ").append(flat.getId()).append(" была удалена");
            });
            oos.writeUTF(answer.toString());
        }
    }
}
