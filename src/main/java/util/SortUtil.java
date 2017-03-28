package util;

import model.Entity;
import model.Region;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by duke on 15.03.2017.
 */
public class SortUtil {

    public static Region getMaxEntitiesByRegion(Map<Region, Integer> map) {
        return Collections.max(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static Region getMinEntitiesByRegion(Map<Region, Integer> map) {
        return Collections.min(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static int getMaxEntitiesByDays(Map<Integer, Integer> map) {
        return Collections.max(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static int getMinEntitiesByDays(Map<Integer, Integer> map) {
        return Collections.min(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static Month getMaxEntitiesByMonth(Map<Month, Integer> map) {
        return Collections.max(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static Month getMinEntitiesByMonth(Map<Month, Integer> map) {
        return Collections.min(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static LocalDate getMaxEntitiesByDate(Map<LocalDate, Integer> map) {
        return Collections.max(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static LocalDate getMinEntitiesByDate(Map<LocalDate, Integer> map) {
        return Collections.min(map.entrySet(),(e1,e2) -> e1.getValue() - e2.getValue()).getKey();
    }

    public static String averageRating(List<Entity> list) {
        double average = list.stream().collect(Collectors.summarizingDouble(Entity::getRating)).getAverage();
        return String.format("%.1f", average);
    }

    public static String averageAge(List<Entity> list) {
        double average = list.stream().collect(Collectors.summarizingDouble(Entity::getAge)).getAverage();
        return String.format("%.1f", average);
    }

}
