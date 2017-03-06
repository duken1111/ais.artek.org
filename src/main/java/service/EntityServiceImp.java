package service;

import model.Entity;
import model.Region;
import util.EntityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by duke on 05.03.2017.
 */
public class EntityServiceImp implements EntityService {
    private List<Entity> allEntities = EntityUtil.ENTITIES;

    @Override
    public List<Entity> getALL() {
        return allEntities;
    }

    @Override
    public Map<Region, List<Entity>> groupAllByRegion() {


        return null;
    }

    @Override
    public Map<LocalDate, List<Entity>> groupAllByDate() {

        Map<LocalDate,List<Entity>> tmp = allEntities.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.toList()));

        SortedMap<LocalDate, List<Entity>> sortedMap = new TreeMap<>(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate o1, LocalDate o2) {
                return o1.compareTo(o2);
            }
        });
        sortedMap.putAll(tmp);

        return sortedMap;
    }

    @Override
    public Map<LocalDate, Integer> countAllEntitiesByData() {
        final Map<LocalDate, Integer> result = new TreeMap<>();
        allEntities.forEach(e -> result.merge(e.getDate(), 1, Integer::sum));
        return result;
    }
}
