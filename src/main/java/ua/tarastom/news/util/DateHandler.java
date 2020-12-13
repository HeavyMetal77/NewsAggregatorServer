package ua.tarastom.news.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHandler extends StdDeserializer<LocalDateTime> {

    public DateHandler(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) {
        try {
            String date = jsonparser.getText();
            return LocalDateTime.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
}
