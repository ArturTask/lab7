package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ShowCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String allElements = LabWorkTreeSetManager.getInstance().getAllElementsAsString();
        oos.writeUTF(allElements);
    }
}
