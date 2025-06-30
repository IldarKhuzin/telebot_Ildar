package ru.ildar.surveybot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ildar.surveybot.entity.UserEntity;
import ru.ildar.surveybot.repository.UserRepository;
import ru.ildar.surveybot.service.ReportService;
import ru.ildar.surveybot.util.ReportGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final ReportGenerator reportGenerator;

    @Autowired
    public ReportServiceImpl(UserRepository userRepository, ReportGenerator reportGenerator) {
        this.userRepository = userRepository;
        this.reportGenerator = reportGenerator;
    }

    @Override
    @Async
    public CompletableFuture<byte[]> generateReportBytes() {
        try {
            List<UserEntity> users = userRepository.findAll();
            byte[] data = reportGenerator.generateReport(users);
            return CompletableFuture.completedFuture(data);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }
} 