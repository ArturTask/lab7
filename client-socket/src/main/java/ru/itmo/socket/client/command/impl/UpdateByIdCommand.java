package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.common.util.FlatInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class UpdateByIdCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные элемента который хотите поменять:");
        Flat newFlat = FlatInputHelper.readFlat(true);
        return Optional.of(newFlat);
    }
}
