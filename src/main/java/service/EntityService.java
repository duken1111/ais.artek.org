package service;

import model.Entity;
import model.Region;
import model.Smena;
import model.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by duke on 05.03.2017.
 */
public interface EntityService {

    // Все получаю все переданные элементы
    List<String> getALL();

    //Групирую все записи по Региону,
    List<String> groupAllByRegion();

    //группирую все записи по дате
    List<String> groupAllByDate();

    //Число заявок за каждый день
    List<String> linesAllByDate();

    //Число заявок за конкретный день
    List<String> linesAllByDate(LocalDate localDate);

    //Число заявок за каждый день по сменам
    List<String> linesByAllDateAndSmena(List<Smena> smenas);
    //Число заявок на конкретный день день по сменам
    List<String> linesByAllDateAndSmena(List<Smena> smenas, LocalDate localDate);

    //Кол-во заявок за все дни, по регионам
    List<String> allLinesByRegionType();

    List<String> allLinesByRegion(List<Region> regions);

    //Полный отчет за смену как я делал
    List<String> fullLinesBySmena(Smena smena);
}
