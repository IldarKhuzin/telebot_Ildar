package ru.ildar.surveybot.util;

import org.apache.poi.xwpf.usermodel.*;
import ru.ildar.surveybot.entity.UserEntity;
import ru.ildar.surveybot.entity.SurveyEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {
    public byte[] generateReport(List<UserEntity> users) throws IOException {
        XWPFDocument doc = new XWPFDocument();
        XWPFTable table = doc.createTable();
        XWPFTableRow header = table.getRow(0);
        header.getCell(0).setText("Имя");
        header.addNewTableCell().setText("Email");
        header.addNewTableCell().setText("Оценка");
        for (UserEntity user : users) {
            for (SurveyEntity survey : user.getSurveys()) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(user.getName());
                row.getCell(1).setText(user.getEmail());
                row.getCell(2).setText(String.valueOf(survey.getScore()));
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.write(out);
        doc.close();
        return out.toByteArray();
    }
} 