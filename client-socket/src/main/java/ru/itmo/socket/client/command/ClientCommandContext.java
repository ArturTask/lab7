package ru.itmo.socket.client.command;


import ru.itmo.socket.client.command.impl.*;
import ru.itmo.socket.common.command.AppCommand;

import java.util.HashMap;
import java.util.Map;

import static ru.itmo.socket.common.command.AppCommand.*;

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
//        map.put(SAVE, new SaveCommand());
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(REMOVE_AT, new RemoveAt());
        map.put(REMOVE_LOVER, new RemoveLower());
        map.put(SORT, new DefaultCommand());
        map.put(GROUP_COUNTING_BY_ID, new GroupContingById());
        map.put(COUNT_BY_NUMBER_OF_BATHROOMS, new CountByNumberOfBathrooms());
        map.put(COUNT_GREATER_THAN_CENTRAL_HEATING, new CountGreaterThanCentralHeating());
//        map.put(DISCONNECT_CLIENT, new DisconnectClientCommand());
        map.put(LOGIN, new LoginCommand());
        map.put(REGISTER, new RegisterCommand());
        return map;
    }

    public static ClientCommand getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }
}
