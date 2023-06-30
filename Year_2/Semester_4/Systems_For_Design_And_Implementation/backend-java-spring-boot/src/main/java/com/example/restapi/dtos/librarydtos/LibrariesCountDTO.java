package com.example.restapi.dtos.librarydtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrariesCountDTO {
    private Long ID;
    private String name;
    private Long totalCount;
}
