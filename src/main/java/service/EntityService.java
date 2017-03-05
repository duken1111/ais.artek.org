package service;

import model.Entity;
import model.Region;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by duke on 05.03.2017.
 */
public interface EntityService {

    // Все получаю все переданные элементы
    List<Entity> getALL();

    //Групирую все записи по Региону,
    Map<Region, List<Entity>> groupeAllByRegion();

    //группирую все записи по дате
    Map<LocalDate, List<Entity>> groupeAllByDate();
}
