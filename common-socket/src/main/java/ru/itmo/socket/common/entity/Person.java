package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Person implements Serializable {
    private String name;
    private ZonedDateTime birthday;
    private float height;
    private Float weight;
    private Color eyeColor;

    public Person(String name, ZonedDateTime birthday, float height, Float weight, Color eyeColor) {
        setName(name);
        this.birthday = birthday;
        this.height = height;
        setWeight(weight);
        setEyeColor(eyeColor);
    }

    @Override
    public String toString() {
        String birthdayStr = (birthday != null) ? birthday.toLocalDate().toString() : "Не указана";

        return "name: \"" + name + "\"\n" +
                "birthday: " + birthdayStr + "\n" +
                "height: " + height + "\n" +
                "weight: " + weight + "\n" +
                "eyeColor: " + eyeColor + "\n";
    }

    public void setName(String name) {
        if (name.trim().equalsIgnoreCase("null") || name.trim().isEmpty())
            throw new IllegalArgumentException("Значение поля name не может быть равно null и не может быть пустым!");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public void setHeight(float height) {
        if (height <= 0)
            throw new IllegalArgumentException("Значение поля height должно быть больше 0!");
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setWeight(Float weight) {
        if (weight == null || weight <= 0)
            throw new IllegalArgumentException("Значение поля weight не должно быть равно null и должно быть больше 0!");
        this.weight = weight;
    }

    public Float getWeight() {
        return weight;
    }

    public void setEyeColor(Color eyeColor) {
        if (eyeColor == null)
            throw new IllegalArgumentException("Значение поля eyeColor не должно быть равно null!");
        this.eyeColor = eyeColor;
    }

    public Color getEyeColor() {
        return eyeColor;
    }
}
