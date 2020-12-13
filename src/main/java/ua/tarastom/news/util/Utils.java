package ua.tarastom.news.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Utils {

    public static LocalDateTime getDate(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        }
        LocalDateTime parse = null;
        try {
            parse = LocalDateTime.parse(stringDate, DateTimeFormatter.RFC_1123_DATE_TIME);
        } catch (Exception e) {
            System.out.println("Exception in Utils.getDate " + stringDate);
        }
        return parse;
    }
}