import model.Type;
import service.EntityService;
import service.EntityServiceImp;
import util.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
        System.out.println("Объектов без регионов: " + EntityUtil.ENTITIES.stream().filter(e -> e.getRegion() == null).count());
        System.out.println("Объектов без смены: " + EntityUtil.ENTITIES.stream().filter(e -> e.getSmena() == null).count());
        System.out.println("Объектов без статуса: " + EntityUtil.ENTITIES.stream().filter(e -> e.getStatus() == null).count());
        System.out.println();
        System.out.println();
        //ПРосто посчитать все заявки в бд и записатб в файл
        ToFileUtil.createFile("all",entityService.getALL());

        //Подсчет всех заявок по датам
        ToFileUtil.createFile("countALLByDate", entityService.linesAllByDate());

        //Числозаявок за конкретный день
        LocalDate date = TimeUtil.getLocalDateByString("01.03.2017");
        ToFileUtil.createFile("countALLByDate-" + date.toString(), entityService.linesAllByDate(date));

        //Кол-во заявок по сменно на каждый день
        ToFileUtil.createFile("countBySmena",entityService.linesByAllDateAndSmena(SmenaUtil.SMENA));

        //кол-во заявок по сменно на конкретный день
        ToFileUtil.createFile("countBySmena-" + date.toString(),entityService.linesByAllDateAndSmena(SmenaUtil.SMENA,date));

        //кол-во заявок по типу Регионов и партнеров на каждый день
        ToFileUtil.createFile("countByRegionType",entityService.allLinesByRegionType());

        //кол-во заявок по всем операторам на каждый день
        ToFileUtil.createFile("countByAllRegions",entityService.allLinesByRegion(RegionUtil.REGIONS));

        long ends = new Date().getTime();
        System.out.println();
        System.out.println("Время выполнения: " + (ends-start) + " мс");
    }
}
