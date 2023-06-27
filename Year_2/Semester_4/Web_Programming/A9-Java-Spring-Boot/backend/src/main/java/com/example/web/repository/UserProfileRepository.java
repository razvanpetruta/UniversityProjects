package com.example.web.repository;

import com.example.web.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    List<UserProfile> findByNameContainingIgnoreCase(String name);
    List<UserProfile> findByEmailContainingIgnoreCase(String email);
    List<UserProfile> findByHomeTownContainingIgnoreCase(String homeTown);
    List<UserProfile> findByAgeLessThanEqual(int maxAge);
}