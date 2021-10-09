package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            u.setProjects(null);
        }
        return users;
    }

    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login,password);
    }

    public User getByLogin(String login) {
        User user = userRepository.findByLogin(login);
        user.setProjects(null);
        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
