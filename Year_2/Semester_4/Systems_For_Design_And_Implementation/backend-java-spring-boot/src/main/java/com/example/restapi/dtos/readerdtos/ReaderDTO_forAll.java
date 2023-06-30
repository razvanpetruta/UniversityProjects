package com.example.restapi.dtos.readerdtos;

import com.example.restapi.dtos.readerdtos.ReaderDTO;
import com.example.restapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDTO_forAll extends ReaderDTO {
    private Long totalLibraries;
    private String username;
}
