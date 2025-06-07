package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class CountGreaterThanCentralHeating implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите true, чтобы посчитать количество квартир с отоплением или false, чтобы посчитать квартиры без отопения:");
        boolean heating = Boolean.parseBoolean(scanner.nextLine().trim());
        return Optional.of(heating);
    }
}