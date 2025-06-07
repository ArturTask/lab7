package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class RemoveAt implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите индекс элемента для удаления:");
        long id = Long.parseLong(scanner.nextLine().trim());
        return Optional.of(id);
    }
}
