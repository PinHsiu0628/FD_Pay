import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingPanel extends JFrame {
    Main main;
    SettingHandler settingHandler = new SettingHandler();
    JLabel portLabel = new JLabel("Port: ");
    JTextField portInput = new JTextField();
    JLabel autoStartLabel = new JLabel("自動啟動: ");
    JCheckBox autoStartCheckBox = new JCheckBox();
    JButton readButton = new JButton("讀取");
    JButton saveButton = new JButton("儲存");

    public void setUpValues() {
        portInput.setText("" + Settings.PORT);
        autoStartCheckBox.setSelected(Boolean.valueOf(Settings.AUTO_START));
    }

    private void setupJFrame() {
        getContentPane();
        setBounds(300, 300, 450, 450);
        setTitle("F&D金流系統 光亮谷");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
    }

    private void frontendSet() {
        SettingPanel strongThis = this;

        portLabel.setBounds(10, 10, 100, 50);
        portLabel.setFont(new Font(null, Font.PLAIN, 16));
        add(portLabel);

        portInput.setBounds(50, 25, 100, 25);
        add(portInput);

        autoStartLabel.setBounds(10, 50, 100, 50);
        autoStartLabel.setFont(new Font(null, Font.PLAIN, 16));
        add(autoStartLabel);

        autoStartCheckBox.setBounds(80, 65, 25, 25);
        add(autoStartCheckBox);

        readButton.setBounds(275, 380, 75, 25);
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingHandler.readSettings();

                setUpValues();

                JOptionPane.showMessageDialog(strongThis, "讀取成功!");
            }
        });
        add(readButton);

        saveButton.setBounds(355, 380, 75, 25);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.PORT = Integer.valueOf(portInput.getText());
                Settings.AUTO_START = autoStartCheckBox.isSelected();

                settingHandler.saveSettings();

                JOptionPane.showMessageDialog(strongThis, "儲存成功!");
            }
        });
        add(saveButton);
    }

    public SettingPanel(Main _main) {
        main = _main;

        setupJFrame();
        frontendSet();
        setUpValues();

        setVisible(true);
    }
}
