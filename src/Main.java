import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    JButton startButton = new JButton("啟動金流");
    JButton restartButton = new JButton("重啟金流");
    JButton settingsButton = new JButton("設定");
    JButton exitButton = new JButton("離開");
    JTextArea information = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(information);
    SocketServer socketServer;

    public void informationAppend(String msg) {
        information.setText(information.getText() + msg + "\r\n");

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void start() {
        informationAppend("金流啟動中......");
        socketServer = new SocketServer(this);
        socketServer.start();
    }

    private void setupJFrame() {
        getContentPane();
        setBounds(300, 300, 600, 600);
        setTitle("F&D金流系統 光亮谷");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
    }

    private void frontendSet() {
        Main strongThis = this;

        startButton.setBounds(10, 20, 100, 40);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        add(startButton);

        restartButton.setBounds(120, 20, 100, 40);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                socketServer.end();
                socketServer = null;
                start();
            }
        });
        add(restartButton);

        settingsButton.setBounds(230, 20, 100, 40);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingPanel settingPanel = new SettingPanel(strongThis);
            }
        });
        add(settingsButton);

        exitButton.setBounds(340, 20, 100, 40);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

        information.setFont(new Font(null, Font.PLAIN, 16));

        scrollPane.setBounds(10, 80, 565, 470);
        add(scrollPane);
    }

    public Main() {
        setupJFrame();
        frontendSet();

        setVisible(true);
    }

    public static void main(String[] args) {
        SettingHandler settingHandler = new SettingHandler();
        settingHandler.readSettings();
        Main main = new Main();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            main.informationAppend("成功載入JDBC Driver!");
        } catch (ClassNotFoundException e) {
            main.informationAppend("JDBC Driver載入失敗!");
        }

        if (Settings.AUTO_START) {
            main.start();
        }
    }
}
