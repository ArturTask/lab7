package ru.itmo.socket.server.manager;

import ru.itmo.socket.common.entity.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Загрузчик коллекции LabWork из XML-файла при запуске программы.
 * Размещается в отдельном пакете loader, чтобы не конфликтовать с остальными классами.
 */
public class XmlCollectionLoader {
    private final LabWorkTreeSetManager manager;
    private final String xmlFilePath;

    public XmlCollectionLoader(LabWorkTreeSetManager manager, String xmlFilePath) {
        this.manager = manager;
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * Загружает все элементы <LabWork> из указанного XML и добавляет их в менеджер.
     */
    public void load() {
        try {
            File xmlFile = new File(xmlFilePath);
            if (!xmlFile.exists()) {
                System.out.println("XML-файл не найден: " + xmlFilePath);
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("LabWork");
            DateTimeFormatter zonedFmt = DateTimeFormatter.ISO_ZONED_DATE_TIME;

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element elem = (Element) node;
                LabWork lw = parseLabWork(elem, zonedFmt);
                manager.add(lw);
            }

            System.out.println("Загружено элементов: " + nodes.getLength());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Разбирает один узел <LabWork> в объект LabWork.
     * Использует конструктор модели и метод setId, чтобы сохранить исходный id.
     */
    private LabWork parseLabWork(Element e, DateTimeFormatter zonedFmt) {
        long id = Long.parseLong(getText(e, "id"));
        String name = getText(e, "name");
        long minimalPoint = Long.parseLong(getText(e, "minimalPoint"));
        Difficulty difficulty = Difficulty.valueOf(getText(e, "difficulty"));

        // Coordinates
        Element coordsEl = (Element) e.getElementsByTagName("coordinates").item(0);
        float x = Float.parseFloat(getText(coordsEl, "x"));
        float y = Float.parseFloat(getText(coordsEl, "y"));
        Coordinates coords = new Coordinates(x, y);

        // Автор
        Element authEl = (Element) e.getElementsByTagName("author").item(0);
        String authorName = getText(authEl, "name");
        ZonedDateTime birthday = ZonedDateTime.parse(getText(authEl, "birthday"), zonedFmt);
        float height = Float.parseFloat(getText(authEl, "height"));
        Float weight = Float.parseFloat(getText(authEl, "weight"));
        Color eyeColor = Color.valueOf(getText(authEl, "eyeColor"));
        Person author = new Person(authorName, birthday, height, weight, eyeColor);

        // Создаём объект через конструктор модели
        LabWork lw = new LabWork(name, coords, minimalPoint, difficulty, author);
        lw.setId(id);
        // Если нужно сохранять оригинальную дату создания, то модель надо расширить.

        return lw;
    }

    private String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        return nl.item(0).getTextContent().trim();
    }
}
