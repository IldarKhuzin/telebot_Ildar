package ru.ildar.surveybot.enums;

public enum BotState {
    // Состояния бота
    START,
    SURVEY,
    REPORT,
    IDLE,         // Ожидание команды
    WAIT_NAME,    // Ожидание имени
    WAIT_EMAIL,   // Ожидание email
    WAIT_SCORE    // Ожидание оценки
} 