package ru.ildar.telebot_ildar.service;

import org.jvnet.hk2.annotations.Service;
import org.springframework.scheduling.annotation.Async;
import ru.ildar.telebot_ildar.util.ReportGenerator;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    private final SurveyRepository surveyRepository;
    private final ReportGenerator reportGenerator;

    @Async("reportTaskExecutor")
    public CompletableFuture<File> generateReportAsync(Long userId) {
        List<SurveyEntity> surveys = surveyRepository.findByUserId(userId);
        File reportFile = reportGenerator.generateWordReport(surveys);
        return CompletableFuture.completedFuture(reportFile);
    }

    // Другие методы
}