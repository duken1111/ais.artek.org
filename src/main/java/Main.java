
import model.Entity;
import model.Status;
import repository.EntityRepository;
import repository.EntityRepositoryImp;

import repository.RegionRepository;
import repository.RegionRepositoryImpl;
import util.StatisticUtil;
import util.ToFileUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by duke on 04.03.2017.
 */
public class Main {



    public static void main(String[] args) {

        EntityRepository entityRepository = new EntityRepositoryImp();
        RegionRepository regionRepository = new RegionRepositoryImpl();
        long start = new Date().getTime();
        System.out.println("Загружено объектов: " + entityRepository.getALL().size());
        System.out.println("Объектов без оператора: " + entityRepository.getALL().stream().filter(e -> e.getRegion() == null).count());
        System.out.println("Объектов без смены: " + entityRepository.getALL().stream().filter(e-> e.getSmena() == null).count());
        System.out.println("Объектов без статуса: " + entityRepository.getALL().stream().filter(e -> e.getStatus() == Status.NONAMED).count());

        System.out.println("Максимальный возраст: " + entityRepository.getALL().stream().max((o1, o2) -> o1.getAge() - o2.getAge()).get().getAge());

        List<Entity> noNull = entityRepository.getALL().stream().filter(e -> e.getRegion()!= null).collect(Collectors.toList());
        //entityRepository.getALL().stream().filter(e -> e.getRegion() == null).forEach(System.out::println);

        //Обычная статистика в 1 файл
        //ToFileUtil.createSimpleFile("common", StatisticUtil.createCommonStatisticLines(entityRepository.getALL()));

        //Тестовый фильтр
        // List<Entity> tmp = entityRepository.getALL().stream().filter(e -> e.getRegion() == regionRepository.getByName("воронежская область")).collect(Collectors.toList());

         ToFileUtil.createSimpleFile("2017", StatisticUtil.createStatisticByYearWithParams(noNull, 2017));

        //ToFileUtil.createSimpleFile("smena",StatisticUtil.createStatisticBySmena(entityRepository.getALL()));


        long end = new Date().getTime();
        System.out.println("Выполнено за " + (end - start) + " мс");
    }
}
