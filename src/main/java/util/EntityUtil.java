package util;

import model.Entity;
import model.Region;
import model.Smena;
import model.Status;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by duke on 04.03.2017.
 */


public class EntityUtil {
    public static final Set<Entity> ENTITIES;
    private static final String ENTITIES_FILE = "C:/java/ais.artek.org/zayavki".replace("/", File.separator);

    static {
        ENTITIES = init();
    }

    public static Set<Entity> init() {
        HashSet<Entity> tmp = new HashSet<>();

        try {

            List<Path> pathes = Files.walk(Paths.get(ENTITIES_FILE)).filter(f -> Files.isRegularFile(f)).collect(Collectors.toList());

            for(Path path : pathes) {
                Document doc = Jsoup.parse(path.toFile(), "UTF-8");

                Elements tr = doc.select("table tr");

                tmp.addAll(tr.stream().map(EntityUtil::parseEntity).collect(Collectors.toList()));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmp;
    }


    private static Entity parseEntity(Element tr) {
        int id = getId(tr);
        String name = getName(tr);
        int year = getYear(tr);
        int number = getNumber(tr);
        Region region = getRegion(tr);
        Status status = getStatus(tr);
        LocalDate date = getLocalDate(tr);
        double rating = getCurrentRating(tr);
        int age = getAge(tr);
        Smena smena = SmenaUtil.getSmenaByYearAndNumber(year,number);

        return new Entity(id,name,smena,date,status,region,rating,age);
    }

    private static int getId(Element tr) {
        Element td = tr.select("td").first();
        return Integer.parseInt(td.text());
    }

    private static String getName(Element tr) {
        Element td = tr.select("td").get(1);
        return td.text();
    }

    private static int getYear(Element tr) {
        Element td = tr.select("td").get(2);
        return Integer.parseInt(td.text());
    }

    private static int getNumber(Element tr) {
        Element td = tr.select("td").get(3);
        return Integer.parseInt(td.text());
    }

    private static Region getRegion(Element tr) {
        Element td = tr.select("td").get(4);
        Region tmp = RegionUtil.REGIONS.stream().filter(r -> r.getName().equals(td.text().toLowerCase())).findFirst().orElse(null);
        return RegionUtil.REGIONS.contains(tmp) ? tmp : null;

    }

    private static Status getStatus(Element tr) {
        Element td = tr.select("td").get(5);
        return Status.init(td.text());
    }

    private static LocalDate getLocalDate(Element tr) {
        Element td = tr.select("td").get(6);
        return TimeUtil.getLocalDateByString(td.text());
    }

    private static double getCurrentRating(Element tr) {
        Element td = tr.select("td").get(7);
        return Double.parseDouble(td.text().replace(",",".").replaceAll("\\u00A0",""));
    }

    private static int getAge(Element tr) {
        Element td = tr.select("td").get(8);
        return Integer.parseInt(td.text());
    }

}
