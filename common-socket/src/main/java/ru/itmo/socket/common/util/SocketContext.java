package ru.itmo.socket.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class SocketContext {

    private static Map<SocketContextParam, Object> ALL_PARAMS = Map.of(
            SocketContextParam.SERVER_HOST, "localhost",
            SocketContextParam.SERVER_PORT, 12345
    );

    public Object getParam(SocketContextParam param) {
        return ALL_PARAMS.get(param);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(SocketContextParam param, Class<T> clazz) {
        Object val = ALL_PARAMS.get(param);

        if (!clazz.isInstance(val)) {
            throw new ClassCastException("WrongClass");
        }
        return (T) val;
    }

    public int getPort() {
        return (Integer) ALL_PARAMS.get(SocketContextParam.SERVER_PORT);
    }

    public String getHost() {
        return String.valueOf(ALL_PARAMS.get(SocketContextParam.SERVER_HOST));
    }


    @Getter
    @AllArgsConstructor
    public enum SocketContextParam {
        SERVER_PORT,
        SERVER_HOST
    }

}
