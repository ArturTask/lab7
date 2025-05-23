package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.entity.LabWork;

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

