package checksocket;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static checksocket.PortReader.getIpPortsList;
import static checksocket.PortReader.sortByIp;
import static checksocket.ExcelFileCreator.createExcelReport;
import static checksocket.CheckConnection.checkConnectionThread;

public final class PortConnection {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = ConfigFactory.load();
        String inputFileName = config.getString("inputFileName");
        String outputFileName = config.getString("outputFileName");

        //Создание выходного файла с удалением содержимого
        FileWriter outputFile = new FileWriter(outputFileName, false);

        //Создание списка потоков
        List<Thread> threadList = new LinkedList<>();

        //Получение списка IP и портов к нему из входного файла
        List<IpPorts> ipPortsList = getIpPortsList(inputFileName);

        //Пробег по всем комбинациям IP-порт
        for (IpPorts ipPorts : ipPortsList) {
            String ip = ipPorts.getIp();
            List<String> portList = ipPorts.getPortList();

            for (String port : portList) {
                //Под каждую проверку доступа создаётся отдельный поток и добавляется в список
                threadList.add(new Thread(checkConnectionThread(ip, port, outputFile)));
            }
        }

        //Запуск всех потоков
        for (Thread thread : threadList) {
            thread.start();
        }

        //Ожидание окончания каждого потока
        for (Thread thread : threadList) {
            thread.join();
        }

        //Закрытие выходного файла
        outputFile.close();

        //Сортировка строк в выходном файле
        sortByIp(outputFileName);

        //Создание экселевского документа с результатами проверки доступности портов
        createExcelReport(ipPortsList);
    }
}