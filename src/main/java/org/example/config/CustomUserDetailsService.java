package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.entity.User;
import org.example.service.impl.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private  final UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByLogin(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
