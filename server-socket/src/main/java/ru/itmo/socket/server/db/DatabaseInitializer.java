package ru.itmo.socket.server.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private final Connection connection;

    public DatabaseInitializer(Connection connection) {
        this.connection = connection;
    }

    public void init() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    username TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS coordinates (
                    id SERIAL PRIMARY KEY,
                    x FLOAT NOT NULL,
                    y FLOAT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS persons (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255),
                    birthday TIMESTAMP,
                    height FLOAT,
                    weight FLOAT,
                    eye_color VARCHAR(50)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS lab_works (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    minimal_point BIGINT NOT NULL,
                    difficulty VARCHAR(50),
                    creation_date DATE,
                    coordinates_id INT REFERENCES coordinates(id),
                    author_id INT REFERENCES persons(id)
                );
            """);
        }
    }
}
