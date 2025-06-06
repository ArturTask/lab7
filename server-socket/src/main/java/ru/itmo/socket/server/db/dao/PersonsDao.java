package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Color;
import ru.itmo.socket.common.entity.Person;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.*;
import java.time.ZoneId;

public class PersonsDao {

    public Person insert(Person person) {
        String sql = "INSERT INTO persons (name, birthday, height, weight, eye_color) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, person.getName());
            st.setTimestamp(2, Timestamp.from(person.getBirthday().toInstant()));
            st.setFloat(3, person.getHeight());
            st.setObject(4, person.getWeight(), Types.FLOAT);
            st.setString(5, person.getEyeColor().name());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                return person;
            } else {
                throw new SQLException("insert не добавил автора " + person);
            }
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public Person findById(int id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public Person findById(Connection connection, int id) {
        String sql = "SELECT name, birthday, height, weight, eye_color FROM persons WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;
            Person person = new Person();
            person.setName(rs.getString("name"));
            person.setBirthday(rs.getTimestamp("birthday").toInstant().atZone(ZoneId.systemDefault()));
            person.setHeight(rs.getFloat("height"));
            person.setWeight(rs.getFloat("weight"));
            person.setEyeColor(Color.valueOf(rs.getString("eye_color")));
            return person;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public void update(int id, Person person) {
        String sql = "UPDATE persons SET name = ?, birthday = ?, height = ?, weight = ?, eye_color = ? WHERE id = ?";
        try (PreparedStatement st = DbUserContext.getConnection().prepareStatement(sql)) {
            st.setString(1, person.getName());
            st.setTimestamp(2, Timestamp.from(person.getBirthday().toInstant()));
            st.setFloat(3, person.getHeight());
            st.setObject(4, person.getWeight(), Types.FLOAT);
            st.setString(5, person.getEyeColor().name());
            st.setInt(6, id);
            int updated = st.executeUpdate();
            if (updated == 0) throw new SQLException("update не обновил автора id=" + id);
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }
}
