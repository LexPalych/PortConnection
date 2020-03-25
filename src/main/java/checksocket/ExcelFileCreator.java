package checksocket;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelFileCreator {
    private static final Config CONFIG = ConfigFactory.load();
    private static final XSSFWorkbook WORKBOOK = new XSSFWorkbook();
    private static final XSSFSheet SHEET = WORKBOOK.createSheet("Connection result");

    /**
     * Создание экселевского документа с результатами доступности портов
     * @param ipPortsList - список из IP и портов к нему
     */
    public static void createExcelReport(List<IpPorts> ipPortsList) throws IOException {
        String excelTableFileName = CONFIG.getString("ExcelTableFileName");

        //Установка размера крайней левой колонки
        SHEET.setColumnWidth(0, 5000);

        //Создание информационной строки
        Row infoRow = SHEET.createRow(0);

        //Заполнение информационной строки
        createCell(infoRow, 0, "IP", IndexedColors.LIGHT_BLUE);
        createCell(infoRow, 1, "Succeed", IndexedColors.BRIGHT_GREEN);
        createCell(infoRow, 2, "Fail", IndexedColors.CORAL);
        createCell(infoRow, 3, "Timeout", IndexedColors.ORANGE);
        createCell(infoRow, 4, "Exception", IndexedColors.LIGHT_ORANGE);

        //Создание таблицы с результатами доступности портов
        createConnectionResultTable(ipPortsList);

        //Запись в файл
        WORKBOOK.write(new FileOutputStream(excelTableFileName));

        //Закрытие файла
        WORKBOOK.close();
    }

    /**
     * Создание ячейки
     * @param row - строка
     * @param cellNumber - номер яцейки в строке
     * @param value - значение ячейки
     * @param indexedColors - цвет ячейки
     */
    private static void createCell(Row row, int cellNumber, String value, IndexedColors indexedColors) {
        CellStyle cellStyle = getDefaultCellStyle();
        cellStyle.setFillForegroundColor(indexedColors.getIndex());

        Cell cell = row.createCell(cellNumber);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    /**
     * Создание таблицы с результатами доступности портов
     * @param ipPortsList - список из IP и портов к нему
     */
    private static void createConnectionResultTable(List<IpPorts> ipPortsList) throws IOException {
        int rowNumber = SHEET.getLastRowNum() + 2;

        for (IpPorts ipPorts : ipPortsList) {
            Row row = SHEET.createRow(rowNumber++);

            String ip = ipPorts.getIp();
            createCell(row, 0, ip, IndexedColors.LIGHT_BLUE);

            List<String> portList = ipPorts.getPortList();

            for (int j = 0; j < portList.size(); j++) {
                String port = portList.get(j);
                createCell(row, j + 1, port, getCellColor(ip, port));
            }
        }
    }

    /**
     * Получение цвета ячейки в зависимости от результата проверки доступа
     * @param ip - IP
     * @param port - порт
     * @return - возвращает цвет ячейки
     */
    private static IndexedColors getCellColor(String ip, String port) throws IOException {
        String outputFileName = CONFIG.getString("outputFileName");

        String resultLine = Files
                .lines(Paths.get(outputFileName), StandardCharsets.UTF_8)
                .filter(line -> line.contains("telnet ;" + ip + " ;" + port + " ;"))
                .findAny()
                .get();

        String result = Arrays.asList(resultLine.split(";")).get(3);

        switch (result) {
            case "Succeed": {
                return IndexedColors.BRIGHT_GREEN;
            }

            case "Fail": {
                return IndexedColors.CORAL;
            }

            case "Fail with timeout": {
                return IndexedColors.ORANGE;
            }

            default: {
                return IndexedColors.LIGHT_ORANGE;
            }
        }
    }

    /**
     * Получение стиля ячейки с параметрами по умолчанию
     * @return - возвращает стиль яцейки
     */
    private static CellStyle getDefaultCellStyle() {
        CellStyle cellStyle = WORKBOOK.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return cellStyle;
    }
}
