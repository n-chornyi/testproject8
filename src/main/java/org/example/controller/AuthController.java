package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.config.jwt.JwtProvider;
import org.example.dto.user.CreateUserDTO;
import org.example.entity.User;
import org.example.service.impl.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        User u = new User();
        u.setPassword(createUserDTO.getPassword());
        u.setLogin(createUserDTO.getLogin());
        u.setEmail(createUserDTO.getEmail());
        userService.save(u);
        return "OK";
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }

}
