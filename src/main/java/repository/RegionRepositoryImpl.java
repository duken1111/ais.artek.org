package repository;

import model.Region;
import model.Type;
import util.RegionUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DLepeshko on 17.03.2017.
 */
public class RegionRepositoryImpl implements RegionRepository {

    private List<Region> regions = RegionUtil.REGIONS;

    @Override
    public List<Region> getALL() {
        return regions;
    }

    @Override
    public List<Region> getAllByRegion(Type type) {
        return regions.stream().filter(r -> r.getType() == type).collect(Collectors.toList());
    }

    @Override
    public Region get(Region region) {
        return regions.stream().filter(r -> r.equals(region)).findFirst().orElse(null);
    }
}
