package org.example.dto.user;

import lombok.Data;

@Data
public class AuthUser {
    private int id;
    private String login;
    private String token;
}
