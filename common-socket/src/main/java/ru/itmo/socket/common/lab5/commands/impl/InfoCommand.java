package ru.itmo.socket.common.lab5.commands.impl;


import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;

public class InfoCommand implements Command {
    @Override
    public void execute() {
        String info = LabWorkTreeSetManager.getInstance().getCollectionInfo();
        System.out.println(info);
    }
}

