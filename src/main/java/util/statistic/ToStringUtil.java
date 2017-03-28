package util.statistic;

import model.Entity;
import model.Region;
import model.Status;
import util.SortUtil;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 24.03.2017.
 */

class ToStringUtil {

    static String getProcent(int value, int totalSize) {
        float result = (float) value / totalSize*100;
        return String.format("%.1f",result);
    }

    static String getStatusSting(List<Entity> list) {
        StringBuilder builder = new StringBuilder();
        //Обработанные заявки
        int status = (int) list.stream().filter(Entity::isProcessing).count();
        builder.append(status).append(";").append(getProcent(status,list.size())).append(";");
        //Не Обработанные заявки
        status = (int) list.stream().filter(e -> !e.isProcessing()).count();
        builder.append(status).append(";").append(getProcent(status,list.size())).append(";");

        return builder.toString();
    }

    static String getOperatorString(Map<Region, Integer> map, int size) {
        StringBuilder builder = new StringBuilder();

        Region max = SortUtil.getMaxEntitiesByRegion(map);
        builder.append(max.getName()).append(";").append(map.get(max)).append(";").append(getProcent(map.get(max),size)).append(";");

        //Меньше всего
        Region min = SortUtil.getMinEntitiesByRegion(map);
        builder.append(min.getName()).append(";").append(map.get(min)).append(";").append(getProcent(map.get(min),size)).append(";");

        return builder.toString();
    }

    static String getMonthStrings(Map<Month, Integer> map, int size) {
        StringBuilder builder = new StringBuilder();

        Month max = SortUtil.getMaxEntitiesByMonth(map);
        builder.append(max.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU","ru"))).append(";").append(map.get(max)).append(";").append(getProcent(map.get(max),size)).append(";");

        //Меньше всего
        Month min = SortUtil.getMinEntitiesByMonth(map);
        builder.append(min.getDisplayName(TextStyle.FULL_STANDALONE,new Locale("RU","ru"))).append(";").append(map.get(min)).append(";").append(getProcent(map.get(min),size)).append(";");

        return builder.toString();
    }

     static String getDayOfYearStrings(Map<Integer, Integer> map, int year) {
        StringBuilder builder = new StringBuilder();

        int max = SortUtil.getMaxEntitiesByDays(map);
        builder.append(LocalDate.ofYearDay(year,max)).append(";").append(map.get(max)).append(";");

        //Меньше всего
        int min = SortUtil.getMinEntitiesByDays(map);
        builder.append(LocalDate.ofYearDay(year,min)).append(";").append(map.get(min)).append(";");

        return builder.toString();
    }

     static String getDayOfYearStrings(Map<Integer, Integer> map, Month month, int year) {
        StringBuilder builder = new StringBuilder();

        int max = SortUtil.getMaxEntitiesByDays(map);
        builder.append(LocalDate.of(year,month,max)).append(";").append(map.get(max)).append(";");

        //Меньше всего
        int min = SortUtil.getMinEntitiesByDays(map);
        builder.append(LocalDate.of(year,month,min)).append(";").append(map.get(min)).append(";");

        return builder.toString();
    }

    static String getDaysStrings(Map<LocalDate, Integer> map) {
        StringBuilder builder = new StringBuilder();

        LocalDate max = SortUtil.getMaxEntitiesByDate(map);
        builder.append(max.toString()).append(";").append(map.get(max)).append(";");

        //Меньше всего
        LocalDate min = SortUtil.getMinEntitiesByDate(map);
        builder.append(min).append(";").append(map.get(min)).append(";");

        return builder.toString();
    }

    static String getInDeatailStatusString(List<Entity> list) {
        StringBuilder builder = new StringBuilder();
        int size = list.size();

        //Принятые к расмотрению
        List<Entity> tmp = list.stream().filter(e -> (e.getStatus() != Status.NEW && e.getStatus() != Status.NONAMED)).collect(Collectors.toList());
        builder.append(tmp.size()).append(";").append(getProcent(tmp.size(),size)).append(";").append(SortUtil.averageRating(tmp)).append(";");

        //Принятые к расмотрению
        List<Entity> reseived = list.stream().filter(e -> (e.getStatus() == Status.RECEIVED || e.getStatus() == Status.ISSUED)).collect(Collectors.toList());
        builder.append(reseived.size()).append(";").append(getProcent(reseived.size(),size)).append(";").append(SortUtil.averageRating(reseived)).append(";");

        //Отклоненныек
        List<Entity> refused = list.stream().filter(e -> (e.getStatus() == Status.APPEAL || e.getStatus() == Status.REFUSE || e.getStatus() == Status.REJECT)).collect(Collectors.toList());
        builder.append(refused.size()).append(";").append(getProcent(refused.size(),size)).append(";").append(SortUtil.averageRating(refused)).append(";");

        //Не рассмотренные
        List<Entity> open = list.stream().filter(e -> e.getStatus() == Status.NEW).collect(Collectors.toList());
        builder.append(open.size()).append(";").append(getProcent(open.size(),size)).append(";").append(SortUtil.averageRating(open)).append(";");

        return builder.toString();
    }
}
