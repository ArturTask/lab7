package ru.itmo.socket.server;

import ru.itmo.socket.server.commands.impl.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainMenu {



//    // Статический метод для исполнения скрипта из файла.
//    public static void executeScript(String fileName) throws FileNotFoundException {
//        File file = new File(fileName);
//        Scanner fileScanner = new Scanner(file);
//
//        // Создаем новый экземпляр MainMenu для получения карты команд.
//        MainMenu menu = new MainMenu();
//        Map<String, Command> commandMap = menu.getCommandMap();
//
//        while (fileScanner.hasNextLine()) {
//            String commandLine = fileScanner.nextLine().trim();
//            if (!commandLine.isEmpty()) {
//                Command command = commandMap.get(commandLine);
//                if (command != null) {
//                    System.out.println("Выполнение команды: " + commandLine);
//                    command.execute();
//                } else {
//                    System.out.println("Команда \"" + commandLine + "\" не найдена.");
//                }
//            }
//        }
//        fileScanner.close();
//    }
}


