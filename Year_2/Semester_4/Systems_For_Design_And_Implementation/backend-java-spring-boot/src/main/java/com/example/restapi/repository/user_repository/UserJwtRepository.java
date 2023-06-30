package com.example.restapi.repository.user_repository;

import com.example.restapi.model.user.UserJwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJwtRepository extends JpaRepository<UserJwt, Long> {
    Boolean existsByJwtToken(String jwtToken);
    Boolean existsByUsername(String username);
    UserJwt findByJwtToken(String jwtToken);
    void deleteByUsername(String username);
}
