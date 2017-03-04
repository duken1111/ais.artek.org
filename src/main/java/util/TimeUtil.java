package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by duke on 04.03.2017.
 */
public class TimeUtil {
    private static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate getLocalDateByString(String s) {
        return LocalDate.parse(s,DATE_TME_FORMATTER);
    }
}
