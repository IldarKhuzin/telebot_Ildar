package ru.ildar.surveybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ildar.surveybot.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
} 