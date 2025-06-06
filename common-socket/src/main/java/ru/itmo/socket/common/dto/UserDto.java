package ru.itmo.socket.common.dto;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto implements Serializable {

    private int id;
    private String login;
    private String password;
}
