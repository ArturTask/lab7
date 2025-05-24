package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.common.util.LabWorkInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class UpdateByIdCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные элемента который хотите поменять:");
        LabWork newLabWork = LabWorkInputHelper.readLabWork(scanner, true);
        return Optional.of(newLabWork);
    }
}
