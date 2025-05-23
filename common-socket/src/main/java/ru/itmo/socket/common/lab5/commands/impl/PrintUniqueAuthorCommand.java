package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;

import java.util.Set;

public class PrintUniqueAuthorCommand implements Command {
    @Override
    public void execute() {
        Set<String> uniqueAuthors = LabWorkTreeSetManager.getInstance().getUniqueAuthors();
        if (uniqueAuthors.isEmpty()) {
            System.out.println("Нет уникальных авторов.");
        } else {
            uniqueAuthors.forEach(System.out::println);
        }
    }
}

