package checksocket;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class PortReader {
    static List<IpPorts> getIpPortsList(final String fileName) throws IOException {
        return Files
                .lines(Paths.get(fileName))
                .filter(line -> !line.equals(""))
                .map(line -> {
                    String[] ipAndPorts = line.split(";");

                    String ip = ipAndPorts[0];
                    List<String> portList = Arrays.asList(ipAndPorts[1].split(","));

                    return new IpPorts.IpPortsBuilder()
                            .withIp(ip)
                            .withPortList(portList)
                            .build();
                })
                .collect(toList());
    }

    static void sortByIp(final String fileName) throws IOException {
        List<String> recordList = Files
                .lines(Paths.get(fileName))
                .sorted()
                .collect(toList());

        FileWriter outputFile = new FileWriter(fileName, false);

        for (String record : recordList) {
            outputFile.write(record + "\r\n");
        }

        outputFile.close();

    }
}
