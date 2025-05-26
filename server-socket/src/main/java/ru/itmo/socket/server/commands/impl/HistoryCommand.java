package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static ru.itmo.socket.server.commands.impl.CommandHistory.MAX_HISTORY_SIZE;

public class HistoryCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        StringBuilder answer = new StringBuilder("Последние " + MAX_HISTORY_SIZE + " команд:\n");
        for (String record : CommandHistory.getHistory()) {
            answer.append(record).append("\n");
        }
        oos.writeUTF(answer.toString());
    }
}
