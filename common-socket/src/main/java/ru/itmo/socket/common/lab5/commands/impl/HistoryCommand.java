package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;

import static ru.itmo.socket.common.lab5.commands.impl.CommandHistory.MAX_HISTORY_SIZE;

// todo artur move out
public class HistoryCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Последние " + MAX_HISTORY_SIZE + " команд:");
        CommandHistory.getHistory().forEach(System.out::println);
    }
}
