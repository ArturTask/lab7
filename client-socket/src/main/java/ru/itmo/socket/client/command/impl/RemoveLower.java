package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class RemoveLower implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите площадь квартиры для удаления квартир:");
        long area = Long.parseLong(scanner.nextLine().trim());
        return Optional.of(area);
    }
}
