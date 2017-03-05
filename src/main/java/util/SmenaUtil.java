package util;

import model.Region;
import model.Smena;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by duke on 05.03.2017.
 */
public class SmenaUtil {

    public static final List<Smena> SMENA;
    private static final String SMENA_FILE = "src/main/resources/smena.csv".replace("/", File.separator);

    static {
        SMENA = SmenaUtil.init();
    }

    public static List<Smena> init() {
        List<Smena> tmp = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(SMENA_FILE))) {
            tmp = reader.lines().filter(s -> s.length() > 0).map(SmenaUtil::parseSmena).collect(Collectors.toList());


        } catch (FileNotFoundException e) {
            System.out.println("Нету такого файла: " + SMENA_FILE);
        } catch (IOException e) {
            System.out.println("Чтото пошло не так");
        }

        return tmp;
    }

    public static Smena getSmenaByYearAndNumber(int year, int number){
        return SMENA.stream().filter(s -> s.isExists(year,number)).findFirst().get();
    }

    private static Smena parseSmena(String line) {
        String name = getName(line);
        int year = getYear(line);
        int number = getNumber(line);
        int quota = getQuota(line);

        return new Smena(name,year,number,quota);
    }

    private static String getName(String line) {
        return line.split(";")[0];
    }

    private static int getYear(String line) {
        return Integer.parseInt(line.split(";")[1]);
    }

    private static int getNumber(String line) {
        return Integer.parseInt(line.split(";")[2]);
    }

    private static int getQuota(String line) {
        return Integer.parseInt(line.split(";")[3]);
    }


}
