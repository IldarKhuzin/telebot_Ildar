package ru.ildar.telebot_ildar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.ildar.telebot_ildar.service.ReportService;

@Slf4j
@Component
public class BotController extends TelegramLongPollingBot {

    private final ReportService reportService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/report".equals(messageText)) {
                handleReportCommand(chatId);
            }
            // Обработка других команд
        }
    }

    private void handleReportCommand(Long chatId) {
        sendMessage(chatId, "Генерация отчета...");

        reportService.generateReportAsync(chatId)
                .thenAccept(reportFile -> {
                    try {
                        sendDocument(chatId, reportFile);
                        sendMessage(chatId, "Отчет готов!");
                    } catch (TelegramApiException e) {
                        log.error("Failed to send report", e);
                        sendMessage(chatId, "Ошибка при отправке отчета");
                    } finally {
                        if (reportFile != null && reportFile.exists()) {
                            reportFile.delete();
                        }
                    }
                })
                .exceptionally(ex -> {
                    log.error("Report generation failed", ex);
                    sendMessage(chatId, "Ошибка при генерации отчета");
                    return null;
                });
    }

    // Другие методы
}