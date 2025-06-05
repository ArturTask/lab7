package ru.itmo.socket.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConnectionContext {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final int MAX_CONNECTIONS = 3;

    public static int getPort() {
        return SERVER_PORT;
    }

    public static String getHost() {
        return SERVER_HOST;
    }

    public static int getMaxConnections() {
        return MAX_CONNECTIONS;
    }

}
