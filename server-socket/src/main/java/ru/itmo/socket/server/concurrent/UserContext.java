package ru.itmo.socket.server.concurrent;

import lombok.Getter;
import ru.itmo.socket.server.concurrent.exception.UserContextNotInitializedException;

import java.sql.SQLException;

@Getter
public class UserContext {

    private static final ThreadLocal<Integer> CLIENT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> LOGIN = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> AUTHORIZED = new ThreadLocal<>();
    /**
     * user_id from db (when authorized and saved to db)
     */
    private static final ThreadLocal<Integer> DB_USER_ID = new ThreadLocal<>();

    public static void initUserContext(int clientId) {
        try {
            // connect user to db
            DbUserContext.connectToDb();
            CLIENT_ID.set(clientId);
            LOGIN.set("unauthorized_" + clientId);
            AUTHORIZED.set(false);
            DB_USER_ID.set(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void inheritContext(int clientId, String login, boolean authorized, int userId) {
        try {
            // connect user to db
            DbUserContext.connectToDb();
            CLIENT_ID.set(clientId);
            LOGIN.set(login);
            AUTHORIZED.set(authorized);
            DB_USER_ID.set(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyUserContextWithoutDisconnect() {
        CLIENT_ID.remove();
        LOGIN.remove();
        AUTHORIZED.remove();
        DB_USER_ID.remove();

    }

    public static void destroyUserContext() {
        try {
            DbUserContext.disconnect();
            CLIENT_ID.remove();
            LOGIN.remove();
            AUTHORIZED.remove();
            DB_USER_ID.remove();
        } catch (SQLException ignore) {
        }
    }

    public static Integer getClientId() {
        if (CLIENT_ID.get() == null) {
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return CLIENT_ID.get();
    }

    public static String getLogin() {
        if (LOGIN.get() == null) {
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return LOGIN.get();
    }

    public static void setLogin(String login) {
        LOGIN.set(login);
    }

    public static Boolean getAuthorized() {
        if (AUTHORIZED.get() == null) {
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return AUTHORIZED.get();
    }

    public static void setAuthorized(boolean authorized) {
        AUTHORIZED.set(authorized);
    }


    public static Integer getDbUserId() {
        if (DB_USER_ID.get() == null) {
            throw new UserContextNotInitializedException("Контекст пользователя не был проинициализирован");
        }
        return DB_USER_ID.get();
    }

    public static void setDbUserId(Integer userId) {
        DB_USER_ID.set(userId);
    }


}
