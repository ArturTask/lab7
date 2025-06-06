package ru.itmo.socket.server;

import ru.itmo.socket.common.util.ConnectionContext;
import ru.itmo.socket.server.concurrent.ProcessClientTask;
import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.DatabaseInitializer;
import ru.itmo.socket.server.manager.LabWorkTreeSetManager;
import ru.itmo.socket.server.manager.XmlCollectionLoader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(ConnectionContext.getMaxConnections());
    private static final AtomicInteger CONNECTION_COUNTER = new AtomicInteger(0);

    // concurrent TreeSet<> (берем первый доступный незанятый номер и присваиваем клиенту)
    private static final ConcurrentSkipListSet<Integer> AVAILABLE_IDS = initAvailableIds();

    public static void main(String[] args) {
        initDb();
        startServer();
    }

    private static void initDb() {
        try(Connection connection = DatabaseConfig.getConnection()) {
            DatabaseInitializer.init(connection);
        } catch (SQLException e) {
            System.out.println("[Tech] [ERROR] блин че то с базой");
            throw new RuntimeException(e);
        }
    }

    private static void startServer() {
        // загружаем из файла collection.txt изначальные значения
        LabWorkTreeSetManager manager = LabWorkTreeSetManager.getInstance();
        new XmlCollectionLoader(manager, "collection.txt").load();

        int port = ConnectionContext.getPort();

        // стартуем сервер
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Макс. количество подключений = " + ConnectionContext.getMaxConnections());
            System.out.println("Сервер запущен и ожидает подключения...");

            // ждем подключений
            acceptConnection(serverSocket);

        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void acceptConnection(ServerSocket serverSocket) throws IOException, ClassNotFoundException {
        while (true) {
            // accept new connection
            Socket clientSocket = serverSocket.accept();
            if (CONNECTION_COUNTER.get() == ConnectionContext.getMaxConnections()) {
                System.out.println("Максимальное число подключений достигнуто, отказ в подключении: " + clientSocket.getInetAddress());
                clientSocket.close();
                continue;
            }

            forkJoinPool.submit(new ProcessClientTask(clientSocket, AVAILABLE_IDS, CONNECTION_COUNTER));

        }
    }

    /**
     * Генерим что-то вроде номерков, по количеству доступных соединений - после отключения
     * просто переиспользуем номерки пользователей так они не будут плодиться
     */
    private static ConcurrentSkipListSet<Integer> initAvailableIds() {
        ConcurrentSkipListSet<Integer> arrayBlockingQueue = new ConcurrentSkipListSet<>();
        for (int i = 0; i < ConnectionContext.getMaxConnections(); i++) {
            arrayBlockingQueue.add(i + 1);
        }
        return arrayBlockingQueue;
    }
}
