package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.lab5.model.Coordinates;
import ru.itmo.socket.common.lab5.model.LabWork;
import ru.itmo.socket.common.lab5.model.Person;

import java.util.Scanner;

public class UpdateIdCommand implements Command {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите id элемента для обновления: ");
        long id = Long.parseLong(scanner.nextLine().trim());

        if (!LabWorkTreeSetManager.getInstance().containsId(id)) {
            System.out.println("Элемент с id " + id + " не найден.");
            return;
        }

        // Получаем текущий элемент
        LabWork currentLabWork = LabWorkTreeSetManager.getInstance().getAllElements().stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElse(null);

        if (currentLabWork == null) {
            System.out.println("Ошибка: элемент не найден.");
            return;
        }

        System.out.println("Текущие данные элемента:");
        System.out.println(currentLabWork);

        // Создаем копию текущего элемента
        LabWork updatedLabWork = new LabWork(
                currentLabWork.getName(),
                new Coordinates(
                        currentLabWork.getCoordinates().getX(),
                        currentLabWork.getCoordinates().getY()
                ),
                currentLabWork.getMinimalPoint(),
                currentLabWork.getDifficulty(),
                new Person(
                        currentLabWork.getAuthor().getName(),
                        currentLabWork.getAuthor().getBirthday(),
                        currentLabWork.getAuthor().getHeight(),
                        currentLabWork.getAuthor().getWeight(),
                        currentLabWork.getAuthor().getEyeColor()
                )
        );
        updatedLabWork.setId(id);

        System.out.println("\nВыберите поля для обновления (введите 'да' для изменения, 'нет' для пропуска):");

        // Обновление основных полей LabWork
        updateLabWorkFields(scanner, updatedLabWork);

        // Обновление полей автора
        updateAuthorFields(scanner, updatedLabWork.getAuthor());

        if (LabWorkTreeSetManager.getInstance().updateById(id, updatedLabWork)) {
            System.out.println("Элемент успешно обновлён.");
        } else {
            System.out.println("Ошибка обновления элемента с id " + id);
        }
    }

    private void updateLabWorkFields(Scanner scanner, LabWork labWork) {
        if (askForUpdate(scanner, "Название")) {
            labWork.setName(LabWorkInputHelper.inputName(scanner));
        }

        if (askForUpdate(scanner, "Координаты")) {
            labWork.setCoordinates(LabWorkInputHelper.inputCoordinates(scanner));
        }

        if (askForUpdate(scanner, "Минимальный балл")) {
            labWork.setMinimalPoint(LabWorkInputHelper.inputMinimalPoint(scanner));
        }

        if (askForUpdate(scanner, "Сложность")) {
            labWork.setDifficulty(LabWorkInputHelper.inputDifficulty(scanner));
        }
    }

    private void updateAuthorFields(Scanner scanner, Person author) {
        System.out.println("\nОбновление полей автора:");

        if (askForUpdate(scanner, "Имя автора")) {
            author.setName(LabWorkInputHelper.inputAuthorName(scanner));
        }

        if (askForUpdate(scanner, "День рождения автора")) {
            author.setBirthday(LabWorkInputHelper.inputAuthorBirthday(scanner));
        }

        if (askForUpdate(scanner, "Рост автора")) {
            author.setHeight(LabWorkInputHelper.inputAuthorHeight(scanner));
        }

        if (askForUpdate(scanner, "Вес автора")) {
            author.setWeight(LabWorkInputHelper.inputAuthorWeight(scanner));
        }

        if (askForUpdate(scanner, "Цвет глаз автора")) {
            author.setEyeColor(LabWorkInputHelper.inputAuthorEyeColor(scanner));
        }
    }

    private boolean askForUpdate(Scanner scanner, String fieldName) {
        while (true) {
            System.out.printf("Обновить поле '%s'? (да/нет): ", fieldName);
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("да")) {
                return true;
            } else if (answer.equals("нет")) {
                return false;
            } else {
                System.out.println("Пожалуйста, введите 'да' или 'нет'.");
            }
        }
    }
}



