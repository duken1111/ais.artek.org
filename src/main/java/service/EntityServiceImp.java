package service;

import model.Entity;
import model.Region;
import model.Smena;
import model.Type;
import repository.EntityRepository;
import repository.EntityRepositoryImp;
import util.EntityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by duke on 05.03.2017.
 */
public class EntityServiceImp implements EntityService {
    private EntityRepository entityRepository = new EntityRepositoryImp();

    @Override
    public List<String> getALL() {
        List<String> lines = new ArrayList<>();
        lines.add("Всего заявок;" + entityRepository.getALL().size());
        return lines;
    }

    @Override
    public List<String> groupAllByRegion() {
        return null;
    }

    @Override
    public List<String> groupAllByDate() {
        return null;
    }

    @Override
    public List<String> linesAllByDate() {
        List<String> lines = new ArrayList<>();
        entityRepository.groupAllByDate().forEach((k,v) -> lines.add(k + ";" + v.size()));

        return lines;
    }

    @Override
    public List<String> linesAllByDate(LocalDate localDate) {
        List<String> lines = new ArrayList<>();
        int size = entityRepository.groupAllByDate().get(localDate).size();

        lines.add(localDate + ";" + size);

        return lines;
    }

    @Override
    public List<String> linesByAllDateAndSmena(List<Smena> smenas) {
        List<String> lines = new ArrayList<>();
        StringBuilder template = new StringBuilder(";");
        //Собираю шапку файла
        entityRepository.groupAllByDate().forEach((k,v)-> template.append(k.toString()).append(";"));
        template.append("Всего;");
        lines.add(template.toString());

        for(Smena smena : smenas) {
            StringBuilder tmp = new StringBuilder().append(smena.getNumber()).append(" смена;");
            int total = 0;
            for(Map.Entry<LocalDate, List<Entity>> entry : entityRepository.groupAllByDate().entrySet()) {
                int count = (int) entry.getValue().stream().filter(e -> e.getSmena().equals(smena)).count();
                tmp.append(count).append(";");
                total += count;
            }
            tmp.append(total).append(";");
            lines.add(tmp.toString());
        }

        StringBuilder total = new StringBuilder("Всего;");
        entityRepository.groupAllByDate().forEach((k,v) -> total.append(v.size()).append(";"));

        lines.add(total.toString());

        return lines;
    }

    @Override
    public List<String> linesByAllDateAndSmena(List<Smena> smenas, LocalDate localDate) {
        List<String> lines = new ArrayList<>();
        StringBuilder template = new StringBuilder(";").append(localDate.toString());
        lines.add(template.toString());

        List<Entity> entities = entityRepository.groupAllByDate().get(localDate);

        for(Smena smena : smenas) {
            StringBuilder tmp = new StringBuilder().append(smena.getNumber()).append(" смена;");
            tmp.append(entities.stream().filter(e -> e.getSmena().equals(smena)).count()).append(";");
            lines.add(tmp.toString());
        }

        lines.add("Всего;" + entities.size() + ";");

        return lines;
    }

    @Override
    public List<String> allLinesByRegionType() {
        List<String> lines = new ArrayList<>();
        StringBuilder template = new StringBuilder(";");
        //Собираю шапку файла
        entityRepository.groupAllByDate().forEach((k,v)-> template.append(k.toString()).append(";"));

        lines.add(template.toString());

        for(Type type : Type.values()) {
            StringBuilder tmp = new StringBuilder().append(Type.getName(type)).append(";");

            for(Map.Entry<LocalDate, List<Entity>> entry : entityRepository.groupAllByDate().entrySet()) {
                int count = (int) entry.getValue().stream().filter(e -> e.getRegion().getType() == type).count();
                tmp.append(count).append(";");
            }

            lines.add(tmp.toString());
        }

        StringBuilder total = new StringBuilder().append("Всего:;");
        entityRepository.groupAllByDate().forEach((k,v) -> total.append(v.size()).append(";"));

        lines.add(total.toString());

        return lines;
    }

    @Override
    public List<String> allLinesByRegion(List<Region> regions) {
        List<String> lines = new ArrayList<>();
        StringBuilder template = new StringBuilder(";");
        //Собираю шапку файла
        entityRepository.groupAllByDate().forEach((k,v)-> template.append(k.toString()).append(";"));

        lines.add(template.toString());

        for(Region region : regions) {
            StringBuilder tmp = new StringBuilder().append(region.getName()).append(";");

            for(Map.Entry<LocalDate, List<Entity>> entry : entityRepository.groupAllByDate().entrySet()) {
                int count = (int) entry.getValue().stream().filter(e -> e.getRegion().equals(region)).count();
                tmp.append(count).append(";");
            }

            lines.add(tmp.toString());
        }

        StringBuilder total = new StringBuilder().append("Всего:;");
        entityRepository.groupAllByDate().forEach((k,v) -> total.append(v.size()).append(";"));

        lines.add(total.toString());

        return lines;
    }

    @Override
    public List<String> fullLinesBySmena(Smena smena) {
        return null;
    }
}
