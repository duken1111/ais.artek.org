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

        return tmp;
    }

    @Override
    public Map<LocalDate, Integer> countAllEntitiesByData() {
        final Map<LocalDate, Integer> result = new TreeMap<>();
        allEntities.forEach(e -> result.merge(e.getDate(), 1, Integer::sum));
        return result;
    }
}
