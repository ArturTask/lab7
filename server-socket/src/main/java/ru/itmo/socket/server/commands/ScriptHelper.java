package ru.itmo.socket.server.commands;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.ObjectOutputStream;

@UtilityClass
public class ScriptHelper {

    public static void sendToClient(String answer, ObjectOutputStream oos) {
        try {
            oos.writeUTF(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
