package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RegisterCommand implements ServerCommand {
    UsersDao usersDao = new UsersDao();

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        UserDto userDto = (UserDto) args[0];
        try {
            usersDao.insert(userDto);
            oos.writeUTF(userDto.getLogin() + " зарегистрирован!");
        } catch (SqlRequestException e) {
            oos.writeUTF("user с таким login уже есть");
        }
    }
}
