package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

@Deprecated
public class SaveCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.print("Введите имя файла для сохранения коллекции: ");
        String fileName = scanner.nextLine().trim();
        return Optional.of(fileName);
    }
}
