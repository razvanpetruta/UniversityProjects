package com.example.restapi.repository.user_repository;

import com.example.restapi.model.user.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}
