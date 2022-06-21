import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    Main main;
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

    public SocketServer(Main _main) {
        main = _main;

        try {
            server = new ServerSocket(Settings.PORT);
            main.informationAppend("啟動成功! 正在監聽 Port: " + Settings.PORT + "\r\n");
        } catch (IOException e) {
            main.informationAppend("發生了未知的錯誤: \r\n" + e + "\r\n");
        }
    }

    @Override
    public void run() {
        while (serverStart) {
            try {
                Socket client = server.accept();

                client.setSoTimeout(1000);

                InputStream in = client.getInputStream();
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                int size = in.available();

                byte[] buffer = new byte[size];

                in.read(buffer);

                String data = new String(buffer);

                DataHandler dataHandler = new DataHandler(main, data);
                dataHandler.start();

                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Server: Apache\r\n");
                out.write("Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n");
                out.write("Access-Control-Allow-Origin: *\r\n\r\n");
                out.write("SUCCESS");

                out.flush();
                out.close();
                in.close();

                client.close();
            } catch (IOException e) {
//                main.informationAppend("Failed to fetch data.");
//                e.printStackTrace();
            }
        }
    }
}
