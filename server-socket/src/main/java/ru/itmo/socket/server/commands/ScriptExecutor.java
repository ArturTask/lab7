package ru.itmo.socket.server.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.itmo.socket.server.commands.impl.ExecuteScriptCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ScriptExecutor {
    private static final Set<String> runningScripts = new HashSet<>();

    /**
     * Исполняет файл-скрипт построчно.
     * Запрещает повторный запуск уже запущенного файла (рекурсию).
     */
    public static void execute(ObjectOutputStream oos, String fileName) throws IOException, URISyntaxException {
        if (runningScripts.contains(fileName)) {
            System.out.println("Ошибка: рекурсивный вызов скрипта '" + fileName + "' обнаружен. Выполнение прекращено.");
            return;
        }

        URL resource = ScriptExecutor.class.getClassLoader().getResource(fileName);
        if (resource == null || !Files.exists(Paths.get(resource.toURI()))) {
            System.out.println("Файл скрипта не найден: " + fileName);
            return;
        }

        File file = new File(resource.getFile());

        runningScripts.add(fileName);
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String cmdName = parts[0];
                String rawArgFromFile = parts.length > 1 ? parts[1] : null;

                ServerCommand cmd = ServerCommandContext.getCommand(cmdName);
                Object objArg = rawArgFromFile;

                if (rawArgFromFile!=null) {
                    // create json object mapper + configure to read ZonedDateTime
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JavaTimeModule());
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                    // map if not string argument!
                    Class<?> argType = cmd.getArgType();
                    if (argType != String.class) {
                        objArg = mapper.readValue(rawArgFromFile, argType);
                    }
                }

                // recursive execute_script
                if (cmd instanceof ExecuteScriptCommand) {
                    if (rawArgFromFile == null) {
                        System.out.println("Ошибка: имя файла не указано в команде execute_script.");
                    } else {
                        ScriptExecutor.execute(oos, rawArgFromFile);
                    }
                }
                // other commands
                else {

                    if (cmd == null) {
                        System.out.println("Команда не найдена в скрипте: " + cmdName);
                    } else {
                        cmd.execute(oos, objArg);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось читать файл скрипта: " + e.getMessage());
        } finally {
            runningScripts.remove(fileName);
        }
    }

    /**
     * number of not empty commands in script
     */
    public static int countNumberOfCommands(String fileName) {

        try {
            URL resource = ScriptExecutor.class.getClassLoader().getResource(fileName);
            if (resource == null || !Files.exists(Paths.get(resource.toURI()))) {
                System.out.println("Файл скрипта не найден: " + fileName);
                return 0;
            }

            Scanner scanner = new Scanner(new File(resource.toURI()));

            int count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // trim чтобы убрать пробелы и табы
                if (!line.isEmpty()) {
                    count++;
                }
            }

            scanner.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
