package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Выход из программы...");
    }
}
