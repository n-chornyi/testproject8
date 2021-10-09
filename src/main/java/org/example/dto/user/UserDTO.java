package org.example.dto.user;

import lombok.Data;
import org.example.entity.Project;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
public class UserDTO {
    private int id;
    private String login;
    private String password;
    private String email;
}
