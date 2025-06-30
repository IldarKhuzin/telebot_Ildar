package ru.ildar.surveybot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ildar.surveybot.util.ReportGenerator;

@Configuration
public class ReportConfig {
    @Bean
    public ReportGenerator reportGenerator() {
        return new ReportGenerator();
    }
} 