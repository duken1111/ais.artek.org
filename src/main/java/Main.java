
import repository.EntityRepository;
import repository.EntityRepositoryImp;

import repository.RegionRepository;
import repository.RegionRepositoryImpl;
import util.*;


import java.util.Date;

/**
 * Created by duke on 04.03.2017.
 */
public class Main {



    public static void main(String[] args) {

        EntityRepository entityRepository = new EntityRepositoryImp();
        RegionRepository regionRepository = new RegionRepositoryImpl();
        long start = new Date().getTime();

        //Обычная статистика в 1 файл
        ToFileUtil.createSimpleFile("common", StatisticUtil.createCommonStatisticLines(entityRepository.getALL()));


        long end = new Date().getTime();
        System.out.println("Выполнено за " + (end - start) + " мс");
    }
}
