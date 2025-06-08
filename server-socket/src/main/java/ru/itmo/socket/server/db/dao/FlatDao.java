package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.data.Coordinates;
import ru.itmo.socket.common.data.Flat;
import ru.itmo.socket.common.data.House;
import ru.itmo.socket.common.data.View;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class FlatDao {

    private final CoordinatesDao coordinatesDao = new CoordinatesDao();
    private final HouseDao houseDao = new HouseDao();

    public Flat insert(Flat flat, int userId) {
        try {
            // persist nested objects first
            Coordinates savedCoord = coordinatesDao.insert(flat.getCoordinates());
            House savedHouse = houseDao.insert(flat.getHouse());

            String sql = """
                INSERT INTO flats (name, coordinates_id, area, number_of_rooms, number_of_bathrooms,
                                    central_heating, view, house_id, user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id
            """;
            try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
                st.setString(1, flat.getName());
                st.setInt(2, getCoordId(savedCoord));
                st.setDouble(3, flat.getArea());
                st.setLong(4, flat.getNumberOfRooms());
                st.setInt(5, flat.getNumberOfBathrooms());
                st.setBoolean(6, flat.isCentralHeating());
                st.setString(7, flat.getView().name());
                st.setInt(8, getHouseId(savedHouse));
                st.setInt(9, userId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    flat.setId(rs.getLong("id"));
                    return flat;
                }
                throw new SQLException("Flat insert failed");
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public boolean update(Flat flat, int userId) {
        try {
            // update nested objects
            coordinatesDao.update(getCoordId(flat.getCoordinates()), flat.getCoordinates());
            houseDao.update(getHouseId(flat.getHouse()), flat.getHouse());

            String sql = "UPDATE flats SET name=?, area=?, number_of_rooms=?, number_of_bathrooms=?, central_heating=?, view=? " +
                    "WHERE id=? AND user_id=?";
            try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
                st.setString(1, flat.getName());
                st.setDouble(2, flat.getArea());
                st.setLong(3, flat.getNumberOfRooms());
                st.setInt(4, flat.getNumberOfBathrooms());
                st.setBoolean(5, flat.isCentralHeating());
                st.setString(6, flat.getView().name());
                st.setLong(7, flat.getId());
                st.setInt(8, userId);
                return st.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Flat findById(Connection connection, long id) {
        String sql = "SELECT * FROM flats WHERE id=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;
            Flat flat = new Flat();
            flat.setId(id);
            flat.setName(rs.getString("name"));
            flat.setArea(rs.getDouble("area"));
            flat.setNumberOfRooms(rs.getLong("number_of_rooms"));
            flat.setNumberOfBathrooms(rs.getInt("number_of_bathrooms"));
            flat.setCentralHeating(rs.getBoolean("central_heating"));
            flat.setView(View.valueOf(rs.getString("view")));
            flat.setCoordinates(coordinatesDao.findById(connection, rs.getInt("coordinates_id")));
            flat.setHouse(houseDao.findById(connection, rs.getInt("house_id")));
            Timestamp ts = rs.getTimestamp("creation_date");
            flat.setCreationDate(ts.toInstant().atZone(ZoneId.systemDefault()));
            return flat;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public List<Flat> findAllByUserId(Connection connection, int userId) {
        String sql = "SELECT id FROM flats WHERE user_id=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            List<Flat> list = new ArrayList<>();
            while (rs.next()) {
                Flat f = findById(connection, rs.getLong("id"));
                if (f != null) list.add(f);
            }
            return list;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public boolean remove(long id) {
        String sql = "DELETE FROM flats WHERE id=?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setLong(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    // helpers: simplistic lookup; in продакшене лучше возвращать id при insert
    private int getCoordId(Coordinates c) throws SQLException {
        String sql = "SELECT id FROM coordinates WHERE x=? AND y=?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setFloat(1, c.getX());
            st.setFloat(2, c.getY());
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("coordinates ID not found");
        }
    }

    private int getHouseId(House h) throws SQLException {
        String sql = "SELECT id FROM houses WHERE name=? AND year=?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, h.getName());
            st.setInt(2, h.getYear());
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("House ID not found");
        }
    }
}
