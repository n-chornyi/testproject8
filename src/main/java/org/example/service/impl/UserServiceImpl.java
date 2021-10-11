package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UserDTO;
import org.example.exception.ErrorAuth;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO findByLoginAndPassword(String login, String password) {
        return userMapper.getDTO(userRepository.findByLoginAndPassword(login,password).orElseThrow(
                () -> new ErrorAuth("User not found")
        ));
    }

    @Override
    public UserDTO findByLogin(String login) {
        return userMapper.getDTO(userRepository.findByLogin(login)
                .orElseThrow(() -> new ErrorAuth("User not found")));
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public UserDTO save(CreateUserDTO createUserDTO) {
        return userMapper.getDTO(userRepository.save(userMapper.parseDTO(createUserDTO)));
    }
}
