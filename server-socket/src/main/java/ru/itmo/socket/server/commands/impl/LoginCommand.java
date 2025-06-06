package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.concurrent.UserContext;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class LoginCommand implements ServerCommand {

    UsersDao usersDao = new UsersDao();

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        UserDto userDto = (UserDto) args[0];

        if (UserContext.getAuthorized()) {
            oos.writeUTF("Пользователь уже авторизован login = " + UserContext.getLogin());
            return;
        }

        try {
            usersDao.findByUsername(userDto.getLogin());
            UserContext.setLogin(userDto.getLogin());
            UserContext.setAuthorized(true);
            oos.writeUTF(userDto.getLogin() + " залогинен!");
        } catch (SqlRequestException e) {
            oos.writeUTF("user с таким login не найден");
        }
    }
}
