package ru.itmo.socket.server.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 */
public interface ServerCommand {
    void execute(ObjectOutputStream oos, Object... args) throws IOException;

    /**
     * method returning number of lines to send (by some command) to client (1 by default)
     */
    default int getNumberOfOutputLines(Object... args){
        return 1;
    }

    /**
     *
     * @return Тип аргумента (если аргумент используется в команде) это нужно для команды execute_script
     * чтобы команда прочитала из файла arg (аргумент) для команды В ФАЙЛЕ
     */
    default Class<?> getArgType() {
        return String.class; // по умолчанию String (вообще может и не быть параметров)
    }
}