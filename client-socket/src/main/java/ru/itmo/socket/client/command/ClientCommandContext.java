package ru.itmo.socket.client.command;


import ru.itmo.socket.client.command.impl.*;
import ru.itmo.socket.common.command.AppCommand;

import java.util.HashMap;
import java.util.Map;

import static ru.itmo.socket.common.command.AppCommand.*;
import static ru.itmo.socket.common.command.AppCommand.PRINT_UNIQUE_AUTHOR;

/**
 * Some commands require input values,
 * others - not (they  have DefaultCommand without input values)
 */
public class ClientCommandContext {

    private static final Map<AppCommand, ClientCommand> commandMap = initializeCommands();

    private static Map<AppCommand, ClientCommand> initializeCommands() {
        Map<AppCommand, ClientCommand> map = new HashMap<>();

        map.put(HELP, new DefaultCommand());
        map.put(INFO, new DefaultCommand());
        map.put(SHOW, new DefaultCommand());
        map.put(ADD, new AddCommand());
        map.put(UPDATE_ID, new UpdateByIdCommand());
        map.put(REMOVE_BY_ID, new RemoveByIdCommand());
        map.put(CLEAR, new DefaultCommand());
        map.put(SAVE, new SaveCommand());
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(ADD_IF_MAX, new AddIfMaxCommand());
        map.put(HISTORY, new DefaultCommand());
        map.put(FILTER_LESS_THAN_MINIMAL_POINT, new FilterLessThanMinimalPointCommand());
        map.put(PRINT_DESCENDING, new DefaultCommand());
        map.put(PRINT_UNIQUE_AUTHOR, new DefaultCommand());
        map.put(DISCONNECT_CLIENT, new DisconnectClientCommand());
        return map;
    }

    public static ClientCommand getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }
}
