package util;

import model.Region;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by duke on 04.03.2017.
 */

public class RegionUtil {
    public static final List<Region> REGIONS;
    private static final Path REGIONS_FILE = Paths.get("web.csv");

    static {
        REGIONS = RegionUtil.init();
    }

    private static List<Region> init() {

        try(BufferedReader reader = new BufferedReader(new FileReader(REGIONS_FILE.toFile()))) {

        } catch (FileNotFoundException e) {
            System.out.println("Нету такого файла");
        } catch (IOException e) {
            System.out.println("Чтото пошло не так");
        }

        return null;
    }
}
