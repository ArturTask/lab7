package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class CountGreaterThanCentralHeating implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {

        boolean targetHeating = Boolean.parseBoolean(String.valueOf(args[0]));
        long number = Storage.getInstance().countGreaterThanCentralHeating(targetHeating);

        if (number > -1) {
            oos.writeUTF("Количество квартир с центральным отоплением лучше чем, у квартиры с отоплением:" + targetHeating + "Найдено:" + number);
        } else {
            oos.writeUTF("Произошла ошибка, повторите запрос!");
        }
    }
}
