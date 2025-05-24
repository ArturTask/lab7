package ru.itmo.socket.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public enum Color implements Serializable {
    GREEN,
    RED,
    BROWN;
}