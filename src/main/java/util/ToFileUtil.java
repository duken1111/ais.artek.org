package util;

import model.Entity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by duke on 06.03.2017.
 */
public class ToFileUtil {
    private static final String RESULT_PATH = "src/main/resources/result/".replace("/", File.separator);


    public static void createAllEntitesCount(List<Entity> entities) {
        Path file = createFile("all");
        List<String> lines = new ArrayList<>();
        lines.add("Всего заявок;" + entities.size());

        save(file.toFile(),lines);
    }

    public static void createAllEntitiesByDateCount(Map<LocalDate,Integer> map) {
        Path file = createFile("AllEntitiesByDateCount");
        List<String> lines = new ArrayList<>();

        map.forEach((k,v) -> lines.add(k + ";" + v));

        save(file.toFile(),lines);
    }

    private static Path createFile(String string) {
        String fullName = string + ".csv";
        try {
            return Files.createFile(Paths.get(RESULT_PATH + fullName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void save(File file, List<String> lines) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            lines.forEach(l-> writeLine(writer,l));

        } catch (IOException e) {
            System.out.println(file.getName() + "чтото пошло не так");
        }
    }

    private static void writeLine(Writer writer,String line) {
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
