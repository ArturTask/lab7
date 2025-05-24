package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PrintDescendingCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String descending = LabWorkTreeSetManager.getInstance().getElementsDescending();
        oos.writeUTF(descending);
    }
}

