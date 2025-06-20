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
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "add_if_max : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                        "history : вывести последние 6 команд (без их аргументов)\n" +
                        "filter_less_than_minimal_point : вывести элементы, значение поля minimalPoint которых меньше заданного\n" +
                        "print_descending : вывести элементы коллекции в порядке убывания\n" +
                        "print_unique_author : вывести уникальные значения поля author всех элементов в коллекции"

        );
    }
}