package ru.ildar.surveybot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ildar.surveybot.config.BotConfig;
import ru.ildar.surveybot.entity.UserEntity;
import ru.ildar.surveybot.enums.BotState;
import ru.ildar.surveybot.service.UserService;
import ru.ildar.surveybot.service.SurveyService;
import ru.ildar.surveybot.service.ReportService;
import ru.ildar.surveybot.util.EmailValidator;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotController extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UserService userService;
    private final SurveyService surveyService;
    private final ReportService reportService;
    private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, UserEntity> formBuffer = new HashMap<>();

    @Autowired
    public BotController(BotConfig botConfig, UserService userService, SurveyService surveyService, ReportService reportService) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.userService = userService;
        this.surveyService = surveyService;
        this.reportService = reportService;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            switch (text) {
                case "/start":
                    userStates.put(chatId, BotState.IDLE);
                    formBuffer.remove(chatId);
                    sendMessage(chatId, "Привет! Я бот-опросник. Используй /form для прохождения опроса или /report для получения отчёта.");
                    break;
                case "/form":
                    userStates.put(chatId, BotState.WAIT_NAME);
                    formBuffer.put(chatId, new UserEntity());
                    sendMessage(chatId, "Введите ваше имя:");
                    break;
                case "/report":
                    userStates.put(chatId, BotState.IDLE);
                    formBuffer.remove(chatId);
                    sendMessage(chatId, "Генерирую отчёт, пожалуйста, подождите...");
                    // Генерация отчёта асинхронно (шаблон)
                    // byte[] report = reportService.generateReportBytes();
                    // sendDocument(chatId, report);
                    break;
                default:
                    handleForm(chatId, text);
            }
        }
    }

    private void handleForm(Long chatId, String text) {
        BotState state = userStates.getOrDefault(chatId, BotState.IDLE);
        UserEntity buffer = formBuffer.get(chatId);
        switch (state) {
            case WAIT_NAME:
                buffer.setName(text);
                userStates.put(chatId, BotState.WAIT_EMAIL);
                sendMessage(chatId, "Введите ваш email:");
                break;
            case WAIT_EMAIL:
                if (!EmailValidator.isValid(text)) {
                    sendMessage(chatId, "Некорректный email. Попробуйте снова:");
                    return;
                }
                buffer.setEmail(text);
                userStates.put(chatId, BotState.WAIT_SCORE);
                sendMessage(chatId, "Оцените наш сервис от 1 до 10:");
                break;
            case WAIT_SCORE:
                try {
                    int score = Integer.parseInt(text);
                    if (score < 1 || score > 10) throw new NumberFormatException();
                    UserEntity user = userService.getUserByEmail(buffer.getEmail());
                    if (user == null) {
                        user = userService.createUser(buffer.getName(), buffer.getEmail());
                    }
                    surveyService.createSurvey(user.getId(), score);
                    userStates.put(chatId, BotState.IDLE);
                    formBuffer.remove(chatId);
                    sendMessage(chatId, "Спасибо за участие в опросе!");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Введите число от 1 до 10:");
                }
                break;
            default:
                sendMessage(chatId, "Используйте /form для начала опроса или /report для получения отчёта.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Для отправки отчёта (Word-файл) реализовать sendDocument
} 