package ru.ildar.surveybot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ildar.surveybot.entity.SurveyEntity;
import ru.ildar.surveybot.entity.UserEntity;
import ru.ildar.surveybot.repository.SurveyRepository;
import ru.ildar.surveybot.repository.UserRepository;
import ru.ildar.surveybot.service.SurveyService;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository surveyRepository, UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SurveyEntity createSurvey(Long userId, Integer score) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        SurveyEntity survey = new SurveyEntity();
        survey.setUser(userOpt.get());
        survey.setScore(score);
        return surveyRepository.save(survey);
    }

    @Override
    public List<SurveyEntity> getSurveysByUserId(Long userId) {
        return surveyRepository.findByUserId(userId);
    }

    @Override
    public List<SurveyEntity> getAllSurveys() {
        return surveyRepository.findAll();
    }
} 