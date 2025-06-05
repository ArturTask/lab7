package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class Coordinates implements Serializable {
    private float x;
    private float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    public void setX(float x) {
        if (x > 879)
            throw new IllegalArgumentException("Максимальное значение поля x: 879!");
        this.x = x;
    }
}
