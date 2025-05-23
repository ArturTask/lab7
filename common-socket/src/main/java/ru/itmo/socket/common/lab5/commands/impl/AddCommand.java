package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.lab5.model.LabWork;

import java.util.Scanner;

public class AddCommand implements Command {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные нового элемента:");
        // Использование вспомогательного класса для ввода данных
        LabWork newLabWork = LabWorkInputHelper.readLabWork(scanner);

        if (LabWorkTreeSetManager.getInstance().add(newLabWork)) {
            System.out.println("Элемент успешно добавлен.");
        } else {
            System.out.println("Не удалось добавить элемент.");
        }
    }
}
