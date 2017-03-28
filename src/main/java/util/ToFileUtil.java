package util;


import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

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
    private static CellStyle dateFormater;



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
        HSSFWorkbook result = new HSSFWorkbook();
        //Формат даты

        
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
            row.setHeightInPoints(16);

            String tmp = lines.get(i);

            if(tmp.contains(";")) {
                String[] values = tmp.split(";");

                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j);
                    setCellStyle(cell,values[j]);
                    cell.setCellStyle(cell.getRow().getRowStyle());

                    createStringSell(cell,values[j]);
                    //Стиль ячейки

                }

            } else {
                Cell cell = row.createCell(0);
                createStringSell(cell,tmp);
            }

        }
    }

    private static void setCellStyle(Cell cell, String value) {
        if(value.matches("(<.+>).*")) {

            String[] params = value.substring(1,value.lastIndexOf(">")).split(",");
            for(String param : params) {
                //Нужно сделать верхнее и нижнее слияние
               String subParam = param.substring(0,1);
               switch (subParam) {
                   case "b":
                       setBoldRow(cell);
                       break;
                   case "a":
                       setAlignRow(cell);
                       break;
                   case "w":
                       parseStringToMergeCell(cell,param);
                       break;
                   case "h":
                       parseStringToMergeCell(cell,param);
                       break;
                   case "r":
                       setRowHeight(cell,param);
                       break;
               }
            }
        }
    }

    private static void setRowHeight(Cell cell, String param) {
        if(param.length() > 1) {
            try {
                int number = Integer.parseInt(param.substring(1));
                cell.getRow().setHeightInPoints(number);
            } catch (NumberFormatException e) {
            }
        }
    }

    private static void mergeCells(Cell cell, int width, int height) {
        int row = cell.getRow().getRowNum();
        int current = cell.getColumnIndex();
        cell.getSheet().addMergedRegion(new CellRangeAddress(row-height,row,current-width,current));
    }

    private static void parseStringToMergeCell(Cell cell, String param) {
        if(param.length() > 1) {
            try {
                int number = Integer.parseInt(param.substring(1));
                switch (param.substring(0,1)) {
                    case "w":
                        mergeCells(cell,number,0);
                        break;
                    case "h":
                        mergeCells(cell,0,number);
                        break;
                }
            } catch (NumberFormatException e) {}
        }
    }

    private static void setAlignRow(Cell cell) {
        CellStyle myStyle = getCellStyle(cell);
        myStyle.setAlignment(CellStyle.ALIGN_CENTER);
        myStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        myStyle.setWrapText(true);
        cell.setCellStyle(myStyle);

        cell.getRow().setRowStyle(myStyle);
    }

    private static void setBoldRow(Cell cell) {
        CellStyle myStyle = getCellStyle(cell);
        HSSFFont font = (HSSFFont) cell.getSheet().getWorkbook().createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        myStyle.setFont(font);
        cell.setCellStyle(myStyle);
        cell.getRow().setRowStyle(myStyle);

    }

    private static CellStyle getCellStyle(Cell cell) {
        if(cell.getRow().getRowStyle() == null)
            return cell.getSheet().getWorkbook().createCellStyle();
        else
            return cell.getCellStyle();
    }

    private static void createDateSell(Cell cell, LocalDate date) {
        //Форматер для даты
        if(dateFormater == null) {
            DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
            dateFormater = cell.getSheet().getWorkbook().createCellStyle();
            dateFormater.setDataFormat(format.getFormat("dd.mm.yyyy"));
        }

        cell.setCellStyle(dateFormater);
        cell.setCellValue(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private static void createStringSell(Cell cell, String name) {
        if(name.matches("(\\d[,]?)+")) {
            double number = Double.parseDouble(name.replace(",","."));
            cell.setCellValue(number);
        } else if(name.matches("(\\d){4}-(\\d){2}-(\\d){2}")) {
            createDateSell(cell, LocalDate.parse(name));
        } else if (name.matches("(<.+>).*")) {
            cell.setCellValue(name.replaceAll("(<.+>)",""));
        } else {
            cell.setCellValue(name);
        }
    }


}
