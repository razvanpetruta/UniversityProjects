package com.example.restapi.dtos.readerdtos;

import com.example.restapi.dtos.librarydtos.LibraryDTO_withMembership;
import com.example.restapi.dtos.readerdtos.ReaderDTO;
import com.example.restapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDTO_forOne extends ReaderDTO {
    private Set<LibraryDTO_withMembership> libraries;
    private String username;
}
