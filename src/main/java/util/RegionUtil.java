package util;

import model.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by duke on 04.03.2017.
 */

public class RegionUtil {
    public static final List<Region> REGIONS;
    private static final String REGIONS_FILE = "src/main/resources/regions.csv".replace("/", File.separator);

    static {
        REGIONS = RegionUtil.init();
    }

    public static List<Region> init() {
        List<Region> tmp = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(REGIONS_FILE))) {
            tmp = reader.lines().filter(s -> s.length() > 0).map(Region::new).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            System.out.println("Нету такого файла: " + REGIONS_FILE);
        } catch (IOException e) {
            System.out.println("Чтото пошло не так");
        }

        return tmp;
    }
}
