package ru.ildar.surveybot.service;

import java.io.IOException;

public interface ReportService {
    byte[] generateReportBytes() throws IOException;
} 