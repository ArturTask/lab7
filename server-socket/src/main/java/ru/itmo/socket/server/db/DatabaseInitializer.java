package ru.itmo.socket.server.db;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@UtilityClass
public class DatabaseInitializer {

    public static void init(Connection connection) throws SQLException {
        System.out.println("[Tech] [INFO] начинаем инициализировать БД");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                                id SERIAL PRIMARY KEY,
                                login TEXT UNIQUE NOT NULL,
                                password_hash TEXT NOT NULL
                            );
                        """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS coordinates (
                            id SERIAL PRIMARY KEY,
                            x  REAL  NOT NULL,
                            y  REAL  NOT NULL
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS persons (
                            id SERIAL PRIMARY KEY,
                            name      VARCHAR(255),
                            birthday  TIMESTAMP,
                            height    REAL,
                            weight    REAL,
                            eye_color VARCHAR(50)
                        );
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS lab_works (
                            id            SERIAL PRIMARY KEY,
                            name          VARCHAR(255) NOT NULL,
                            minimal_point BIGINT       NOT NULL,
                            difficulty    VARCHAR(50),
                            creation_date DATE,
                            coordinates_id INT REFERENCES coordinates(id) ON DELETE CASCADE,
                            author_id      INT REFERENCES persons(id)     ON DELETE CASCADE,
                            user_id        INT REFERENCES users(id)
                        );
                    """);
            System.out.println("[Tech] [INFO] проинициализировали БД, фух...");
        }
    }
}
