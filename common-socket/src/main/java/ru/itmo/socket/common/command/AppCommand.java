package ru.itmo.socket.common.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.itmo.socket.common.exception.AppCommandNotFoundException;

import java.util.Arrays;

/**
 * Все доступные команды для приложения
 */
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
    ADD_IF_MAX("add_if_max"),
    HISTORY("history"),
    FILTER_LESS_THAN_MINIMAL_POINT("filter_less_than_minimal_point"),
    PRINT_DESCENDING("print_descending"),
    PRINT_UNIQUE_AUTHOR("print_unique_author"),
    ;

    private final String value;

    /**
     *
     * @param commandName string name of command from console
     * @return enum value of command
     */

    public static AppCommand getByStringValue(String commandName) {
        return Arrays.stream(AppCommand.values())
                .filter(c -> c.getValue().equalsIgnoreCase(commandName))
                .findFirst()
                .orElseThrow(() -> new AppCommandNotFoundException("command " + commandName + " not found"))
        ;
    }

}
