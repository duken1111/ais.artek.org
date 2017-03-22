package repository;

import model.Region;
import model.Type;

import java.util.List;

/**
 * Created by DLepeshko on 17.03.2017.
 */
public interface RegionRepository {
    // Все получаю все переданные элементы
    List<Region> getALL();

    //Групирую все записи по Региону,
    List<Region> getAllByRegion(Type type);

    Region get(Region region);
}
