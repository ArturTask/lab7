package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static ru.itmo.socket.server.commands.impl.CommandHistory.MAX_HISTORY_SIZE;

public class FilterLessThanMinimalPointCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        double minimalPoint = Double.parseDouble(String.valueOf(args[0]));
        List<LabWork> filtered = LabWorkTreeSetManager.getInstance().filterLessThanMinimalPoint(minimalPoint);
        if (filtered.isEmpty()) {
            oos.writeUTF("Нет элементов с minimalPoint меньше " + minimalPoint);
        } else {
            StringBuilder answer = new StringBuilder("Последние " + MAX_HISTORY_SIZE + " команд:\n");
            for (LabWork lab : filtered) {
                answer.append(lab).append("\n");
            }
            oos.writeUTF(answer.toString());
        }

    }
}

