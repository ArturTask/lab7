package ru.itmo.socket.server.db;

import ru.itmo.socket.server.db.exception.ThreadLocalConnectionNullException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Задел на будущее - для многопоточки, у каждого пользака будет свой connection
 */
public class ConnectionManager {

    private static final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    /**
     * эту функцию надо вызывать в всегда при новом потоке (подключении) чтобы проинициалищировать соединение с БД
     */
    public static Connection connectToDatabase() throws SQLException {
        Connection connection = DatabaseConfig.getConnection();
        threadLocalConnection.set(connection);
        return connection;
    }

    public static Connection getConnection() {
        Connection conn = threadLocalConnection.get();
        if (conn == null) {
            System.err.println("[ERROR] Поток " + Thread.currentThread().getName() + " не имеет подключения к БД!");
            throw new ThreadLocalConnectionNullException("No connection for thread " + Thread.currentThread().getName());
        }
        return conn;
    }

    public static void disconnect() throws SQLException {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            conn.close();
            threadLocalConnection.remove();
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