package checksocket;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static checksocket.PortReader.getIpPortsList;
import static checksocket.PortReader.sortByIp;
import static checksocket.ExcelFileCreator.createExcelReport;

public final class PortConnection {
    private static final Config CONFIG = ConfigFactory.load();

    public static void main(String[] args) throws IOException, InterruptedException {
        String inputFileName = CONFIG.getString("inputFileName");
        String outputFileName = CONFIG.getString("outputFileName");

        FileWriter outputFile = new FileWriter(outputFileName, false);

        List<Thread> threadList = new ArrayList<>();
        List<IpPorts> ipPortsList = getIpPortsList(inputFileName);

        for (IpPorts ipPorts : ipPortsList) {
            String ip = ipPorts.getIp();
            List<String> portList = ipPorts.getPortList();

            for (String port : portList) {
                threadList.add(new Thread(new SocketThread(ip, port, outputFile)));
            }
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        outputFile.close();
        sortByIp(outputFileName);
        createExcelReport(ipPortsList);
    }
}