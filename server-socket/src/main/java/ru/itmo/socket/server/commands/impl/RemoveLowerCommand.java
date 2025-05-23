package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.entity.LabWork;

import java.util.Scanner;

public class RemoveLowerCommand implements Command {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные элемента для удаления всех меньших:");
        // Используем вспомогательный класс для ввода всех необходимых данных
        LabWork labWork = LabWorkInputHelper.readLabWork(scanner);

        int removedCount = LabWorkTreeSetManager.getInstance().removeLower(labWork);
        System.out.println("Удалено " + removedCount + " элементов, меньших заданного.");
    }
}

