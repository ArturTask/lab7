package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

/**
 *
 */
public class DefaultCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        return ClientCommand.super.preProcess(scanner);
    }
}
