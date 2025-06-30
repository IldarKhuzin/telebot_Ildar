package ru.ildar.surveybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ildar.surveybot.entity.SurveyEntity;
import java.util.List;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
    List<SurveyEntity> findByUserId(Long userId);
} 