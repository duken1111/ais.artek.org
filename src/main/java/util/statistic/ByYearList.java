package util.statistic;

import model.Entity;
import model.Region;
import model.Type;

import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 24.03.2017.
 */
public class ByYearList {

    //Подробная статистика заявок на указаные год с листами по месячной статистикой
    public static Map<String, List<String>> createStatisticByYearWithParams(List<Entity> entities, int year) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        //Фильтруем лист по году
        List<Entity> yearEntities = entities.stream().filter(e -> e.getDate().getYear() == year).collect(Collectors.toList());
        int totalSize = yearEntities.size();
        //Главный лист
        result.put(year + "",getMainYearStatisticAllLines(yearEntities,year));

        //Помесячная карта объектов
        SortedMap<Month,List<Entity>> sortedMap = new TreeMap<>((Enum::compareTo));
        sortedMap.putAll(yearEntities.stream().collect(Collectors.groupingBy(e -> e.getDate().getMonth())));
        result.putAll(getInDetailStatiticByMonth(sortedMap,year));

        return result;
    }

    //ПОДДРОРБНАЯ ГОДОВАЯ СТАТИСТИКА ПО МЕСЯЦАМ!!!!
    private static Map<String,List<String>> getInDetailStatiticByMonth(Map<Month, List<Entity>> entitiesByMonth, int year) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for(Map.Entry<Month, List<Entity>> entry : entitiesByMonth.entrySet()) {
            //МЕСЯЦ
            String month = entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE,new Locale("ru","RU"));
            //Кладу все в мапу Ключ название месяца знаени строки листа
            result.put(month,getStatisticLinesInDetailByMonth(entry.getKey(), year, entry.getValue()));
        }
        return result;
    }


    //ПОДРОБНАЯ ПО МЕСЯЧНАЯ СТАТИСТИКА -  ВСЕ СТРОКИ
    private static List<String> getStatisticLinesInDetailByMonth(Month month, int year, List<Entity> entities) {
        List<String> result = new ArrayList<>();

        int size = entities.size();
        SortedMap<Type, List<Entity>> mapByRegionType = new TreeMap<>(Enum::compareTo);
        mapByRegionType.putAll(entities.stream().collect(Collectors.groupingBy(e -> e.getRegion().getType())));
        //Шапка листа со статистикой по регионам
        result.addAll(getMonthStatiscByRegionType(mapByRegionType,month,year,size));
        //Основная статистика по дневная
        SortedMap<Integer, List<Entity>> sortedMapByDays = new TreeMap<>((Integer::compareTo));
        sortedMapByDays.putAll(entities.stream().collect(Collectors.groupingBy(e -> e.getDate().getDayOfMonth())));
        result.addAll(getMonthStatiscByDays(sortedMapByDays, month, year, size));

        return result;
    }

    //Месячная статистика по типу оператора - ШАПКА
    private static List<String> getMonthStatiscByRegionType(Map<Type, List<Entity>> mapByRegionType, Month month, int year, int size) {
        List<String> result = new ArrayList<>();
        String name = month.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru","RU"));
        //Шапка
        result.add("<b,a,r50>Статистика за " + name + " " + year + ";Кол-во заявок;Процент от кол-ва;" +
                "Самый активный день;<w1>;День с наименьшей активностью;<w1>;" +
                "Среднне кол-во заявок в день;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наименьшей активностью;;<w2>;");
        result.add("<b,a,r30,h1>;" + size + ";<h1>;День;Кол-во;День;Кол-во;<h1>;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Type, List<Entity>> entry : mapByRegionType.entrySet()) {
            StringBuilder builder = new StringBuilder();
            List<Entity> listByRegionType = entry.getValue();
            int count = listByRegionType.size();
            //Название  - количество - прцент от колва
            builder.append(Type.getName(entry.getKey())).append(";").append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");
            //Самый Активный день и неактивный
            Map<Integer, Integer> mapByDayOfMonth = listByRegionType.stream().collect(Collectors.groupingBy(e -> e.getDate().getDayOfMonth(), Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getDayOfYearStrings(mapByDayOfMonth,month,year));
            //среднее колво заявок в день
            builder.append(String.format("%.1f",(float)count/ month.length(Year.isLeap(year)))).append(";");
            //Обработанных заявок и необработанных
            builder.append(ToStringUtil.getStatusSting(listByRegionType));
            //Оператор активный и неактивный
            Map<Region, Integer> mapByRegion = listByRegionType.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(mapByRegion,count));

            result.add(builder.toString());

        }

        result.add("");
        return result;
    }
    //МЕСЯЧНАЯ СТАТИСТИКА = ОСНОВНАЯ ПОДНЕВНАЯ СТАТИСТИКА
    private static List<String> getMonthStatiscByDays(Map<Integer, List<Entity>> sortedMapByDays, Month month, int year, int size) {
        List<String> result = new ArrayList<>();
        String name = month.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru","RU"));
        //Шапка
        result.add("<b,a,r50>" +name + " " + year + ";Кол-во заявок;Процент от кол-ва;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наименьшей активностью;;<w2>;");
        result.add("<b,a,r30,h1>;" + size + ";;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Integer, List<Entity>> entry : sortedMapByDays.entrySet()) {
            StringBuilder builder = new StringBuilder();
            List<Entity> listByDays = entry.getValue();
            int count = listByDays.size();
            //Название  - количество - прцент от колва
            builder.append(entry.getKey()).append(";").append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");
            //Обработанных заявок и необработанных
            builder.append(ToStringUtil.getStatusSting(listByDays));
            //Оператор активный и неактивный
            Map<Region, Integer> mapByRegion = listByDays.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(mapByRegion,count));
            result.add(builder.toString());

        }

        result.add("");
        return result;
    }


    //Главный лист с годовой сатистикой
    private static List<String> getMainYearStatisticAllLines(List<Entity> yearEntities, int year) {
        List<String> result = new ArrayList<>();
        int size = yearEntities.size();
        //Получаю Статистику по операторам за указаный год
        SortedMap<Type,List<Entity>> mapOperatorType = new TreeMap<>(Enum::compareTo);
        mapOperatorType.putAll(yearEntities.stream().collect(Collectors.groupingBy(e -> e.getRegion().getType())));
        result.addAll(getMainYearStatisticByOperator(mapOperatorType,year, size));

        //Получаю общую помесячную статистику за год
        SortedMap<Month,List<Entity>> sortedMap = new TreeMap<>((Enum::compareTo));
        sortedMap.putAll(yearEntities.stream().collect(Collectors.groupingBy(e -> e.getDate().getMonth())));
        result.addAll(getCommonMonthStatisticByYear(sortedMap,year, size));

        return result;
    }

    //ГОДОВАЯ СТАТИСТИКА ПО ТИПУ ОПЕРАТОРА
    private static List<String> getMainYearStatisticByOperator(Map<Type, List<Entity>> mapOperatorType, int year, int size) {
        List<String> result = new ArrayList<>();
        //Шапка сататистики
        result.add("<b,a,r50>Статистика за " + year + ";Кол-во заявок;Процент от кол-ва;" +
                "Самый активный месяц;;<w2>;Месяц с наименьшей активностью;;<w2>;" +
                "Самый активный день;<w1>;День с наименьшей активностью;<w1>;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наименьшей активностью;;<w2>;");
        result.add("<b,a,h1,r30>;" + size + ";<h1>;Месяц;Кол-во;Процент;Месяц;Кол-во;Процент;День;Кол-во;День;Кол-во;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Type, List<Entity>> entry : mapOperatorType.entrySet()) {
            StringBuilder builder = new StringBuilder();
            //Оператор Кол-во Процент
            List<Entity> tmpList = entry.getValue();
            int count = tmpList.size();
            builder.append(Type.getName(entry.getKey())).append(";").append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");
            //Самый Активный месяц
            Map<Month, Integer> mapByMonth = tmpList.stream().collect(Collectors.groupingBy(e -> e.getDate().getMonth(), Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getMonthStrings(mapByMonth,count));
            //Самый Активный День в году!!
            Map<Integer, Integer> mapByDaysOfYear = tmpList.stream().collect(Collectors.groupingBy(e -> e.getDate().getDayOfYear(), Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getDayOfYearStrings(mapByDaysOfYear,year));
            //Обработанных заявок
            builder.append(ToStringUtil.getStatusSting(tmpList));
            //Самый Активный оператор и самый не активный
            Map<Region, Integer> mapByRegion = tmpList.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(mapByRegion,count));

            result.add(builder.toString());

        }

        result.add("");

        return result;
    }

    //ГОДОВАЯ СТАТИСТИКА ПО МЕСЯЦАМ НА ГЛАВНОМ ЛИСТЕ
    private static List<String> getCommonMonthStatisticByYear(Map<Month, List<Entity>> mapByMonth, int year, int size) {
        List<String> result = new ArrayList<>();
        //Шапка сататистики
        result.add("<b,a,r50>Статистика по месяцам;Кол-во заявок;Процент от количества;" +
                "Самый активный день;<w1>;День с наменьшей активностью;<w1>;" +
                "Среднее кол-во заявок день;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наимеьшей активностью;;<w2>;");
        result.add("<b,a,r30,h1>;" + size + ";<h1>;День;Кол-во;День;Кол-во;<h1>;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Month, List<Entity>> entry : mapByMonth.entrySet()) {
            StringBuilder builder = new StringBuilder();
            List<Entity> monthEntities = entry.getValue();
            int count = monthEntities.size();
            //Название месяца
            builder.append(entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU","ru"))).append(";");
            //Размер и процент
            builder.append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");
            //Самый Активный день и не активный
            Map<Integer, Integer> monthByDayMap = monthEntities.stream().collect(Collectors.groupingBy(e -> e.getDate().getDayOfMonth(), Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getDayOfYearStrings(monthByDayMap,entry.getKey(),year));

            //Среднее количество завок в день
            builder.append(String.format("%.1f",(float)count/ entry.getKey().length(Year.isLeap(year)))).append(";");

            //Обработаных заявок
            builder.append(ToStringUtil.getStatusSting(monthEntities));
            //Самый активный оператор
            Map<Region, Integer> regionMap = monthEntities.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(regionMap,count));

            result.add(builder.toString());
        }

        result.add("");
        return result;
    }
}
