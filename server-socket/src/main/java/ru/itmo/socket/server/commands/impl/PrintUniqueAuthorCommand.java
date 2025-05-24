package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ScriptHelper;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

import static ru.itmo.socket.server.commands.impl.CommandHistory.MAX_HISTORY_SIZE;

public class PrintUniqueAuthorCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {

        Set<String> uniqueAuthors = LabWorkTreeSetManager.getInstance().getUniqueAuthors();
        if (uniqueAuthors.isEmpty()) {
            oos.writeUTF("Нет уникальных авторов.");
        } else {
            StringBuilder answer = new StringBuilder();
            for (String author : uniqueAuthors) {
                answer.append(author).append("\n");
            }
            oos.writeUTF(answer.toString());
        }
    }
}

