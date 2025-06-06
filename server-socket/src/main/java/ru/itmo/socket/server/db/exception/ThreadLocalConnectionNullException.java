package ru.itmo.socket.server.db.exception;

public class ThreadLocalConnectionNullException extends RuntimeException{
    public ThreadLocalConnectionNullException(String message) {
        super(message);
    }
}
