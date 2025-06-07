package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.Storage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class CountByNumberOfBathrooms implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {

        long rooms = Long.parseLong(String.valueOf(args[0]));
        long number = Storage.getInstance().countByNumberOfBathrooms(rooms);

        if (number > 0) {
            oos.writeUTF("Количество квартир с ванными комнатами в количестве:" + rooms + " найдено:" + number);
        } else {
            oos.writeUTF("Квартиры с ванными комнатами в количестве:"+ rooms +" не найдены!");
        }
    }
}
