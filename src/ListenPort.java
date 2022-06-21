import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ListenPort extends Thread {
    Main main;
    int serverPort = 8080;
    boolean serverStart = true;
    ServerSocket server;

    public void end() {
        serverStart = false;
        try {
            server.close();
        } catch (Exception e) {
            main.informationAppend("發生了未知的錯誤: \r\n" + e + "\r\n");
        }
    }

    public ListenPort(Main _main) {
        main = _main;

        try {
            server = new ServerSocket(serverPort);
            main.informationAppend("啟動成功! 正在監聽 Port: " + serverPort + "\r\n");
        } catch (IOException e) {
            main.informationAppend("發生了未知的錯誤: \r\n" + e + "\r\n");
        }
    }

    @Override
    public void run() {
        while (serverStart) {
            try {
                Socket socket = server.accept();

                socket.setSoTimeout(100);

                InputStream in = socket.getInputStream();

                int size = in.available();
                byte[] buffer = new byte[size];

                in.read(buffer);

                String data = new String(buffer);

                String[] req = data.split("\n");

                data = req[req.length - 1];

                main.informationAppend(data);

                OutputStream os = socket.getOutputStream();
                os.write("Test".getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
