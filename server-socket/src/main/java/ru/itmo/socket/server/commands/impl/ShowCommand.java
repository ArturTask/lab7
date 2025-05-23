package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

public class ShowCommand implements Command {
    @Override
    public void execute() {
        String allElements = LabWorkTreeSetManager.getInstance().getAllElementsAsString();
        System.out.println(allElements);
    }
}
