package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
