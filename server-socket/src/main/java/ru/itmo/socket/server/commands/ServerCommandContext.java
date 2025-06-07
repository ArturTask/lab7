package ru.itmo.socket.server.commands;

import ru.itmo.socket.common.command.AppCommand;
import ru.itmo.socket.server.commands.impl.*;

import java.util.HashMap;
import java.util.Map;

import static ru.itmo.socket.common.command.AppCommand.*;

public class ServerCommandContext {

    private static final Map<AppCommand, ServerCommand> commandMap = initializeCommands();

    private static Map<AppCommand, ServerCommand> initializeCommands() {
        Map<AppCommand, ServerCommand> map = new HashMap<>();
        map.put(HELP, new HelpCommand());
        map.put(INFO, new InfoCommand());
        map.put(SHOW, new ShowCommand());
        map.put(ADD, new AddCommand());
        map.put(UPDATE_ID, new UpdateByIdCommand());
        map.put(REMOVE_BY_ID, new RemoveByIdCommand());
        map.put(CLEAR, new ClearCommand());
//        map.put(SAVE, new SaveCommand());
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(REMOVE_AT, new RemoveAt());
        map.put(REMOVE_LOVER, new RemoveLower());
        map.put(SORT, new Sort());
        map.put(GROUP_COUNTING_BY_ID, new GroupContingById());
        map.put(COUNT_BY_NUMBER_OF_BATHROOMS, new CountByNumberOfBathrooms());
        map.put(COUNT_GREATER_THAN_CENTRAL_HEATING, new CountGreaterThanCentralHeating());
//        map.put(DISCONNECT_CLIENT, new DisconnectClientCommand());
        map.put(LOGIN, new LoginCommand());
        map.put(REGISTER, new RegisterCommand());
        return map;
    }

    public static ServerCommand getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }


}
