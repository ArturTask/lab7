package ru.itmo.socket.common.lab5.commands;

import ru.itmo.socket.common.lab5.main.MainMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ScriptExecutor {
    private static final Set<String> runningScripts = new HashSet<>();

    /**
     * Исполняет файл-скрипт построчно.
     * Запрещает повторный запуск уже запущенного файла (рекурсию).
     */
    public static void execute(String fileName) {
        if (runningScripts.contains(fileName)) {
            System.out.println("Ошибка: рекурсивный вызов скрипта '" + fileName + "' обнаружен. Выполнение прекращено.");
            return;
        }

        File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Файл скрипта не найден: " + fileName);
            return;
        }

        runningScripts.add(fileName);
        try (Scanner fileScanner = new Scanner(file)) {
            MainMenu menu = new MainMenu();
            Map<String, Command> commands = menu.getCommandMap();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String cmdName = parts[0];
                String arg     = parts.length > 1 ? parts[1] : null;

                if ("execute_script".equals(cmdName)) {
                    if (arg == null) {
                        System.out.println("Ошибка: имя файла не указано в команде execute_script.");
                    } else {
                        ScriptExecutor.execute(arg);
                    }
                } else {
                    Command cmd = commands.get(cmdName);
                    if (cmd == null) {
                        System.out.println("Команда не найдена в скрипте: " + cmdName);
                    } else {
                        cmd.execute();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось читать файл скрипта: " + e.getMessage());
        } finally {
            runningScripts.remove(fileName);
        }
    }
}
