package util;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * Created by duke on 04.03.2017.
 */
public class TimeUtil {
    private static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate getLocalDateByString(String s) {
        return LocalDate.parse(s,DATE_TME_FORMATTER);
    }

    public static int daysBetween(LocalDate start) {
        LocalDate today = LocalDate.now();
        return (int) DAYS.between(start,today);
    }

    public static int daysBetween(LocalDate start, LocalDate end) {
        return (int) DAYS.between(start,end);
    }

}
