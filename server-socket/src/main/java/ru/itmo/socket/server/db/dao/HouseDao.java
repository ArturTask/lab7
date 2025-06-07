package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.data.House;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseDao {

    public House insert(House house) {
        String sql = "INSERT INTO houses (name, year, number_of_floors, number_of_flats_on_floor, number_of_lifts) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, house.getName());
            st.setInt(2, house.getYear());
            st.setLong(3, house.getNumberOfFloors());
            st.setLong(4, house.getNumberOfFlatsOnFloor());
            st.setLong(5, house.getNumberOfLifts());
            ResultSet rs = st.executeQuery();
            if (rs.next()) return house;
            throw new SQLException("Insert house failed " + house);
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public House findById(Connection connection, int id) {
        String sql = "SELECT name, year, number_of_floors, number_of_flats_on_floor, number_of_lifts FROM houses WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                House h = new House();
                h.setName(rs.getString("name"));
                h.setYear(rs.getInt("year"));
                h.setNumberOfFloors(rs.getLong("number_of_floors"));
                h.setNumberOfFlatsOnFloor(rs.getLong("number_of_flats_on_floor"));
                h.setNumberOfLifts(rs.getLong("number_of_lifts"));
                return h;
            }
            return null;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public void update(int id, House house) {
        String sql = "UPDATE houses SET name=?, year=?, number_of_floors=?, number_of_flats_on_floor=?, number_of_lifts=? WHERE id=?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, house.getName());
            st.setInt(2, house.getYear());
            st.setLong(3, house.getNumberOfFloors());
            st.setLong(4, house.getNumberOfFlatsOnFloor());
            st.setLong(5, house.getNumberOfLifts());
            st.setInt(6, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}
