package ua.tarastom.news.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String DateFormat(String stringDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", new Locale(getCountry()));
        return dateFormat.format(getDate(stringDate));
    }

    public static Date getDate(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry().toLowerCase();
    }

    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }
}