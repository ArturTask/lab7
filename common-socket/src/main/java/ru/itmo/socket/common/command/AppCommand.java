package ru.itmo.socket.common.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.itmo.socket.common.exception.AppCommandNotFoundException;

import java.util.Arrays;


//Все доступные команды для приложения

@AllArgsConstructor
@Getter
public enum AppCommand {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE_ID("update_id"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    REMOVE_AT("remove_at"),
    REMOVE_LOVER("remove_lower"),
    SORT("sort"),
    GROUP_COUNTING_BY_ID("group_counting_by_id"),
    COUNT_BY_NUMBER_OF_BATHROOMS("count_by_number_of_bathrooms"),
    COUNT_GREATER_THAN_CENTRAL_HEATING("count_greater_than_central_heating"),
    DISCONNECT_CLIENT("disconnect");

    private final String value;

    public static AppCommand getByStringValue(String commandName) {
        return Arrays.stream(AppCommand.values())
                .filter(c -> c.getValue().equalsIgnoreCase(commandName))
                .findFirst()
                .orElseThrow(() -> new AppCommandNotFoundException("command " + commandName + " not found"))
                ;
    }

}
