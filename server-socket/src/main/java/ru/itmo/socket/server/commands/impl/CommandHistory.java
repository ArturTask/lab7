package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.concurrent.UserContext;

import java.util.*;

public class CommandHistory {
    static final int MAX_HISTORY_SIZE = 7;
    private static final Map<Integer, Queue<String>> historyMap = new HashMap<>();

    private CommandHistory() {}

    public static void addCommand(String command) {
        if (!UserContext.getAuthorized()) {
            return; // save only history for authorized users!
        }
        Queue<String> history = getHistoryOfCurrentUser();
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.poll();
        }
        history.add(command);
    }

    public static List<String> getHistory() {
        return new ArrayList<>(getHistoryOfCurrentUser());
    }

    private static Queue<String> getHistoryOfCurrentUser() {
        Integer dbUserId = UserContext.getDbUserId();
        Queue<String> history = historyMap.get(dbUserId);
        if (history == null) {
            history = new LinkedList<>();
            historyMap.put(dbUserId, history);
        }
        return history;
    }
}
