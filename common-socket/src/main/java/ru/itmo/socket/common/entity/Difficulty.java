package ru.itmo.socket.common.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Difficulty implements Serializable {
    NORMAL,
    IMPOSSIBLE,
    INSANE,
    HOPELESS;
}
