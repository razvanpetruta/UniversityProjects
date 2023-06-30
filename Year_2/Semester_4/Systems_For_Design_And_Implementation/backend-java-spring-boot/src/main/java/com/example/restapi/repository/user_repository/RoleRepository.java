package com.example.restapi.repository.user_repository;

import com.example.restapi.model.user.ERole;
import com.example.restapi.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
