package ru.ildar.telebot_ildar.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class ReportGenerator {

    private static final String REPORT_TEMPLATE = "survey_report_%d.docx";

    public File generateWordReport(List<SurveyEntity> surveys) {
        try (XWPFDocument document = new XWPFDocument()) {
            // Создание таблицы с результатами
            XWPFTable table = document.createTable();
            // Заголовки таблицы
            addTableHeader(table);

            // Данные опросов
            for (SurveyEntity survey : surveys) {
                addSurveyRow(table, survey);
            }

            // Сохранение во временный файл
            File reportFile = File.createTempFile(
                    String.format(REPORT_TEMPLATE, System.currentTimeMillis()),
                    ".docx"
            );

            try (FileOutputStream out = new FileOutputStream(reportFile)) {
                document.write(out);
            }

            return reportFile;
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to generate report", e);
        }
    }

    private void addTableHeader(XWPFTable table) {
        // Реализация добавления заголовков
    }

    private void addSurveyRow(XWPFTable table, SurveyEntity survey) {
        // Реализация добавления строки с данными
    }
}