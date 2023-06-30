package com.example.restapi.dtos.userdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;

    private String bio;

    private String location;

    private LocalDate birthDate;

    private String gender;

    private String maritalStatus;
}
