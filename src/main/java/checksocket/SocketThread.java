package checksocket;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.Integer.parseInt;

public class SocketThread implements Runnable {
    private String ip;
    private String port;
    private FileWriter outputFile;

    public SocketThread(String ip, String port, FileWriter outputFile) {
        this.ip = ip;
        this.port = port;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        final String resultConnection = checkConnection(ip, port);

        try {
            outputFile.write("telnet " + ";" + ip + " ;" + port + " ;" + resultConnection + "\r\n");
            System.out.printf("telnet %-16s %-6s %s\n", ip, port, resultConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String checkConnection(final String ip, final String port) {
        final String succeed = "Succeed";
        final String fail = "Fail";
        final String timeout = "Fail with timeout";
        final String exception = "Exception";

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