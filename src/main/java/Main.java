import model.Entity;
import model.Region;
import model.Type;
import service.EntityService;
import service.EntityServiceImp;
import util.EntityUtil;
import util.RegionUtil;
import util.SmenaUtil;
import util.ToFileUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by duke on 04.03.2017.
 */
public class Main {



    public static void main(String[] args) {
        EntityService entityService = new EntityServiceImp();
        long start = new Date().getTime();

        System.out.println("Регионов и партнеров - " + RegionUtil.REGIONS.size());

        System.out.println("Загруженно объектов - " + EntityUtil.ENTITIES.size());
        System.out.println();
        System.out.println("Объекстов без регионов: " + entityService.getALL().stream().filter(e -> e.getRegion() == null).count());
        System.out.println("Объекстов без смены: " + entityService.getALL().stream().filter(e -> e.getSmena() == null).count());
        System.out.println("Объекстов без статуса: " + entityService.getALL().stream().filter(e -> e.getStatus() == null).count());
        System.out.println();





        ToFileUtil.createAllEntitiesByDateCount(entityService.countAllEntitiesByData());


        long ends = new Date().getTime();

        System.out.println("Время выполнения: " + (ends-start) + " мс");
    }
}
