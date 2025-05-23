package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;

public class ClearCommand implements Command {
    @Override
    public void execute() {
        LabWorkTreeSetManager.getInstance().clear();
        System.out.println("Коллекция очищена.");
    }
}
