package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.common.entity.Person;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Команда для сохранения коллекции LabWork в XML-файл.
 */
@Deprecated
public class SaveCommand implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String fileName = String.valueOf(args[0]);
        SortedSet<LabWork> labWorks = LabWorkTreeSetManager.getInstance().getAllElements();
        if (labWorks.isEmpty()) {
            oos.writeUTF("Коллекция пуста. Нечего сохранять.");
            return;
        }

        DateTimeFormatter zonedFmt = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<LabWorks>\n");
            for (LabWork lw : labWorks) {
                writer.write("  <LabWork>\n");
                writer.write("    <id>" + lw.getId() + "</id>\n");
                writer.write("    <name>" + escapeXml(lw.getName()) + "</name>\n");
                writer.write("    <minimalPoint>" + lw.getMinimalPoint() + "</minimalPoint>\n");
                writer.write("    <difficulty>" + lw.getDifficulty() + "</difficulty>\n");
                writer.write("    <coordinates>\n");
                writer.write("      <x>" + lw.getCoordinates().getX() + "</x>\n");
                writer.write("      <y>" + lw.getCoordinates().getY() + "</y>\n");
                writer.write("    </coordinates>\n");
                Person author = lw.getAuthor();
                writer.write("    <author>\n");
                writer.write("      <name>" + escapeXml(author.getName()) + "</name>\n");
                writer.write("      <birthday>" + author.getBirthday() == null ? null : author.getBirthday().format(zonedFmt) + "</birthday>\n");
                writer.write("      <height>" + author.getHeight() + "</height>\n");
                writer.write("      <weight>" + author.getWeight() + "</weight>\n");
                writer.write("      <eyeColor>" + author.getEyeColor() + "</eyeColor>\n");
                writer.write("    </author>\n");
                writer.write("  </LabWork>\n");
            }
            writer.write("</LabWorks>\n");
            oos.writeUTF("Коллекция успешно сохранена в файл: " + fileName);
        } catch (IOException e) {
            oos.writeUTF("Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }

    /**
     * Экранирует специальные XML-символы в строке.
     */
    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}


