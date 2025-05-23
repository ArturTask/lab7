package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.lab5.model.LabWork;

import java.util.List;
import java.util.Scanner;

public class FilterLessThanMinimalPointCommand implements Command {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите значение minimalPoint: ");
        double minimalPoint = Double.parseDouble(scanner.nextLine().trim());

        List<LabWork> filtered = LabWorkTreeSetManager.getInstance().filterLessThanMinimalPoint(minimalPoint);
        if (filtered.isEmpty()) {
            System.out.println("Нет элементов с minimalPoint меньше " + minimalPoint);
        } else {
            filtered.forEach(System.out::println);
        }
    }
}

