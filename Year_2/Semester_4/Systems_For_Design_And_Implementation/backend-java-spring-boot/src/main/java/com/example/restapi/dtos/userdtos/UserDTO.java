package com.example.restapi.dtos.userdtos;

import com.example.restapi.model.user.Role;
import com.example.restapi.model.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long ID;
    @NotEmpty
    private String username;
    private Set<Role> roles;
    private UserProfileDTO userProfile;
    private Long totalLibraries;
    private Long totalBooks;
    private Long totalReaders;
}
