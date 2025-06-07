package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.common.util.FlatInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class AddCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные нового элемента:");
        // Использование вспомогательного класса для ввода данных
        Flat newFlat = FlatInputHelper.readFlat();
        return Optional.of(newFlat);
    }
}
