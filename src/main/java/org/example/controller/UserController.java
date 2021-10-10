package org.example.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.AuthUser;
import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UserDTO;
import org.example.exception.NotFoundExeption;
import org.example.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public AuthUser login(@RequestParam(value = "login", required = true) String login,
                          @RequestParam(value = "password", required = true) String password) {

        password = Sha512DigestUtils.shaHex(password);
        UserDTO userDTO = userService.findByLoginAndPassword(login, password);
        String token = getJWTToken(login);
        AuthUser user = new AuthUser();
        user.setId(userDTO.getId());
        user.setLogin(login);
        user.setToken(token);
        return user;
    }

    @PostMapping("/register")
    public AuthUser register(@RequestBody @NotNull CreateUserDTO createUserDTO) {

        if (userService.existsByLogin(createUserDTO.getLogin())) {
            throw new NotFoundExeption("User exits");
        }
        createUserDTO.setPassword(Sha512DigestUtils.shaHex(createUserDTO.getPassword()));
        UserDTO userDTO = userService.save(createUserDTO);
        String token = getJWTToken(userDTO.getLogin());
        AuthUser user = new AuthUser();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        user.setToken(token);
        return user;
    }

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");
        Date date = Date.from(LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return "Bearer " + token;
    }
}
