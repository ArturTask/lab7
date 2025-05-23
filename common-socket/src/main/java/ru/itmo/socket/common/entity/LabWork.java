package ru.itmo.socket.common.entity;

import java.time.LocalDate;

public class LabWork implements Comparable<LabWork> {
    private static long lastGeneratedId = 0;
    private long id;
    private String name;
    private Coordinates coordinates;
    private long minimalPoint;
    private Difficulty difficulty;
    private Person author;
    private final LocalDate creationDate;


    public LabWork(String name, Coordinates coordinates, long minimalPoint, Difficulty difficulty, Person author) {
        this.id = generateId(); // Автоматическая генерация id при создании объекта
        setName(name);
        this.creationDate = LocalDate.now();
        setCoordinates(coordinates);
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        setAuthor(author);
    }

    private long generateId() {
        return ++lastGeneratedId;
    }

    @Override
    public String toString() {

        String difficultyStr = (difficulty != null) ? difficulty.toString() : "Не указана";

        return "\n" +
                "id: " + id + "\n" +
                "name: \"" + name + "\"\n" +
                "coordinates: " + coordinates + "\n" +
                "created: " + creationDate + "\n" +
                "minimalPoint: " + minimalPoint + "\n" +
                "difficulty: " + difficultyStr + "\n" +
                "author: " + author;
    }


    @Override
    public int compareTo(LabWork labWork) {
        return Long.compare(this.minimalPoint, labWork.minimalPoint);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        if (name.trim().equalsIgnoreCase("null") || name.trim().isEmpty())
            throw new IllegalArgumentException("Значение поля name не может быть равно null и не может быть пустым!");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("Значение поля coordinates не может быть равно null!");
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setMinimalPoint(long minimalPoint) {
        if (minimalPoint <= 0)
            throw new IllegalArgumentException("Значение поля minimalPoint должно быть больше 0!");
        this.minimalPoint = minimalPoint;
    }

    public long getMinimalPoint() {
        return minimalPoint;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setAuthor(Person author) {
        if (author == null)
            throw new IllegalArgumentException("Значение поля author не может быть равно null!");
        this.author = author;
    }

    public Person getAuthor() {
        return author;
    }
}

