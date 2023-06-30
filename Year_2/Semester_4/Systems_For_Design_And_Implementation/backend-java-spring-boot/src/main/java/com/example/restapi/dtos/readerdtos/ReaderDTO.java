package com.example.restapi.dtos.readerdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ReaderDTO {
    private Long ID;

    private String name;

    private String email;

    private LocalDate birthDate;

    private String gender;

    private boolean isStudent;
}
