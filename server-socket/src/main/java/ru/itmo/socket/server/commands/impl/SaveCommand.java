package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Команда для сохранения коллекции LabWork в JSON-файл.
 */
@Deprecated
public class SaveCommand implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String fileName = String.valueOf(args[0]);
        List<Flat> flats = Storage.getInstance().getAllElements();
        if (flats.isEmpty()) {
            oos.writeUTF("Коллекция пуста. Нечего сохранять.");
            return;
        }

        DateTimeFormatter zonedFmt = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        try (PrintWriter writer = new PrintWriter("flats.json")) {
            writer.println("["); // Открываем массив JSON
            for (int i = 0; i < flats.size(); i++) {
                writer.print("  " + flats.get(i).toJson()); // Записываем JSON-объект
                if (i < flats.size() - 1) writer.println(","); // Добавляем запятую между объектами
            }
            writer.println("\n]"); // Закрываем массив JSON
            oos.writeUTF("Коллекция успешно сохранена в файл: " + fileName);
        } catch (IOException e) {
            oos.writeUTF("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }

}


