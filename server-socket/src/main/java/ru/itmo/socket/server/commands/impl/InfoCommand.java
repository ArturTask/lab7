package ru.itmo.socket.server.commands.impl;


import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

public class InfoCommand implements Command {
    @Override
    public void execute() {
        String info = LabWorkTreeSetManager.getInstance().getCollectionInfo();
        System.out.println(info);
    }
}

