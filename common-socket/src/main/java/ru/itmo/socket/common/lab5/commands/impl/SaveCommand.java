package ru.itmo.socket.common.lab5.commands.impl;

import ru.itmo.socket.common.lab5.commands.Command;
import ru.itmo.socket.common.lab5.manager.LabWorkTreeSetManager;
import ru.itmo.socket.common.lab5.model.LabWork;
import ru.itmo.socket.common.lab5.model.Person;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Команда для сохранения коллекции LabWork в XML-файл.
 */
public class SaveCommand implements Command {

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя файла для сохранения коллекции: ");
        String fileName = scanner.nextLine().trim();

        TreeSet<LabWork> labWorks = LabWorkTreeSetManager.getInstance().getAllElements();
        if (labWorks.isEmpty()) {
            System.out.println("Коллекция пуста. Нечего сохранять.");
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
            System.out.println("Коллекция успешно сохранена в файл: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении коллекции: " + e.getMessage());
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


