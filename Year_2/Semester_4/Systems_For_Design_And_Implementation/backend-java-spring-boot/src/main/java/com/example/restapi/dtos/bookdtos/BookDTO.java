package com.example.restapi.dtos.bookdtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BookDTO {
    private Long ID;
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String publisher;
    @Min(0)
    private Double price;
    @Min(1000)
    @Max(2023)
    private Integer publishedYear;
    @NotEmpty
    private String description;
}
