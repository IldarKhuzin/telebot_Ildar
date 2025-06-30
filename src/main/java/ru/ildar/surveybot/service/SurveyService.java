package ru.ildar.surveybot.service;

import ru.ildar.surveybot.entity.SurveyEntity;
import java.util.List;

public interface SurveyService {
    SurveyEntity createSurvey(Long userId, Integer score);
    List<SurveyEntity> getSurveysByUserId(Long userId);
    List<SurveyEntity> getAllSurveys();
} 