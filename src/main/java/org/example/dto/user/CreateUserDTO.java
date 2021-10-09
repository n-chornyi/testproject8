package org.example.dto.user;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String login;
    private String password;
    private String email;
}
