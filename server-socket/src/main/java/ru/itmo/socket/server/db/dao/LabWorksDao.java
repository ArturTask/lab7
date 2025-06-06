package ru.itmo.socket.server.db.dao;

import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.common.entity.Difficulty;
import ru.itmo.socket.common.entity.LabWork;
import ru.itmo.socket.common.entity.Person;
import ru.itmo.socket.server.concurrent.DbUserContext;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabWorksDao {

    private final CoordinatesDao coordinatesDao = new CoordinatesDao();
    private final PersonsDao personDao = new PersonsDao();

    public LabWork insert(LabWork labWork, int userId) {
        try {
            // Сначала сохранить координаты и автора
            Coordinates savedCoordinates = coordinatesDao.insert(labWork.getCoordinates());
            Person savedPerson = personDao.insert(labWork.getAuthor());

            String sql = """
                INSERT INTO lab_works (name, minimal_point, difficulty, creation_date, coordinates_id, author_id, user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
            """;

            try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
                stmt.setString(1, labWork.getName());
                stmt.setLong(2, labWork.getMinimalPoint());
                stmt.setString(3, labWork.getDifficulty().name());
                stmt.setDate(4, Date.valueOf(labWork.getCreationDate()));
                stmt.setInt(5, getIdFromSavedCoordinates(savedCoordinates));
                stmt.setInt(6, getIdFromSavedPerson(savedPerson));
                stmt.setInt(7, userId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    labWork.setId(rs.getLong("id"));
                    return labWork;
                } else {
                    throw new SQLException("LabWork insert failed");
                }
            }

        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public boolean update(LabWork labWork, int userId) {
        try {
            // Обновим координаты
            coordinatesDao.update(labWork.getId(), labWork.getCoordinates());

            // Обновим автора
            personDao.update(getIdFromSavedPerson(labWork.getAuthor()), labWork.getAuthor());

            String sql = """
            UPDATE lab_works
            SET name = ?, minimal_point = ?, difficulty = ?, creation_date = ?
            WHERE id = ? AND user_id = ?
        """;

            try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
                stmt.setString(1, labWork.getName());
                stmt.setLong(2, labWork.getMinimalPoint());
                stmt.setString(3, labWork.getDifficulty().name());
                stmt.setDate(4, Date.valueOf(labWork.getCreationDate()));
                stmt.setLong(5, labWork.getId());
                stmt.setInt(6, userId);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public LabWork findById(long id) {
        return findById(DbUserContext.getConnection(), id);
    }

    public LabWork findById(Connection connection, long id) {
        String sql = """
        SELECT name, minimal_point, difficulty, creation_date, coordinates_id, author_id
        FROM lab_works WHERE id = ?
    """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LabWork lab = new LabWork();
                lab.setId(id);
                lab.setName(rs.getString("name"));
                lab.setMinimalPoint(rs.getLong("minimal_point"));
                lab.setDifficulty(Difficulty.valueOf(rs.getString("difficulty")));
                lab.setCreationDate(rs.getDate("creation_date").toLocalDate());
                lab.setCoordinates(coordinatesDao.findById(connection, rs.getInt("coordinates_id")));
                lab.setAuthor(personDao.findById(connection, rs.getInt("author_id")));
                return lab;
            }
            return null;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public List<LabWork> findAllByUserId(Connection connection, int userId) {
        String sql = "SELECT id FROM lab_works WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<LabWork> result = new ArrayList<>();
            while (rs.next()) {
                LabWork lab = findById(connection, rs.getLong("id"));
                if (lab != null) result.add(lab);
            }
            return result;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }

    public boolean remove(long id, int userId) {
        String sql = "DELETE FROM lab_works WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SqlRequestException(e);
        }
    }





    private int getIdFromSavedCoordinates(Coordinates coordinates) throws SQLException {
        // Простейший хак — найти по полям
        String sql = "SELECT id FROM coordinates WHERE x = ? AND y = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setFloat(1, coordinates.getX());
            stmt.setFloat(2, coordinates.getY());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Can't find coordinates ID");
        }
    }

    private int getIdFromSavedPerson(Person person) throws SQLException {
        String sql = "SELECT id FROM persons WHERE name = ? AND height = ? AND weight = ? AND eye_color = ? AND birthday = ?";
        try (PreparedStatement stmt = DbUserContext.getConnection().prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setFloat(2, person.getHeight());
            stmt.setFloat(3, person.getWeight());
            stmt.setString(4, person.getEyeColor().name());
            stmt.setTimestamp(5, Timestamp.from(person.getBirthday().toInstant()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            throw new SQLException("Can't find person ID");
        }
    }
}

