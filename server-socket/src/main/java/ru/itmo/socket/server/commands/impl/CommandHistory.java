package ru.itmo.socket.server.commands.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommandHistory {
    static final int MAX_HISTORY_SIZE = 6;
    private static final Queue<String> history = new LinkedList<>();

    private CommandHistory() {}

    public static void addCommand(String command) {
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.poll();
        }
        history.add(command);
    }

    public static List<String> getHistory() {
        return new ArrayList<>(history);
    }
}
