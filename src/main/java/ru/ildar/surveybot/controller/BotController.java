package ru.ildar.surveybot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class BotController extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UserService userService;
    private final SurveyService surveyService;
    private final ReportService reportService;
    private final Map<Long, BotState> userStates = new HashMap<>();
    private final Map<Long, UserEntity> formBuffer = new HashMap<>();

    @Autowired
    public BotController(BotConfig botConfig, UserService userService, SurveyService surveyService
            , ReportService reportService) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.userService = userService;
        this.surveyService = surveyService;
        this.reportService = reportService;
        try {
            execute(new SetMyCommands(
                java.util.Arrays.asList(
                    new BotCommand("/start", "Приветствие и сброс состояния"),
                    new BotCommand("/form", "Пройти опрос"),
                    new BotCommand("/report", "Получить отчёт")
                ),
                new BotCommandScopeDefault(),
                null
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    sendInlineKeyboard(chatId, "Привет! Я бот-опросник. Выберите действие:");
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
                    reportService.generateReportBytes().thenAccept(report -> {
                        if (report != null) {
                            sendDocument(chatId, report, "report.docx");
                        } else {
                            sendMessage(chatId, "Ошибка при генерации отчёта.");
                        }
                    });
                    break;
                default:
                    handleForm(chatId, text);
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (data) {
                case "START":
                    userStates.put(chatId, BotState.IDLE);
                    formBuffer.remove(chatId);
                    sendInlineKeyboard(chatId, "Привет! Я бот-опросник. Выберите действие:");
                    break;
                case "FORM":
                    userStates.put(chatId, BotState.WAIT_NAME);
                    formBuffer.put(chatId, new UserEntity());
                    sendMessage(chatId, "Введите ваше имя:");
                    break;
                case "REPORT":
                    userStates.put(chatId, BotState.IDLE);
                    formBuffer.remove(chatId);
                    sendMessage(chatId, "Генерирую отчёт, пожалуйста, подождите...");
                    reportService.generateReportBytes().thenAccept(report -> {
                        if (report != null) {
                            sendDocument(chatId, report, "report.docx");
                        } else {
                            sendMessage(chatId, "Ошибка при генерации отчёта.");
                        }
                    });
                    break;
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

    private void sendDocument(Long chatId, byte[] fileBytes, String filename) {
        try {
            SendDocument sendDoc = new SendDocument();
            sendDoc.setChatId(chatId.toString());
            sendDoc.setDocument(new org.telegram.telegrambots.meta.api.objects.InputFile(new java.io.ByteArrayInputStream
                    (fileBytes), filename));
            execute(sendDoc);
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка при отправке файла: " + e.getMessage());
        }
    }

    private void sendInlineKeyboard(Long chatId, String text) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new java.util.ArrayList<>();
        List<InlineKeyboardButton> row = new java.util.ArrayList<>();
        row.add(InlineKeyboardButton.builder().text("Старт").callbackData("START").build());
        row.add(InlineKeyboardButton.builder().text("Опрос").callbackData("FORM").build());
        row.add(InlineKeyboardButton.builder().text("Отчёт").callbackData("REPORT").build());
        rows.add(row);
        markup.setKeyboard(rows);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
} 