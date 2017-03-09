package util;

import model.Entity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by duke on 06.03.2017.
 */
public class ToFileUtil {
    private static final String RESULT_PATH = "src/main/resources/result/".replace("/", File.separator);


    public static void createFile(String fileName, List<String> lines) {
        Path file = makeFile(fileName);
        save(file.toFile(),lines);
        System.out.println("OK! В файл "+ file.getFileName().toString() + " записано " + lines.size() + " строк");
    }


    private static Path makeFile(String string) {
        String fullName = string + ".csv";
        Path file = Paths.get(RESULT_PATH + fullName);
        try {
            if(Files.exists(file)) {
                return file;
            } else {
                return Files.createFile(file);
            }

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
