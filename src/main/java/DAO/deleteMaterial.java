package DAO;


import com.warehouse.system.WarehouseManagementSystem;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class deleteMaterial {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;


    static {
        loadDatabaseProperties();
    }

    private static void loadDatabaseProperties() {
        try (InputStream input = WarehouseManagementSystem.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("对不起，找不到资源");
                return;
            }
            prop.load(input);

            // 读取属性值
            DB_URL = prop.getProperty("db.url");
            DB_USER = prop.getProperty("db.user");
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public deleteMaterial(int mid) {
        deleteMaterial.deleteMaterial(mid); // 调用删除方法
    }

    public static void deleteMaterial(int mid) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM material WHERE mid = ?");
            preparedStatement.setInt(1, mid);
            System.out.println(mid);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除物料失败", e);
        }
    }
}