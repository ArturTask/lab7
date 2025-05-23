package ru.itmo.socket.common.entity;

import lombok.*;

import java.io.Serializable;

// Объект, который мы будем передавать между клиентом и сервером
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender;
    private String text;
    private long timestamp;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

}
