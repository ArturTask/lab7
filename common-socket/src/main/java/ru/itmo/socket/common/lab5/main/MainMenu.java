package ru.itmo.socket.common.lab5.main;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.commands.impl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainMenu {

    private final Map<String, Command> commandMap = initializeCommands();

    private Map<String, Command> initializeCommands() {
        Map<String, Command> map = new HashMap<>();
        map.put("help", new HelpCommand());
        map.put("info", new InfoCommand());
        map.put("show", new ShowCommand());
        map.put("add", new AddCommand());
        map.put("update_id", new UpdateIdCommand());
        map.put("remove_by_id", new RemoveByIdCommand());
        map.put("clear", new ClearCommand());
        map.put("save", new SaveCommand());
        map.put("execute_script", new ExecuteScriptCommand());
        map.put("exit", new ExitCommand());
        map.put("add_if_max", new AddIfMaxCommand());
        map.put("remove_lower", new RemoveLowerCommand());
        map.put("history", new HistoryCommand());
        map.put("filter_less_than_minimal_point", new FilterLessThanMinimalPointCommand());
        map.put("print_descending", new PrintDescendingCommand());
        map.put("print_unique_author", new PrintUniqueAuthorCommand());
        return map;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в приложение! Введите 'help' для получения списка команд.");

        while (true) {
            System.out.print("> ");
            String commandName = scanner.nextLine().trim();
            Command command = commandMap.get(commandName);

            if (command != null) {
                command.execute();
                CommandHistory.addCommand(commandName);
                if (command instanceof ExitCommand) {
                    break;
                }
            } else {
                System.out.println("Команда не найдена! Введите 'help' для получения списка команд.");
            }
        }
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    // Статический метод для исполнения скрипта из файла.
    public static void executeScript(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner fileScanner = new Scanner(file);

        // Создаем новый экземпляр MainMenu для получения карты команд.
        MainMenu menu = new MainMenu();
        Map<String, Command> commandMap = menu.getCommandMap();

        while (fileScanner.hasNextLine()) {
            String commandLine = fileScanner.nextLine().trim();
            if (!commandLine.isEmpty()) {
                Command command = commandMap.get(commandLine);
                if (command != null) {
                    System.out.println("Выполнение команды: " + commandLine);
                    command.execute();
                } else {
                    System.out.println("Команда \"" + commandLine + "\" не найдена.");
                }
            }
        }
        fileScanner.close();
    }
}


