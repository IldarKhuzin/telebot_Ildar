package ru.ildar.surveybot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.telegram.telegrambots.meta.TelegramBotsApi;
// import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
// import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    // Здесь можно создать бин TelegramBot, если используешь библиотеку TelegramBots
    // @Bean
    // public TelegramBot telegramBot() {
    //     return new TelegramBot(botToken, botUsername);
    // }

    public String getBotToken() {
        return botToken;
    }

    public String getBotUsername() {
        return botUsername;
    }
} 