package ru.itmo.socket.common.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Flat implements Comparable<Flat>, Serializable{

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double area; //Значение поля должно быть больше 0
    private long numberOfRooms; //Значение поля должно быть больше 0
    private Integer numberOfBathrooms; //Поле не может быть null, Значение поля должно быть больше 0
    private boolean centralHeating;
    private View view; //Поле не может быть null
    private House house; //Поле не может быть null


    public Flat(String name, Coordinates coordinates, ZonedDateTime creationDate, Double area, long numberOfRooms, Integer numberOfBathrooms, boolean centralHeating, View view, House house){
        this.id = -1L;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.centralHeating = centralHeating;
        this.view = view;
        this.house = house;
    }

    public Flat(String name, Coordinates coordinates, Double area, long numberOfRooms, Integer numberOfBathrooms, boolean centralHeating, View view, House house){
        this(name, coordinates,ZonedDateTime.now(), area, numberOfRooms, numberOfBathrooms, centralHeating, view, house);
    }

    public Flat(){
        this.creationDate = ZonedDateTime.now();
    }

    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (area <= 0) return false;
        if (numberOfRooms <= 0) return false;
        if (numberOfBathrooms == null || numberOfBathrooms <= 0) return false;
        if (view == null) return false;
        if (house == null || !house.validate()) return false;
        return true;
    }

    @Override
    public int compareTo(Flat flat) {
        return Long.compare(this.id, flat.id);
    }

    // Метод для преобразования объекта в JSON-строку
    public String toJson() {
        return String.format("""
            {
              "id": %d,
              "name": "%s",
              "coordinates": {"x": %s, "y": %s},
              "creationDate": "%s",
              "area": %d,
              "numberOfRooms": %d,
              "numberOfBathrooms": %d,
              "centralHeating": %b,
              "view": "%s",
              "house": {
                "name": "%s",
                "year": %d,
                "numberOfFloors": %d,
                "numberOfFlatsOnFloor": %d,
                "numberOfLifts": %d
              }
            }
            """,
                id, name, coordinates.getX(), coordinates.getY(), creationDate,
                area, numberOfRooms, numberOfBathrooms, centralHeating, view,
                house.getName(), house.getYear(), house.getNumberOfFloors(),
                house.getNumberOfFlatsOnFloor(), house.getNumberOfLifts());
    }

    @Override
    public String toString(){
        return "\nflat{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"creationDate\": \"" + creationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "\", " +
                "\"area\": \"" + area + "\", " +
                "\"numberOfRooms\": \"" + numberOfRooms + "\", " +
                "\"numberOfBathrooms\": \"" + numberOfBathrooms + "\", " +
                "\"centralHeating\": \"" + centralHeating + "\", " +
                "\"view\": \"" + view + "\", " +
                "\"house\": \"" + house + "\", ";
    }


    public static Flat generateDefault() {
        Flat flat = new Flat();
        flat.setName("default flat");
        flat.setCreationDate(ZonedDateTime.now());
        flat.setArea(1.0);
        flat.setNumberOfRooms(1);
        flat.setNumberOfBathrooms(1);
        flat.setCentralHeating(false);
        flat.setView(View.NORMAL);

        Coordinates coords = new Coordinates();
        coords.setX(0f);
        coords.setY(0f);
        flat.setCoordinates(coords);

        House house = new House();
        house.setName("default house");
        house.setYear(2000);
        house.setNumberOfFloors(1);
        house.setNumberOfFlatsOnFloor(1L);
        house.setNumberOfLifts(0L);
        flat.setHouse(house);

        return flat;
    }

}

