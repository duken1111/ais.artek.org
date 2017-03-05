import model.Region;
import service.EntityService;
import service.EntityServiceImp;
import util.EntityUtil;
import util.RegionUtil;
import util.SmenaUtil;

import java.util.Date;

/**
 * Created by duke on 04.03.2017.
 */
public class Main {



    public static void main(String[] args) {
        EntityService entityService = new EntityServiceImp();


        System.out.println("Регионов - " + RegionUtil.REGIONS.size());

        long start = new Date().getTime();

        System.out.println("Записей - " + EntityUtil.ENTITIES.size());

        long ends = new Date().getTime();

        System.out.println("Выполнено за "+ (ends - start) + "mc");

        SmenaUtil.SMENA.forEach(System.out::println);


    }
}
