package ru.itmo.socket.server.concurrent;

import lombok.Getter;
import ru.itmo.socket.server.concurrent.exception.UserContextNotInitializedException;

@Getter
public class UserContext {

    private static final ThreadLocal<Integer> CLIENT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> LOGIN = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> AUTHORIZED = new ThreadLocal<>();

    public static void initUserContext(int clientId) {
        try {
            // connect user to db
            DbContext.connectToDb();
            CLIENT_ID.set(clientId);
            LOGIN.set("unauthorized_" + clientId);
            AUTHORIZED.set(false);
        } catch (Exception e) {
            // todo artur
        }
    }

    public static void destroyUserContext() {
        try {
            DbContext.disconnect();
            CLIENT_ID.remove();
            LOGIN.remove();
            AUTHORIZED.remove();
        } catch (Exception e) {
            // todo artur
        }
    }

    public static Integer getClientId() {
        if (CLIENT_ID.get()==null){
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return CLIENT_ID.get();
    }

    public static String getLogin() {
        if (LOGIN.get()==null){
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return LOGIN.get();
    }

    public static void setLogin(String login) {
        LOGIN.set(login);
    }

    public static Boolean getAuthorized() {
        if (AUTHORIZED.get()==null){
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return AUTHORIZED.get();
    }

    public static void setAuthorized(boolean authorized) {
        AUTHORIZED.set(authorized);
    }

}
