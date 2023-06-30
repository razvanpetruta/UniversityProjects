package com.example.restapi.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDTO {
    String username;
    String jwtToken;
}
