package org.example.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.RegExp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "login shouldn't blank")
    @Size(min = 5, max = 50, message = "Length login {min}-{max} characters")
    private String login;
    @NotBlank(message = "password shouldn't blank")
    @Size(min = 5, max = 50, message = "Length password {min}-{max} characters")
    private String password;
    @NotBlank(message = "email shouldn't blank")
    @Size(min = 5, max = 60, message = "Length email {min}-{max} characters")
    private String email;
}
