package org.example.service;

import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UserDTO;

public interface UserService {

    UserDTO findByLoginAndPassword(String login, String password);

    UserDTO findByLogin(String login);

    boolean existsByLogin(String login);

    UserDTO save(CreateUserDTO createUserDTO);
}
