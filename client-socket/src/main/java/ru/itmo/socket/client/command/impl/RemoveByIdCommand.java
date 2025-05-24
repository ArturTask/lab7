package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class RemoveByIdCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.print("Введите id элемента для удаления: ");
        long id = Long.parseLong(scanner.nextLine().trim());
        return Optional.of(id);
    }
}
