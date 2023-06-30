package com.example.restapi.dtos.librarydtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class LibraryDTO {
    private Long ID;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @Email
    private String email;

    @NotEmpty
    private String website;

    @Min(1850)
    @Max(2023)
    private Integer yearOfConstruction;
}
