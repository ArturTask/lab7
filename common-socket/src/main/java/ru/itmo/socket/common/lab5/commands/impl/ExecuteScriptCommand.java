package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.commands.ScriptExecutor;

import java.util.Scanner;

/**
 * Команда execute_script: запрашивает файл и передаёт его исполнение ScriptExecutor.
 */
public class ExecuteScriptCommand implements Command {

    @Override
    public void execute() {
        System.out.print("Введите имя файла скрипта: ");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine().trim();
        ScriptExecutor.execute(fileName);
    }
}




