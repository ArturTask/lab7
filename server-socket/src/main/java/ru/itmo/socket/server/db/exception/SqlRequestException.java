package ru.itmo.socket.server.db.exception;

public class SqlRequestException extends RuntimeException {

    public SqlRequestException(String message) {
        super(message);
    }

    public SqlRequestException(Throwable cause) {
        super(cause);
    }
}
