package ru.itmo.socket.common.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommandDto implements Serializable {

    private String commandName;
    private Object arg;
}
