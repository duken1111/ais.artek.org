package util.statistic;

import model.*;
import util.SmenaUtil;
import util.SortUtil;
import util.TimeUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 24.03.2017.
 */
public class ByAllList {

    public static Map<String, List<String>> createCommonStatisticLines(List<Entity> entities) {

        Map<String, List<String>> result = new LinkedHashMap<>();

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
        int totalSize = entities.size();
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

        tmp.add("<b,a,r50>" + year + ";Кол-во заявок;Процент от общего количества;" +
                "День с наибольшей активностью;<w1>;День с наименьшей активностью;<w1>;Среднне кол-во заявок в день;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Больше всего заявок от оператора;;<w2>;Меньше всего;;<w2>;");
        //Нижняя строка
        tmp.add("<b,a,r30,h1>;"+ entitiesByYear.size() + ";<h1>;День;Кол-во;День;Кол-во;<h1>;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Map.Entry<Month, List<Entity>> entry : sortedMap.entrySet()) {
            StringBuilder builder = new StringBuilder();
            YearMonth yearMonthObject = YearMonth.of(year, entry.getKey());
            int days = yearMonthObject.lengthOfMonth();
            //Вывод Месяца на руссском
            builder.append(entry.getKey().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU","ru"))).append(";");
            //Количество и процент
            int count = entry.getValue().size();
            builder.append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");
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
            builder.append(ToStringUtil.getStatusSting(entry.getValue()));
            //Заявки по операторам
            Map<Region, Integer> map = entry.getValue().stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(map, count));

            tmp.add(builder.toString());
        }

        tmp.add("");
        return tmp;
    }

    private static List<String> smenaStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        int totalSize = entities.size();
        //Шапка
        //Верхняя строка
        tmp.add("<b,a,r50>Смены;Квота;Заявок;Процент;" +
                "Обработанных заявок;<w1>;" +
                "Необработанных заявок;<w1>;" +
                "Кол-во заявок на место;" +
                "Больше всего заявок;;<w2>;" +
                "Меньше всего заявок;;<w2>;");
        //Нижняя строка
        tmp.add("<b,a,r30,h1>;<h1>;<h1>;<h1>;Кол-во;Процент;Кол-во;Процент;<h1>;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

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
            builder.append(size).append(";").append(ToStringUtil.getProcent(size,totalSize)).append(";");
            //Обработанные заявки
            builder.append(ToStringUtil.getStatusSting(entitiesBySmena));
            //Колво заявок на место
            builder.append(String.format("%.1f",(float) size/smena.getQuota())).append(";");
            //Больше всего заявок
            //Карта объектов
            Map<Region, Integer> collect = entitiesBySmena.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(collect, size));

            tmp.add(builder.toString());

        }

        tmp.add("");
        return tmp;
    }

    private static List<String> operatorTypeStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        int totalSize = entities.size();
        //Шапка
        StringBuilder head = new StringBuilder();
        //Верхняя строка
        tmp.add("<b,a,r50>Тип оператора;Кол-во записей;Процент от общего кол-ва;" +
                "Обработанных заявок;<w1>;" +
                "Необработанных заявок;<w1>;" +
                "Обработанных заявок в день;" +
                "Больше всего заявок;;<w2>;" +
                "Меньше всего заявок;;<w2>;");
        //Нижняя\
        tmp.add("<b,a,r30,h1>;<h1>;<h1>;Кол-во;Процент;Кол-во;Процент;<h1>;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        for(Type type : Type.values()) {
            if(type == Type.NONAMED)
                continue;
            StringBuilder builder = new StringBuilder();
            List<Entity> entytiesByType = entities.stream().filter(e -> e.getRegion().getType() == type).collect(Collectors.toList());

            int typeSize = entytiesByType.size();
            //До обработанных заявок
            builder.append(Type.getName(type)).append(";").append(typeSize).append(";").append(ToStringUtil.getProcent(typeSize,totalSize)).append(";");
            //Обработанные заявки
            builder.append(ToStringUtil.getStatusSting(entytiesByType));
            //Обработанных заявок в день
            SortedSet<LocalDate> numberSet = new TreeSet<>();
            Map<LocalDate, Integer> byDay = entytiesByType.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.summingInt(e -> 1)));
            numberSet.addAll(byDay.keySet());
            builder.append(String.format("%.1f", (float)typeSize/ TimeUtil.daysBetween(numberSet.first())));
            builder.append(";");
            //Больше всего заявок
            //Карта объектов
            Map<Region, Integer> collect = entytiesByType.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(collect, typeSize));

            //добавляем в лист Строк
            tmp.add(builder.toString());
        }

        tmp.add("");
        return tmp;
    }

    //Шапака статистики и начальная статистика
    private static List<String> mainStatistic(List<Entity> entities) {
        List<String> tmp = new ArrayList<>();
        int totalSize = entities.size();
        //Шапка
        tmp.add("<b,a,r30>;Количество записей;Процент от общего количества");
        //Всего
        tmp.add("Всего заявок;" + entities.size() + ";100");

        //Без Региона
        int noRegion = (int) entities.stream().filter(e -> e.getRegion() == null).count();
        tmp.add("Без регионов;" + noRegion + ";" + ToStringUtil.getProcent(noRegion,totalSize));

        //Без без смены
        int noName = (int) entities.stream().filter(e -> e.getSmena() == null).count();
        tmp.add("Без смены;" + noName + ";" + ToStringUtil.getProcent(noName,totalSize));

        //Без Статуса заявки
        int noStatus = (int) entities.stream().filter(e -> e.getStatus() == Status.NONAMED).count();
        tmp.add("Без статуса;" + noRegion + ";" + ToStringUtil.getProcent(noStatus,totalSize));

        tmp.add("");


        return tmp;
    }
}
