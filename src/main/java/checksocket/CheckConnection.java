package checksocket;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.Integer.parseInt;

public class CheckConnection {
    /**
     * Записывает результат доступности IP и порта в консоль и выходной файл
     * @param ip - IP
     * @param port - порт
     * @param outputFile - имя выходного файла
     * @return - возвращает поток, выполняющий проверку и записывающий результат
     */
    public static Runnable checkConnectionThread(final String ip, final String port, final FileWriter outputFile) {
        return () -> {
            String resultConnection = getConnectionResult(ip, port);

            try {
                //Запись в файл
                outputFile.write("telnet " + ";" + ip + " ;" + port + " ;" + resultConnection + "\r\n");

                //Запись в консоль
                System.out.printf("telnet %-16s %-6s %s\n", ip, port, resultConnection);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * Проверяет доступность порта
     * @param ip - IP
     * @param port - порт
     * @return - возвращает результат попытки соединения
     */
    private static String getConnectionResult(final String ip, final String port) {
        String succeed = "Succeed";
        String fail = "Fail";
        String timeout = "Fail with timeout";
        String exception = "Exception";

        try {
            Socket socket = new Socket(ip, parseInt(port));
            return succeed;

        } catch (ConnectException e) {
            return fail;

        } catch (SocketException e) {
            return timeout;

        } catch (Exception e) {
            return exception;
        }
    }
}