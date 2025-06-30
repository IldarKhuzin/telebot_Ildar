package ru.ildar.surveybot.service;

import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;

public interface ReportService {
    @Async
    CompletableFuture<byte[]> generateReportBytes();
} 