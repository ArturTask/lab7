package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

public class ClearCommand implements Command {
    @Override
    public void execute() {
        LabWorkTreeSetManager.getInstance().clear();
        System.out.println("Коллекция очищена.");
    }
}
