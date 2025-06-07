package ru.itmo.socket.common.data;

import java.io.Serializable;

public enum View implements Serializable {
    YARD,
    BAD,
    NORMAL,
    GOOD;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var characteristics : values()) {
            nameList.append(characteristics.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}