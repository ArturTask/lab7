package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.common.util.LabWorkInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class AddIfMaxCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные нового элемента для проверки на максимальность:");

        // Используем вспомогательный класс для ввода всех данных нового LabWork
        LabWork labWork = LabWorkInputHelper.readLabWork(scanner);
        return Optional.of(labWork);
    }
}
