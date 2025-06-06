package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.concurrent.DbContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDao {

    public UserDto insert(UserDto user) {
        String sql = "INSERT INTO users (login, password_hash) VALUES (?, ?) RETURNING id";
        try (PreparedStatement st = DbContext.getConnection().prepareStatement(sql)) {
            st.setString(1, user.getLogin());
            st.setString(2, user.getPassword());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new UserDto(rs.getInt("id"), user.getLogin(), user.getPassword());
            } else {
                throw new SQLException("insert не добавил пользователя " + user);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public UserDto findByUsername(String login) {
        String sql = "SELECT id, password_hash FROM users WHERE login = ?";
        try (PreparedStatement st = DbContext.getConnection().prepareStatement(sql)) {
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) throw new SqlRequestException("User с login %s не найден".formatted(login));
            return new UserDto(rs.getInt("id"), login, rs.getString("password_hash"));
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}
