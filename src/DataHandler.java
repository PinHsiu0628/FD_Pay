import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHandler extends Thread {
    Main main;
    String fullData;

    public DataHandler(Main _main, String _data) {
        main = _main;
        fullData = _data;
    }

    @Override
    public void run() {
        String[] tempString = fullData.split("\n");
        String[] data = tempString[tempString.length - 1].split("&");

        if (data.length != 3) {
            return;
        }

        String verifyCode = data[0];
        String tradeNo = data[1];
        String isAdmin = data[2];

        if (verifyCode.equals("fdLightStory")) {
            String databaseURL = "jdbc:mysql://localhost:3306/v113";
            try {
                Connection conn = DriverManager.getConnection(databaseURL, "root", "");

                PreparedStatement stmtSelectOrder = conn.prepareStatement("SELECT `account`, `price`, `amount`, `product`, `status` FROM `smilepay` WHERE `trade_no` = ?");
                stmtSelectOrder.setString(1, tradeNo);

                ResultSet resultSelectOrder = stmtSelectOrder.executeQuery();

                while (resultSelectOrder.next()) {
                    if (resultSelectOrder.getInt("status") == 0) {
                        PreparedStatement updateOrder = conn.prepareStatement("UPDATE `smilepay` SET `status` = '1', `admin` = ? WHERE `trade_no` = ?");

                        updateOrder.setString(1, isAdmin);
                        updateOrder.setString(2, tradeNo);

                        updateOrder.execute();

                        String playerAccount = resultSelectOrder.getString("account");
                        int price = resultSelectOrder.getInt("price");
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dateTimeFormatter.format(now);

                        if (isAdmin.equals("no")) {
                            main.informationAppend(tradeNo + " 成功付款!");
                        } else {
                            main.informationAppend(tradeNo + " 成功付款! (" + isAdmin + ")");
                        }

                        PreparedStatement insertDonateLog = conn.prepareStatement("INSERT INTO `donate` SET `username` = ?, `amount` = ?, `paymentMethod` = '秘密', `date` = ?");

                        insertDonateLog.setString(1, playerAccount);
                        insertDonateLog.setInt(2, price);
                        insertDonateLog.setString(3, date);

                        insertDonateLog.execute();

                        PreparedStatement updatePoints = conn.prepareStatement("UPDATE `ecpay_donatepoints` SET `Points` = `Points` + ? WHERE `AccountName` = ?");

                        updatePoints.setInt(1, price);
                        updatePoints.setString(2, playerAccount);

                        if (updatePoints.executeUpdate() == 0) {
                            PreparedStatement insertPoints = conn.prepareStatement("INSERT INTO `ecpay_donatepoints` SET `AccountName` = ?, `Points` = ?, `LastAttempt` = ?");

                            insertPoints.setString(1, playerAccount);
                            insertPoints.setInt(2, price);
                            insertPoints.setString(3, date);

                            insertPoints.execute();
                        }
                    }
                }
            } catch (SQLException e) {
                main.informationAppend("資料庫連線失敗");
            }

        }
    }
}
