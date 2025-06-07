package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class HelpCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        oos.writeUTF(
                "help : вывести справку по доступным командам\n" +
                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "add : добавить новый элемент в коллекцию\n" +
                        "update_id : обновить значение элемента коллекции, id которого равен заданному\n" +
                        "remove_by_id id : удалить элемент из коллекции по его id\n" +
                        "clear : очистить коллекцию\n" +
                        "save : сохранить коллекцию в файл\n" +
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index с 0)\n" +
                        "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданная площадь квартиры\n" +
                        "sort : отсортировать коллекцию по площади квартиры в порядке убывания\n" +
                        "group_counting_by_id : сгруппировать элементы коллекции по значению поля id, вывести количество элементов в каждой группе\n" +
                        "count_by_number_of_bathrooms numberOfBathrooms : вывести количество элементов, значение поля numberOfBathrooms которых равно заданному\n" +
                        "count_greater_than_central_heating centralHeating : вывести количество элементов, значение поля centralHeating которых больше заданного"
        );
    }
}