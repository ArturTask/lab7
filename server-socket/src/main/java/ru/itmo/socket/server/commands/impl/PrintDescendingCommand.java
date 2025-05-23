package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

public class PrintDescendingCommand implements Command {
    @Override
    public void execute() {
        String descending = LabWorkTreeSetManager.getInstance().getElementsDescending();
        System.out.println(descending);
    }
}

