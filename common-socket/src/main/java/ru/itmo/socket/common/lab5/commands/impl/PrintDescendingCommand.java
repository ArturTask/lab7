package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;

public class PrintDescendingCommand implements Command {
    @Override
    public void execute() {
        String descending = LabWorkTreeSetManager.getInstance().getElementsDescending();
        System.out.println(descending);
    }
}

