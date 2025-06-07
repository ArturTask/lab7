package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.client.util.SecurityUtil;
import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.common.util.FlatInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class RegisterCommand implements ClientCommand {
    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        UserDto userDto = FlatInputHelper.readUser(scanner);
        // hash password
        userDto.setPassword(SecurityUtil.hashPassword(userDto.getPassword()));
        return Optional.of(userDto);
    }
}
