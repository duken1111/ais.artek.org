package util.statistic;

import model.Entity;
import model.Region;
import model.Smena;
import model.Type;
import sun.util.resources.LocaleData;
import util.SortUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 24.03.2017.
 */
public class BySmenaList {

    //Подробная статистика заявок на указаные год с листами по месячной статистикой
    public static Map<String, List<String>> createStatisticBySmena(List<Entity> entities) {
        Map<String, List<String>> result = new LinkedHashMap<>();

        //ГЛАВНЫЙ ЛИСТ СМЕН
        result.put("Общая",getMainSheet(entities));

        //ЛИСТЫ С ПОДРОБНОЙ СТАТИСТИКОЙ ПО СМЕНАМ
        SortedMap<Smena, List<Entity>> mapBySmena = new TreeMap<>(((o1, o2) -> o1.getNumber() - o2.getNumber()));
        mapBySmena.putAll(entities.stream().collect(Collectors.groupingBy(Entity::getSmena)));
        for(Map.Entry<Smena,List<Entity>> entry : mapBySmena.entrySet()) {
                result.put(entry.getKey().getName(), getSmenaStatisticList(entry.getValue(),entry.getKey()));
        }

        return result;
    }

    //ГЛАВНЫЙ ЛИСТ ПО СМЕНАМ
    private static List<String> getMainSheet(List<Entity> entities) {
        List<String> result = new ArrayList<>();
        int size = entities.size();
        //Шапка сататистики
        result.add("<b,a,r50>Смены;Квота;Кол-во заявок;Процент от количества;Кол-возаявок на место;" +
                "Самый активный день;<w1>;День с наменьшей активностью;<w1>;" +
                "Среднее кол-во заявок день;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наимеьшей активностью;;<w2>;");
        result.add("<b,a,r30,h1>;<h1>;" + size + ";<h1>;<h1>;День;Кол-во;День;Кол-во;<h1>;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");

        SortedMap<Smena, List<Entity>> mapBySmena = new TreeMap<>(((o1, o2) -> o1.getNumber() - o2.getNumber()));
        mapBySmena.putAll(entities.stream().collect(Collectors.groupingBy(Entity::getSmena)));

        for(Map.Entry<Smena,List<Entity>> entry : mapBySmena.entrySet()) {
            StringBuilder builder = new StringBuilder();
            List<Entity> entitiesBySmena = entry.getValue();
            int count = entitiesBySmena.size();

            //Название смены -квота - заявок - прцоент - кол-во заявок
            builder.append(entry.getKey().getName()).append(";").append(entry.getKey().getQuota()).append(";")
                    .append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";")
                    .append(String.format("%.1f", (float) count/entry.getKey().getQuota())).append(";");

            //Дни АКТИВНОСТЕЙ
            Map<LocalDate, Integer> mapDays = entitiesBySmena.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getDaysStrings(mapDays));

            //Среднее кол-во заявок в день
            builder.append(String.format("%.1f", (float) count/ mapDays.size())).append(";");

            //Обработанных заявок
            builder.append(ToStringUtil.getStatusSting(entitiesBySmena));

