package util;

import model.Region;
import model.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by duke on 04.03.2017.
 */

public class RegionUtil {
    public static final List<Region> REGIONS;
    private static final String REGIONS_FILE = "src/main/resources/regions.html".replace("/", File.separator);

    static {
        REGIONS = RegionUtil.init();
    }

    public static List<Region> init() {
        List<Region> tmp = new ArrayList<>();

        try {
            Document doc = Jsoup.parse(Paths.get(REGIONS_FILE).toFile(),"UTF-8");

            Elements tr = doc.select("table tr");

            tmp = tr.stream().map(RegionUtil::parseRegion).collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmp;
    }

    private static Region parseRegion(Element tr) {
        String name = getName(tr);
        Type type = getType(tr);

        return new Region(name, type);
    }

    private static String getName(Element tr) {
        Element td = tr.select("td").get(0);
        return td.text().toLowerCase();
    }

    private static Type getType(Element tr) {
        Element td = tr.select("td").get(1);
        return Type.init(td.text().toLowerCase());
    }
}
