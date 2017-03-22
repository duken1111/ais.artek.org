package util;

import model.Entity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;


/**
 * Created by duke on 06.03.2017.
 */
public class ToFileUtil {
    private static final String RESULT_PATH = "C:/Java/ais.artek.org/result/".replace("/", File.separator);
    private static CellStyle style;


    public static void createSimpleFile(String fileName, Map<String, List<String>> sheets) {
        Path file = makeFile(fileName);
        try {
            save(file.toFile(), sheets);
        } catch (IOException e) {
            System.out.println("ОШИБКА ЗАПИСИ В ФАЙЛ!!!");
            e.printStackTrace();
        }
        System.out.println("OK! В файл "+ file.getFileName().toString() + " записано");
    }


    private static Path makeFile(String string) {
        String fullName = string + ".xls";
        Path file = Paths.get(RESULT_PATH + fullName);
        System.out.println(file.toString());
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

    private static void save(File file, Map<String, List<String>> sheets) throws IOException {
        //создаем документ exel
        Workbook result = new HSSFWorkbook();
        //Формат даты
        DataFormat format = result.createDataFormat();
        style = result.createCellStyle();
        style.setDataFormat(format.getFormat("dd.mm.yyyy"));
        
        //Создаем лист состатистикой List<String> lines
        for(Map.Entry<String, List<String>> entry : sheets.entrySet()) {
            Sheet statistic = result.createSheet(entry.getKey());
            createStatisticList(statistic,entry.getValue());
        }

        

        result.write(new FileOutputStream(file));
        result.close();
    }
    
    //Лист со статистикой, передаем лист Стринuов разделитель ;
    private static void createStatisticList(Sheet statistic, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Row row = statistic.createRow(i);
            String tmp = lines.get(i);

            if(tmp.contains(";")) {
                String[] values = tmp.split(";");

                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j);
                    createStringSell(cell,values[j]);
                }

            } else {
                Cell cell = row.createCell(0);
                createStringSell(cell,tmp);
            }

        }
    }

    private static void createDateSell(Cell cell, LocalDate date) {
        //Форматер для даты
        cell.setCellStyle(style);
        cell.setCellValue(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private static void createStringSell(Cell cell, String name) {
        if(name.matches("(\\d[,]?)+")) {
            double number = Double.parseDouble(name.replace(",","."));
            cell.setCellValue(number);
        } else if(name.matches("(\\d){4}-(\\d){2}-(\\d){2}")) {
            createDateSell(cell, LocalDate.parse(name));
        } else {
            cell.setCellValue(name);
        }
    }

}
