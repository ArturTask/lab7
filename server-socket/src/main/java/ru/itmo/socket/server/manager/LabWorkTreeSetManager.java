package ru.itmo.socket.server.manager;


import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.common.entity.Person;
import ru.itmo.socket.server.concurrent.UserContext;
import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.dao.LabWorksDao;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class LabWorkTreeSetManager {
    private final LabWorksDao labWorksDao = new LabWorksDao();
    private final UsersDao usersDao = new UsersDao();

    private static LabWorkTreeSetManager instance;
    // dbUserId -> Set<LabWork>
    private Map<Integer, SortedSet<LabWork>> labWorksMap;
    private LocalDateTime initializationTime;

    // Приватный конструктор для реализации singleton
    private LabWorkTreeSetManager() {
        this.labWorksMap = new HashMap<>();
        this.initializationTime = LocalDateTime.now();
    }

    // Глобальная точка доступа к экземпляру
    public static synchronized LabWorkTreeSetManager getInstance() {
        if (instance == null) {
            instance = new LabWorkTreeSetManager();
        }
        return instance;
    }

    @Deprecated // was used for save command
    public synchronized SortedSet<LabWork> getAllElements() {
        return getCollectionOfCurrentUser();
    }


    // Метод для получения информации о коллекции
    public synchronized String getCollectionInfo() {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        return "Тип коллекции: " + labWorks.getClass().getName() + "\n" +
                "Дата инициализации: " + initializationTime + "\n" +
                "Количество элементов: " + labWorks.size();
    }


    // Метод для получения строкового представления всех элементов коллекции
    public synchronized String getAllElementsAsString() {
        if (labWorksMap.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        labWorksMap.forEach((userId, labWorks) -> {
                    UserDto user = usersDao.findById(userId);
                    sb.append(("USER %s: %n" +
                            "%s%n" +
                            "----------------%n %n")
                            .formatted(user.getLogin(), labWorks));
                }
                );


        sb.append("\n]");
        return sb.toString();
    }

    // Добавление нового элемента в коллекцию
    public synchronized boolean add(LabWork element) {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        return trySaveUserToDb(element) && labWorks.add(element);
    }

    // Обновление элемента с указанным id: безопасное удаление старого элемента и добавление нового (с сохранённым id)
    public synchronized boolean updateById(long id, LabWork newElement) {
        // причем обновляем мы ТОЛЬКО свои элементы
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();

        Iterator<LabWork> iterator = labWorks.iterator();
        while (iterator.hasNext()) {
            LabWork lw = iterator.next();
            if (lw.getId() == id) {
                labWorksDao.remove(id);
                iterator.remove();
                newElement.setId(id);
                return tryUpdateInDb(newElement) && labWorks.add(newElement);
            }
        }
        return false;
    }


    // Удаление элемента по id
    public synchronized boolean removeById(long id) {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        boolean removeIf = labWorks.removeIf(lw -> lw.getId() == id);
        if (removeIf){
            labWorksDao.remove(id);
        }
        return removeIf;

    }

    // Очистка всей коллекции
    public synchronized void clear() {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        labWorks.clear();
    }

    // Добавление элемента, если он больше наибольшего элемента текущей коллекции
    public synchronized boolean addIfMax(LabWork element) {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        if (labWorks.isEmpty()) {
            return trySaveUserToDb(element) && labWorks.add(element);
        }
        LabWork max = labWorks.last(); // последний элемент — максимальный (TreeSet отсортирован)
        if (element.compareTo(max) > 0) {
            return trySaveUserToDb(element) && labWorks.add(element);
        }
        return false;
    }

    // Фильтрация элементов, у которых значение поля minimalPoint меньше заданного
    public synchronized List<LabWork> filterLessThanMinimalPoint(double minimalPoint) {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        return labWorks.stream()
                .filter(lw -> lw.getMinimalPoint() < minimalPoint)
                .collect(Collectors.toList());
    }

    // Метод для получения элементов в порядке убывания
    public synchronized String getElementsDescending() {
        TreeSet<LabWork> labWorks = (TreeSet<LabWork>) getCollectionOfCurrentUser();
        if (labWorks.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<LabWork> iterator = labWorks.descendingSet().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }


    // Получение уникальных значений поля author у всех элементов коллекции
    public synchronized Set<String> getUniqueAuthors() {
        SortedSet<LabWork> labWorks = getCollectionOfCurrentUser();
        return labWorks.stream()
                .map(LabWork::getAuthor)
                .filter(Objects::nonNull)
                .map(Person::getName) // преобразуем Person в String
                .collect(Collectors.toSet());
    }

    private boolean trySaveUserToDb(LabWork element) {
        try {
            labWorksDao.insert(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryUpdateInDb(LabWork element) {
        try {
            labWorksDao.update(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SortedSet<LabWork> getCollectionOfCurrentUser() {
        Integer dbUserId = UserContext.getDbUserId();
        SortedSet<LabWork> collection = labWorksMap.get(dbUserId);

        if (collection == null) {
            collection = new TreeSet<>();
            labWorksMap.put(dbUserId, collection);
        }
        return collection;
    }

    public void fetchInitialDataFromDb() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            List<UserDto> users = usersDao.findAll(connection);
            for (UserDto userDto : users) {
                List<LabWork> labWorks = labWorksDao.findAllByUserId(connection, userDto.getId());
                labWorksMap.put(userDto.getId(), new TreeSet<>(labWorks));
            }

            List<String> userRepresentation = users.stream()
                    .map(userDto -> "{id = %d, login = %s}".formatted(userDto.getId(), userDto.getLogin()))
                    .toList();

            System.out.println("Ура ура! Загружены элементы из БД для пользователей:"
                    + userRepresentation);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
