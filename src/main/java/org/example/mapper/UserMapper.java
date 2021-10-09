package org.example.mapper;

import org.example.dto.user.CreateUserDTO;
import org.example.dto.user.UserDTO;
import org.example.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO getDTO(User users);

    User parseDTO(UserDTO userDTO);

    User parseDTO(CreateUserDTO userDTO);

    List<UserDTO> getAll(List<User> users);
}
