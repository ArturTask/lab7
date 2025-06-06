package ru.itmo.socket.client.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {

    /**
     * SHA-384 всегда даёт одинаковый хэш для одного и того же пароля (это не шифрование, а хэширование — one way!).
     * Так что "расшифровать" нельзя и не нужно, просто отправляем хэшированный пароль сразу на сервер
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            // шифруем пароль - получаем массив байт
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // переводим байты в строку которую отправим на сервер
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-384 not available", e);
        }
    }
}
