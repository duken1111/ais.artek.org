package util;

import model.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 21.03.2017.
 */
public class StatisticUtil {
    private static int totalSize;

    public static Map<String, List<String>> createCommonStatisticLines(List<Entity> entities) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        totalSize = entities.size();

        List<String> value = new ArrayList<>();

        //Общая статистика
        value.addAll(mainStatistic(entities));
        //статистика по типу оператора REGION, PARTNER
        value.addAll(operatorTypeStatistic(entities));
        //СТАТИСТИКА ПО СМЕНАМ
        value.addAll(smenaStatistic(entities));

        value.addAll(statisticByMonthes(entities));

        result.put("Статистика",value);

        return result;
    }

    private static List<String> statisticByMonthes(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        //Шапка
        //Верхняя строка
        int year = LocalDate.now().getYear();
        Month month= LocalDate.now().getMonth();
        //Список заявок на конкретный год
        final List<Entity> entitiesByYear = entities.stream().filter(e -> e.getDate().getYear() == year).collect(Collectors.toList());
        int size = entitiesByYear.size();
        //Карта отсортированных п омесяцам заявок
        SortedMap<Month,List<Entity>> sortedMap = new TreeMap<>((Enum::compareTo));
        sortedMap.putAll(entitiesByYear.stream().collect(Collectors.groupingBy(e -> e.getDate().getMonth())));

        tmp.add(year + ";Кол-во заявок;Процент от общего количества;" +
                "День с наибольшей активностью;;День с наименьшей активностью;;Среднне кол-во заявок в день;" +
                "Обработанных заявок;;Необработанных заявок;;" +
                "Больше всего заявок от оператора;;;Меньше всего;;;");
        //Нижняя строка
        tmp.add(";"+ entitiesByYear.size() + ";;День;Кол-во;День;Кол-во;;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Month, List<Entity>> entry : sortedMap.entrySet()) {
            StringBuilder builder = new StringBuilder();
            YearMonth yearMonthObject = YearMonth.of(year, entry.getKey());
            int days = yearMonthObject.lengthOfMonth();
            //Вывод Месяца на руссском
            builder.append(entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU","ru"))).append(";");
            //Количество и процент
            int count = entry.getValue().size();
            builder.append(count).append(";").append(getProcent(count,size)).append(";");
            //Больше всего заявок в день
            Map<Integer, Integer> collect = entry.getValue().stream().collect(Collectors.groupingBy(e -> e.getDate().getDayOfMonth(), Collectors.summingInt(e -> 1)));
            int maxDay = SortUtil.getMaxEntitiesByDays(collect);
            builder.append(LocalDate.of(year,entry.getKey(),maxDay).toString()).append(";").append(collect.get(maxDay)).append(";");
            //МИНИМАЛЬНЫЙ ДЕЬ
            int minDay = SortUtil.getMinEntitiesByDays(collect);
            builder.append(LocalDate.of(year,entry.getKey(),minDay).toString()).append(";").append(collect.get(minDay)).append(";");
            //Среднее количество
            builder.append(String.format("%.1f", (float)count/days)).append(";");
            //Обработанных заявок
            builder.append(getStatusSting(entry.getValue()));
            //Заявки по операторам
            Map<Region, Integer> map = entry.getValue().stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(getOperatorString(map, count));

            tmp.add(builder.toString());
        }

        tmp.add("");
        return tmp;
    }

    private static List<String> smenaStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        //Шапка
        //Верхняя строка
        tmp.add("Смены;Квота;Заявок;Процент;" +
                "Обработанных заявок;;" +
                "Необработанных заявок;;" +
                "Кол-во заявок на место;" +
                "Больше всего заявок;;;" +
                "Меньше всего заявок;;;");
        //Нижняя строка
        tmp.add(";;;;Кол-во;Процент;Кол-во;Процент;;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        //Строки по сменам
        for(Smena smena : SmenaUtil.SMENA) {
            if(smena == null)
                continue;
            StringBuilder builder = new StringBuilder();
            //Название и квота
            builder.append(smena.getName()).append(";").append(smena.getQuota()).append(";");
            //кол-во заявок на смену и процент
            List<Entity> entitiesBySmena = entities.stream().filter(e -> e.getSmena().equals(smena)).collect(Collectors.toList());
            int size = entitiesBySmena.size();
            builder.append(size).append(";").append(getProcent(size,totalSize)).append(";");
            //Обработанные заявки
            builder.append(getStatusSting(entitiesBySmena));
            //Колво заявок на место
            builder.append(String.format("%.1f",(float) size/smena.getQuota())).append(";");
            //Больше всего заявок
            //Карта объектов
            Map<Region, Integer> collect = entitiesBySmena.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(getOperatorString(collect, size));

            tmp.add(builder.toString());

        }

        tmp.add("");
        return tmp;
    }

    private static List<String> operatorTypeStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        //Шапка
        StringBuilder head = new StringBuilder();
        //Верхняя строка
        tmp.add("Тип оператора;Кол-во записей;Процент от общего кол-ва;" +
                "Обработанных заявок;;" +
                "Необработанных заявок;;" +
                "Обработанных заявок в день;" +
                "Больше всего заявок;;;" +
                "Меньше всего заявок;;;");
        //Нижняя\
        tmp.add(";;;Кол-во;Процент;Кол-во;Процент;;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Type type : Type.values()) {
            if(type == Type.NONAMED)
                continue;
            StringBuilder builder = new StringBuilder();
            List<Entity> entytiesByType = entities.stream().filter(e -> e.getRegion().getType() == type).collect(Collectors.toList());

            int typeSize = entytiesByType.size();
            //До обработанных заявок
            builder.append(Type.getName(type)).append(";").append(typeSize).append(";").append(getProcent(typeSize,totalSize)).append(";");
            //Обработанные заявки
            builder.append(getStatusSting(entytiesByType));
            //Обработанных заявок в день
            SortedSet<LocalDate> numberSet = new TreeSet<>();
            Map<LocalDate, Integer> byDay = entytiesByType.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.summingInt(e -> 1)));
            numberSet.addAll(byDay.keySet());
            builder.append(String.format("%.1f", (float)typeSize/TimeUtil.daysBetween(numberSet.first())));
            builder.append(";");
            //Больше всего заявок
            //Карта объектов
            Map<Region, Integer> collect = entytiesByType.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(getOperatorString(collect, typeSize));

            //добавляем в лист Строк
            tmp.add(builder.toString());
        }

        tmp.add("");
        return tmp;
    }

    //Шапака статистики и начальная статистика
    private static List<String> mainStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        //Шапка
        tmp.add(";Количество записей;Процент от общего количества");
        //Всего
        tmp.add("Всего заявок;" + entities.size() + ";100");

        //Без Региона
        int noRegion = (int) entities.stream().filter(e -> e.getRegion() == null).count();
        tmp.add("Без регионов;" + noRegion + ";" + getProcent(noRegion,totalSize));

        //Без без смены
        int noName = (int) entities.stream().filter(e -> e.getSmena() == null).count();
        tmp.add("Без смены;" + noName + ";" + getProcent(noName,totalSize));

        //Без Статуса заявки
        int noStatus = (int) entities.stream().filter(e -> e.getStatus() == Status.NONAMED).count();
        tmp.add("Без статуса;" + noRegion + ";" + getProcent(noStatus,totalSize));

        tmp.add("");


        return tmp;
    }


    private static String getProcent(int value, int totalSize) {
        float result = (float) value / totalSize*100;
        return String.format("%.1f",result);
    }

    private static String getStatusSting(List<Entity> list) {
        StringBuilder builder = new StringBuilder();
        //Обработанные заявки
        int status = (int) list.stream().filter(Entity::isProcessing).count();
        builder.append(status).append(";").append(getProcent(status,list.size())).append(";");
        //Не Обработанные заявки
        status = (int) list.stream().filter(e -> !e.isProcessing()).count();
        builder.append(status).append(";").append(getProcent(status,list.size())).append(";");

        return builder.toString();
    }

    private static String getOperatorString(Map<Region, Integer> map, int size) {
        StringBuilder builder = new StringBuilder();

        Region max = SortUtil.getMaxEntitiesByRegion(map);
        builder.append(max.getName()).append(";").append(map.get(max)).append(";").append(getProcent(map.get(max),size)).append(";");

        //Меньше всего
        Region min = SortUtil.getMinEntitiesByRegion(map);
        builder.append(min.getName()).append(";").append(map.get(min)).append(";").append(getProcent(map.get(min),size)).append(";");

        return builder.toString();
    }
}
