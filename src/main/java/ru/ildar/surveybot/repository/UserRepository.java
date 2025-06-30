package ru.ildar.surveybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ildar.surveybot.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = "surveys")
    List<UserEntity> findAll();
} 