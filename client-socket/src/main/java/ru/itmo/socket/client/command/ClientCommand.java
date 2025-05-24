package ru.itmo.socket.client.command;

import java.util.Optional;
import java.util.Scanner;

/**
 * Interface representing Client command action - sometimes some AppCommand
 * needs extra param (e.g. when you add new entity, entity in this case is extra param, you need to input it through console and send to server)
 * <p></p>
 * by default parseParam returns empty optional which means no other params needed
 */
public interface ClientCommand {

    /**
     * Делает пре-обработку ЗАПРОСА на стороне клиента (это если нужно что-то например получить еще данные из консоли -
     * доп параметры (например создаем LabWork, значит нужно ввести LabWork)
     * а потом вернуть их как объект, который отправляется на сервер)
     */
    default Optional<Object> preProcess(Scanner scanner) {
        return Optional.empty();
    }
}
