package ru.itmo.socket.server.concurrent.exception;

public class UserContextNotInitializedException extends RuntimeException{
    public UserContextNotInitializedException(String message) {
        super(message);
    }
}
