package checksocket;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static checksocket.PortReader.getIpPortsList;
import static checksocket.PortReader.sortByIp;

public class PortConnection {
    public static void main(String[] args) throws IOException, InterruptedException {
        String inputFileName = "input.txt";
        String outputFileName = "output.csv";

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
    }
}
