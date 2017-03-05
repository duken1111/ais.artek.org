package service;

import model.Entity;
import model.Region;
import util.EntityUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Region, List<Entity>> groupeAllByRegion() {


        return null;
    }

    @Override
    public Map<LocalDate, List<Entity>> groupeAllByDate() {
        final Map<LocalDate, Integer> result = new HashMap<>();


        allEntities.forEach(e -> result.merge(e.getDate(), 1, Integer::sum));
        int total = 0;

        for(Map.Entry<LocalDate,Integer> entry : result.entrySet()) {
            //System.out.println(entry.getKey() + ": " + entry.getValue());
            total += entry.getValue();
        }

        System.out.println(total);

        return null;
    }
}
