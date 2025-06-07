package ru.itmo.socket.server.manager;

import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.concurrent.UserContext;
import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.dao.FlatDao;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Storage {
    private static Storage instance;

    private final FlatDao flatDao = new FlatDao();
    private final UsersDao usersDao = new UsersDao();

    private final Map<Integer, List<Flat>> objectsMap;
    private final Date initializationDate;
    private LocalDateTime lastSaveTime;

    private Storage() {
        objectsMap = new HashMap<>();
        initializationDate = new Date(); // Сохраняем текущую дату и время
        lastSaveTime = null;

    }

    public boolean add(Flat element) {
        List<Flat> list = getCollectionOfCurrentUser();
        if (!trySaveUserToDb(element)) return false;
        list.add(element);
        return true;
    }

    // Очистка всей коллекции
    public void clear() {
        List<Flat> list = getCollectionOfCurrentUser();
        list.clear();
    }

    // Метод для получения информации о коллекции
    public String getCollectionInfo() {
        List<Flat> list = getCollectionOfCurrentUser();
        return "Тип коллекции: " + list.getClass().getName() + "\n" +
                "Дата инициализации: " + getInitializationDate() + "\n" +
                "Количество элементов: " + list.size();
    }

    // Удаление элемента по id
    public boolean removeById(long id) {
        List<Flat> list = getCollectionOfCurrentUser();
        return list.removeIf(lw -> lw.getId() == id);
    }

    // Удаление элемента
    public boolean remove(Flat flat) {
        List<Flat> list = getCollectionOfCurrentUser();
        return list.remove(flat);
    }

    public List<Flat> getAllElements() {
        return getCollectionOfCurrentUser();
    }

    // Метод для получения строкового представления всех элементов коллекции
    public String getAllElementsAsString() {
        if (objectsMap.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        objectsMap.forEach((userId, list) -> {
                    UserDto user = usersDao.findById(userId);
                    sb.append(("USER %s: %n" +
                            "%s%n" +
                            "----------------%n %n")
                            .formatted(user.getLogin(), list));
                }
        );


        sb.append("\n]");
        return sb.toString();
    }

    // Обновление элемента с указанным id: безопасное удаление старого элемента и добавление нового (с сохранённым id)
    public boolean updateById(long id, Flat newElement) {
        List<Flat> list = getCollectionOfCurrentUser();

        Iterator<Flat> iterator = list.iterator();
        while (iterator.hasNext()) {
            Flat f = iterator.next();
            if (f.getId() == id) {
                iterator.remove();
                newElement.setId(id);
                if (!tryUpdateInDb(newElement)) return false;
                list.add(newElement);
                return true;
            }
        }
        return false;
    }

    // Фильтрация элементов, у которых значение поля minimalPoint меньше заданного
    public List<Flat> filterLessThanArea(double area) {
        List<Flat> list = getCollectionOfCurrentUser();
        return list.stream()
                .filter(f -> f.getArea() < area)
                .collect(Collectors.toList());
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public List<Flat> getList() {
        List<Flat> list = getCollectionOfCurrentUser();
        return list;
    }

    public String getInitializationDate() {
        return "Дата инициализации: " + initializationDate;
    }

    public boolean containsId(Long id) {
        List<Flat> list = getCollectionOfCurrentUser();
        return list.stream().anyMatch(flat -> flat.getId().equals(id));
    }

    public void sort() {
        List<Flat> list = getCollectionOfCurrentUser();
        list.sort(Comparator.comparing(Flat::getArea).thenComparing(Flat::getNumberOfRooms).reversed());
    }

    public Map<Long, Long> groupCountingById(long id) {
        List<Flat> list = getCollectionOfCurrentUser();
        // Группируем по id % 10
        Map<Long, Long> grouped = list.stream()
                .collect(Collectors.groupingBy(f -> id % 10, Collectors.counting()));

        return grouped;

    }

    public long countByNumberOfBathrooms(long targetBathrooms) {
        List<Flat> list = getCollectionOfCurrentUser();
        try {
            long count = list.stream()
                    .filter(flat -> flat.getNumberOfBathrooms() == targetBathrooms) // Фильтруем квартиры по количеству ванных
                    .count(); // Считаем количество

            return count;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long countGreaterThanCentralHeating(boolean targetHeating) {
        List<Flat> list = getCollectionOfCurrentUser();
        try {
            long count;
            if (!targetHeating) {
                // Если введён "false", считаем количество квартир с centralHeating == true
                count = list.stream()
                        .filter(Flat::isCentralHeating)
                        .count();
                return count;
            } else {
                // Если введён "true", нет объектов, у которых centralHeating > true
                count = 0;
                return count;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void saveCollection() {
        lastSaveTime = LocalDateTime.now();
    }


    private boolean trySaveUserToDb(Flat element) {
        try {
            flatDao.insert(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryUpdateInDb(Flat element) {
        try {
            flatDao.update(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Flat> getCollectionOfCurrentUser() {
        Integer dbUserId = UserContext.getDbUserId();
        List<Flat> collection = objectsMap.get(dbUserId);

        if (collection == null) {
            collection = new LinkedList<>();
            objectsMap.put(dbUserId, collection);
        }
        return collection;
    }


    public void fetchInitialDataFromDb() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            List<UserDto> users = usersDao.findAll(connection);
            for (UserDto userDto : users) {
                List<Flat> objects = flatDao.findAllByUserId(connection, userDto.getId());
                objectsMap.put(userDto.getId(), new LinkedList<>(objects));
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
