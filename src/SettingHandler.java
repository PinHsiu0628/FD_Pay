import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SettingHandler {
    File file = new File("./settings.txt");

    public void saveSettings() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            String data = "// 監聽 PORT\n" +
                    Settings.PORT + "\n" +
                    "// 開啟時是否自動啟動\n" +
                    Settings.AUTO_START;
            char[] dataArray = data.toCharArray();
            fileWriter.write(dataArray);
            fileWriter.close();
        } catch (Exception e2) {
            System.exit(0);
        }
    }

    public void readSettings() {
        try {
            FileReader fileReader = new FileReader(file);
            char[] data = new char[1024];
            int num = fileReader.read(data);
            String string = new String(data, 0, num);
            String[] settings = string.split("\n");
            Settings.PORT = Integer.valueOf(settings[1]);
            Settings.AUTO_START = Boolean.valueOf(settings[3]);
            fileReader.close();
        } catch (Exception e1) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                String data = "// 監聽 PORT\n" +
                        "8080\n" +
                        "// 開啟時是否自動啟動\n" +
                        "false";
                char[] dataArray = data.toCharArray();
                fileWriter.write(dataArray);
                fileWriter.close();
            } catch (Exception e2) {
                System.exit(0);
            }
        }
    }

    public SettingHandler() {

    }
}
