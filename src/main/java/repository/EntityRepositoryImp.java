package repository;

import model.Entity;
import model.Region;
import util.EntityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by duke on 09.03.2017.
 */
public class EntityRepositoryImp implements EntityRepository{
    private final static Set<Entity> ENTITY_LIST = EntityUtil.ENTITIES;

    @Override
    public List<Entity> getALL() {
        List<Entity> tmp = new ArrayList<>();
        tmp.addAll(ENTITY_LIST);
        return tmp;
    }

    @Override
    public Map<Region, List<Entity>> groupAllByRegion() {
        return getALL().stream().collect(Collectors.groupingBy(Entity::getRegion, Collectors.toList()));
    }

    @Override
    public Map<LocalDate, List<Entity>> groupAllByDate() {

        Map<LocalDate,List<Entity>> map = ENTITY_LIST.stream().collect(Collectors.groupingBy(Entity::getDate, Collectors.toList()));

        SortedMap<LocalDate, List<Entity>> sortedMap = new TreeMap<>(LocalDate::compareTo);

        sortedMap.putAll(map);

        return sortedMap;
    }

    @Override
    public Map<LocalDate, Integer> countAllEntitiesByData() {
        final Map<LocalDate, Integer> result = new TreeMap<>();
        ENTITY_LIST.forEach(e -> result.merge(e.getDate(), 1, Integer::sum));
        return result;
    }
}
