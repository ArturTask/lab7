package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinatesDao {

    public Coordinates insert(Coordinates coordinates) {
        String sql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setFloat(1, coordinates.getX());
            st.setFloat(2, coordinates.getY());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                // можно сохранить id
                return coordinates;
            } else {
                throw new SQLException("insert не добавил координаты " + coordinates);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Coordinates findById(int id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public Coordinates findById(Connection connection, int id) {
        String sql = "SELECT x, y FROM coordinates WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;
            Coordinates coords = new Coordinates();
            coords.setX(rs.getFloat("x"));
            coords.setY(rs.getFloat("y"));
            return coords;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public void update(long id, Coordinates coordinates) {
        String sql = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setFloat(1, coordinates.getX());
            st.setFloat(2, coordinates.getY());
            st.setLong(3, id);
            int updated = st.executeUpdate();
            if (updated == 0) throw new SQLException("update не обновил координаты id=" + id);
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}
