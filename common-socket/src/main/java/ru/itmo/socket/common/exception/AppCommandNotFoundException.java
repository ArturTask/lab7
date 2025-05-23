package ru.itmo.socket.common.exception;

public class AppCommandNotFoundException extends RuntimeException{

    public AppCommandNotFoundException(String message) {
        super(message);
    }
}
