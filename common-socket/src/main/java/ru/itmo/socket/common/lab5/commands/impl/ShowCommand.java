package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;

public class ShowCommand implements Command {
    @Override
    public void execute() {
        String allElements = LabWorkTreeSetManager.getInstance().getAllElementsAsString();
        System.out.println(allElements);
    }
}
