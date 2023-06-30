package com.example.restapi.dtos.userdtos;

import com.example.restapi.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRolesDTO {
    private String username;
    private Set<Role> roles;
}
