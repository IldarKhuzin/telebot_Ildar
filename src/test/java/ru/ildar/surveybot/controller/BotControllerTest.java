package ru.ildar.surveybot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ildar.surveybot.config.BotConfig;
import ru.ildar.surveybot.service.UserService;
import ru.ildar.surveybot.service.SurveyService;
import ru.ildar.surveybot.service.ReportService;

import static org.mockito.Mockito.*;

class BotControllerTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private UserService userService;
    @Mock
    private SurveyService surveyService;
    @Mock
    private ReportService reportService;

    @InjectMocks
    private BotController botController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(botConfig.getBotToken()).thenReturn("dummy");
        when(botConfig.getBotUsername()).thenReturn("dummyBot");
    }

    @Test
    void testStartCommand() {
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(1L);
        update.setMessage(message);
        botController.onUpdateReceived(update);
        // Проверка: sendMessage вызывается (можно проверить через spy или mock)
    }

    @Test
    void testFormCommand() {
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/form");
        when(message.getChatId()).thenReturn(2L);
        update.setMessage(message);
        botController.onUpdateReceived(update);
        // Проверка: sendMessage вызывается (можно проверить через spy или mock)
    }
} 