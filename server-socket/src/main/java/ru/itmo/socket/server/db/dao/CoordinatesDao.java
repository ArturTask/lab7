package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.server.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinatesDao {

    public int insert(Coordinates coordinates) throws SQLException {
        String sql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
            stmt.setFloat(1, coordinates.getX());
            stmt.setFloat(2, coordinates.getY());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("Failed to insert coordinates");
            }
        }
    }

    public Coordinates findById(int id) throws SQLException {
        String sql = "SELECT x, y FROM coordinates WHERE id = ?";
        try (PreparedStatement stmt = ConnectionManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coordinates coordinates = new Coordinates();
                coordinates.setX(rs.getFloat("x"));
                coordinates.setY(rs.getFloat("y"));
                return coordinates;
            } else {
                return null;
            }
        }
    }
}
