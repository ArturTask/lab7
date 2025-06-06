package ru.itmo.socket.server.concurrent;

import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.exception.ThreadLocalConnectionNullException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Задел на будущее - для многопоточки, у каждого пользака будет свой connection
 */
public class DbContext {

    private static final ThreadLocal<Connection> THREAD_LOCAL_DB_CONNECTION = new ThreadLocal<>();

    /**
     * эту функцию надо вызывать в всегда при новом потоке (подключении) чтобы проинициалищировать соединение с БД
     */
    public static void connectToDb() throws SQLException {
        Connection connection = DatabaseConfig.getConnection();
        THREAD_LOCAL_DB_CONNECTION.set(connection);
    }

    public static Connection getConnection() {
        Connection conn = THREAD_LOCAL_DB_CONNECTION.get();
        if (conn == null) {
            System.err.println("[ERROR] Поток " + Thread.currentThread().getName() + " не имеет подключения к БД!");
            throw new ThreadLocalConnectionNullException("No connection for thread " + Thread.currentThread().getName());
        }
        return conn;
    }

    public static void disconnect() throws SQLException {
        Connection conn = THREAD_LOCAL_DB_CONNECTION.get();
        if (conn != null) {
            conn.close();
            THREAD_LOCAL_DB_CONNECTION.remove();
        }
    }
}


/*
ExecutorService pool = Executors.newFixedThreadPool(3);

while (true) {
    Socket clientSocket = serverSocket.accept();
    pool.submit(() -> {
        try {
            ConnectionManager.connectToDatabase(); // ставим ThreadLocal
            // handle client
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.disconnect(); // САМОЕ ВАЖНОЕ!!!
        }
    });
}
 */