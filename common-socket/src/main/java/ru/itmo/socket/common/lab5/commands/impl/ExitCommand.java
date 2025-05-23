package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Выход из программы...");
    }
}
