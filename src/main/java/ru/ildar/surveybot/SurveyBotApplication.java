package ru.ildar.surveybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ildar.surveybot.controller.BotController;

@SpringBootApplication
public class SurveyBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveyBotApplication.class, args);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(BotController botController) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(botController);
        return botsApi;
    }
} 