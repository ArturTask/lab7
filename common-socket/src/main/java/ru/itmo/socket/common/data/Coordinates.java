package ru.itmo.socket.common.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class Coordinates implements Serializable {
    private float x;
    private Float y; //Максимальное значение поля: 685, Поле не может быть null

    public Coordinates(float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public void setY(Float y) {
        if (y > 685 || y == null)
            throw new IllegalArgumentException ("Максимальное значение поля x: 685!");
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

}