            //ОПЕРАТОРСКАЯ СТАТИСТИКА
            Map<Region, Integer> regionMap = entitiesBySmena.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(regionMap,count));

            result.add(builder.toString());
        }

        result.add("");
        return result;
    }

    private static List<String> getSmenaStatisticList(List<Entity> entities, Smena smena) {
        List<String> result = new ArrayList<>();
        //Шапка листа
        result.addAll(getHeadLines(entities, smena));
        //ТЕЛО ЛИСТА
        result.addAll(getBodyLinesBySmena(entities,smena));

        return result;
    }


    //ШАПКА ПОДРОБНОЙ СТАТИСТИКА ПО КОНКРЕТНОЙ СМЕНЕ
    private static List<String> getHeadLines(List<Entity> entities, Smena smena) {
        List<String> result = new ArrayList<>();
        int size = entities.size();

        //ШАПКА СТАТИСТИКИ
        result.add("<b,a,r50>" + smena.getName() + ";Кол-во заявок;Процент от количества;" +
                "Самый активный день;<w1>;День с наменьшей активностью;<w1>;" +
                "Среднее кол-во заявок день;" +
                "Обработанных заявок;<w1>;Необработанных заявок;<w1>;" +
                "Самый активный оператор;;<w2>;Оператор с наимеьшей активностью;;<w2>;");
        result.add("<b,a,r30,h1>;" + size + ";<h1>;День;Кол-во;День;Кол-во;<h1>;Кол-во;Процент;Кол-во;Процент;Оператор;Кол-во;Процент;Оператор;Кол-во;Процент;");


        SortedMap<Type, List<Entity>> mapByType = new TreeMap<>((Enum::compareTo));
        mapByType.putAll(entities.stream().collect(Collectors.groupingBy(e -> e.getRegion().getType())));

        for(Map.Entry<Type, List<Entity>> entry : mapByType.entrySet()) {
            StringBuilder builder = new StringBuilder();
            List<Entity> entitiesByType = entry.getValue();
            int count = entitiesByType.size();
            //Тип партнера - заявок - процент
            builder.append(Type.getName(entry.getKey())).append(";").append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");

            //ДНИ АКТИВНОСТЕЙ
            Map<LocalDate, Integer> mapDays = entitiesByType.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getDaysStrings(mapDays));

            //Среднее кол-во заявок в день
            builder.append(String.format("%.1f", (float) count/ mapDays.size())).append(";");

            //Обработанных заявок
            builder.append(ToStringUtil.getStatusSting(entitiesByType));

            //ОПЕРАТОРСКАЯ СТАТИСТИКА
            Map<Region, Integer> regionMap = entitiesByType.stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.summingInt(e -> 1)));
            builder.append(ToStringUtil.getOperatorString(regionMap,count));

            result.add(builder.toString());
        }

        result.add("");
        return result;
    }
    //ТЕЛО ПОДРОБНОЙ СТАИТИСТИКИПО СМЕНАМ И ТИПУ ОПЕРАТОРА
    private static List<String> getBodyLinesBySmena(List<Entity> entities, Smena smena) {
        List<String> result = new ArrayList<>();
        int size = entities.size();

        SortedMap<Type, List<Entity>> mapByType = new TreeMap<>((Enum::compareTo));
        mapByType.putAll(entities.stream().collect(Collectors.groupingBy(e -> e.getRegion().getType())));

        //ШАПКА СТАТИСТИКИ ДЛЯ КАЖДОЙ КАТЕГОРИИ ОПЕРАТОРА
        for(Map.Entry<Type, List<Entity>> entry : mapByType.entrySet()) {
            //ШАПКА СТАТИСТИКИ
            result.add("<b,a,r50>" + Type.getName(entry.getKey()) + ";Кол-во заявок;Процент от количества;" +
                    "Самый активный день;<w1>;День с наменьшей активностью;<w1>;" +
                    "Среднее кол-во заявок день;" +
                    "Средний возраст;" +
                    "Средний рейтинг;" +
                    "Приняты к расмотрению;;<w2>;Полученные;;<w2>;Отклоненные;;<w2>;Не рассмотренные;;<w2>;");

            result.add("<b,a,r30,h1>;" + entry.getValue().size() + ";<h1>;День;Кол-во;День;Кол-во;<h1>;<h1>;<h1>;" +
                    "Кол-во;Процент;Средний рейтинг;Кол-во;Процент;Средний рейтинг;Кол-во;Процент;Средний рейтинг;Кол-во;Процент;Средний рейтинг;");

            SortedMap<Region, List<Entity>> mapByRegion = new TreeMap<>((o1, o2) -> o1.getName().compareTo(o2.getName()));
            mapByRegion.putAll(entry.getValue().stream().collect(Collectors.groupingBy(Entity::getRegion)));

            for(Map.Entry<Region, List<Entity>> en : mapByRegion.entrySet()) {
                StringBuilder builder = new StringBuilder();
                List<Entity> entitiesByRegion = en.getValue();
                int count = entitiesByRegion.size();

                // НАЗВАНИЕ РЕГИОНА КОЛ_ВО ЗАЯВОК
                builder.append(en.getKey().getName()).append(";").append(count).append(";").append(ToStringUtil.getProcent(count,size)).append(";");

                //АКТИВНЫЕ ДНИ
                Map<LocalDate, Integer> mapDays = entitiesByRegion.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.summingInt(e -> 1)));
                builder.append(ToStringUtil.getDaysStrings(mapDays));

                //Среднее кол-во заявок в день
                builder.append(String.format("%.1f", (float) count/ mapDays.size())).append(";");
                //Средний возраст
                builder.append(SortUtil.averageAge(entitiesByRegion)).append(";");
                //Средний рейтинг
                builder.append(SortUtil.averageRating(entitiesByRegion)).append(";");

                //ПРИНЯТЫЕ - ПОЛУЧЕННЫЕ - ОТКЛОНЕННЫЕ - НЕ РАСМОТРЕННЫЕ
                builder.append(ToStringUtil.getInDeatailStatusString(en.getValue())).append(";");


                result.add(builder.toString());
            }

            result.add("");
        }

        result.add("");
        return result;
    }
}
