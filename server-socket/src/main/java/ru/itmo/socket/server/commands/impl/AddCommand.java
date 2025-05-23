package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.entity.LabWork;

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
