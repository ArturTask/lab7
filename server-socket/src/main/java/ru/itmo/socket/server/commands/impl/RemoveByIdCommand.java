package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.Command;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;

import java.util.Scanner;

public class RemoveByIdCommand implements Command {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите id элемента для удаления: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        if (LabWorkTreeSetManager.getInstance().removeById(id)) {
            System.out.println("Элемент с id " + id + " успешно удалён!");
        } else {
            System.out.println("Элемент с id " + id + " не найден!");
        }
    }
}
