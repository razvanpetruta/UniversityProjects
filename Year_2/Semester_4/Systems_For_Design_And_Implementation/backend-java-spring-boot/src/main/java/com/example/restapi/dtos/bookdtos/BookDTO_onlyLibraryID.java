package com.example.restapi.dtos.bookdtos;

import com.example.restapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO_onlyLibraryID extends BookDTO {
    private Long libraryID;
    private String username;
}
