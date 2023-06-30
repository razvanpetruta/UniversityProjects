package com.example.restapi.dtos.readerdtos;

import com.example.restapi.dtos.readerdtos.ReaderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDTO_withMembership extends ReaderDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
