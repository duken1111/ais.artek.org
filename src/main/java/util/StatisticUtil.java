package util;

import model.*;
import util.statistic.ByAllList;
import util.statistic.BySmenaList;
import util.statistic.ByYearList;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 21.03.2017.
 */
public class StatisticUtil {

    //Подробная статистика по всем записям
    public static Map<String, List<String>> createCommonStatisticLines(List<Entity> entities) {
        return ByAllList.createCommonStatisticLines(entities);
    }

    //Подробная статистика заявок на указаные год с листами по месячной статистикой
    public static Map<String, List<String>> createStatisticByYearWithParams(List<Entity> entities, int year) {
        return ByYearList.createStatisticByYearWithParams(entities,year);
    }

    //Подробная сатистика по СМЕНАМ
    public static Map<String, List<String>> createStatisticBySmena(List<Entity> entities) {
        return BySmenaList.createStatisticBySmena(entities);
    }

}
