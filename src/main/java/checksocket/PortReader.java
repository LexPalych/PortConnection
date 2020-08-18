package checksocket;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PortReader {
    /**
     * Считывает из файла список IP и портов к нему
     * @param fileName - имя файла
     * @return - возвращает список из IP и портов к нему
     * @throws IOException
     */
    static List<IpPorts> getIpPortsList(final String fileName) throws IOException {
        List<String> inputFile = readFile(fileName);
        List<IpPorts> ipPortsList = new LinkedList<>();
        String description = null;

        for (String line : inputFile) {
            if (line.contains("//")) {
                description = line.substring(2);

            } else if (line.equals("")) {
                description = "-";

            } else {
                String[] ipAndPorts = line.split(";");

                String ip = ipAndPorts[0];
                List<String> portList = Arrays.asList(ipAndPorts[1].split(","));

                IpPorts ipPorts = new IpPorts.IpPortsBuilder()
                        .withDescription(description)
                        .withIp(ip)
                        .withPortList(portList)
                        .build();

                ipPortsList.add(ipPorts);
            }
        }

        return ipPortsList;
    }

    /**
     * Считывает csv-файл
     * @param fileName - имя файла
     * @return - возвращает список строк из файла
     * @throws IOException
     */
    private static List<String> readFile(final String fileName) throws IOException {
        return Files
                .lines(Paths.get(fileName), StandardCharsets.UTF_8)
                .collect(Collectors.toList());
    }

    /**
     * Выполняет сортировку строк в файле
     * @param fileName - имя файла
     * @throws IOException
     */
    static void sortByIp(final String fileName) throws IOException {
        List<String> recordList = Files
                .lines(Paths.get(fileName))
                .sorted()
                .collect(Collectors.toList());

        FileWriter outputFile = new FileWriter(fileName, false);

        for (String record : recordList) {
            outputFile.write(record + "\r\n");
        }

        outputFile.close();

    }
}