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
//        map.put(SAVE, new SaveCommand()); Deprecated!
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(ADD_IF_MAX, new AddIfMaxCommand());
        map.put(HISTORY, new HistoryCommand());
        map.put(FILTER_LESS_THAN_MINIMAL_POINT, new FilterLessThanMinimalPointCommand());
        map.put(PRINT_DESCENDING, new PrintDescendingCommand());
        map.put(PRINT_UNIQUE_AUTHOR, new PrintUniqueAuthorCommand());
        map.put(LOGIN, new LoginCommand());
        map.put(REGISTER, new RegisterCommand());
        return map;
    }

    public static ServerCommand getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }


}
