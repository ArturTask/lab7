package ru.itmo.socket.common.data;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class House implements Serializable {
    private String name; //Поле не может быть null
    private int year; //Значение поля должно быть больше 0
    private long numberOfFloors; //Значение поля должно быть больше 0
    private Long numberOfFlatsOnFloor; //Значение поля должно быть больше 0
    private Long numberOfLifts; //Значение поля должно быть больше 0


    public House(String name, int year, long numberOfFloors, Long numberOfFlatsOnFloor, Long numberOfLifts){
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        this.numberOfLifts = numberOfLifts;
    }

    public boolean validate() {
        if (name == null ) return false;
        if (year <= 0) return false;
        if (numberOfFloors <= 0) return false;
        if (numberOfFlatsOnFloor <= 0) return false;
        if (numberOfLifts <= 0) return false;
        return true;
    }

    @Override
    public String toString(){
        return "house{\"name\": " + name + ", " +
                "\"year\": \"" + year + "\", " +
                "\"numberOfFloors\": \"" + numberOfFloors + "\", " +
                "\"numberOfFlatsOnFloor\": \"" + numberOfFlatsOnFloor + "\", " +
                "\"numberOfLifts\": \"" + numberOfLifts + "\"";
    }

}