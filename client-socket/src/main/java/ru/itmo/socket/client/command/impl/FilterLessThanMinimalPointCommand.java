package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class FilterLessThanMinimalPointCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.print("Введите значение minimalPoint: ");
        double minimalPoint = Double.parseDouble(scanner.nextLine().trim());
        return Optional.of(minimalPoint);
    }
}
