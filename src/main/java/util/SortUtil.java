package util;

import model.Region;

import java.util.Collections;
import java.util.Map;

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

}
