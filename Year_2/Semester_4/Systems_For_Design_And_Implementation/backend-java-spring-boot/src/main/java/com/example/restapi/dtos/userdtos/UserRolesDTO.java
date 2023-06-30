package com.example.restapi.dtos.userdtos;

import com.example.restapi.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesDTO {
    private List<String> roles;
}