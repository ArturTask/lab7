package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class ExecuteScriptCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.print("Введите имя файла скрипта: ");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine().trim();
        return Optional.of(fileName);
    }
}
