package ru.itmo.socket.server.commands;

import ru.itmo.socket.common.command.AppCommand;
import ru.itmo.socket.server.commands.impl.*;

import java.util.HashMap;
import java.util.Map;

import static ru.itmo.socket.common.command.AppCommand.*;

public class CommandsContext {

    private static final Map<AppCommand, Command> commandMap = initializeCommands();

    private static Map<AppCommand, Command> initializeCommands() {
        Map<AppCommand, Command> map = new HashMap<>();
        map.put(HELP, new HelpCommand());
        map.put(INFO, new InfoCommand());
        map.put(SHOW, new ShowCommand());
        map.put(ADD, new AddCommand());
        map.put(UPDATE_ID, new UpdateIdCommand());
        map.put(REMOVE_BY_ID, new RemoveByIdCommand());
        map.put(CLEAR, new ClearCommand());
        map.put(SAVE, new SaveCommand());
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(ADD_IF_MAX, new AddIfMaxCommand());
        map.put(REMOVE_LOWER, new RemoveLowerCommand());
        map.put(HISTORY, new HistoryCommand());
        map.put(FILTER_LESS_THAN_MINIMAL_POINT, new FilterLessThanMinimalPointCommand());
        map.put(PRINT_DESCENDING, new PrintDescendingCommand());
        map.put(PRINT_UNIQUE_AUTHOR, new PrintUniqueAuthorCommand());
        return map;
    }

    public static Command getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }


}
