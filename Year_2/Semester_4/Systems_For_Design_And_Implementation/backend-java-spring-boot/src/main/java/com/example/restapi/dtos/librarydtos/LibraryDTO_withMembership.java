package com.example.restapi.dtos.librarydtos;

import com.example.restapi.dtos.librarydtos.LibraryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO_withMembership extends LibraryDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
