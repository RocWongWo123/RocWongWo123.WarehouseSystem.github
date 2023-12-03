package com.warehouse.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class AddMaterial extends Application {

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
    private Connection connection;
    private Stage primaryStage;


    public AddMaterial(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initModality(Modality.APPLICATION_MODAL); // 设置为应用程序模态


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         showAddMaterialScene();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("无法连接到数据库！");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void addMaterial(String id, String name, String quantity, String specification, String unit, String mark) throws SQLException {
        // 连接到数据库
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/warehouse?serverTimezone=GMT%2B8", "root", "0701");

        // 创建插入语句
        String insertQuery = "INSERT INTO material (material_id,material_name,specification,unit, stock_quantity,remark) VALUES (?,?, ?,?,?,?)";

        // 创建预编译的语句
        PreparedStatement statement = connection.prepareStatement(insertQuery);

        statement.setString(1, id);
        statement.setString(2, name);
        statement.setString(3, specification);
        statement.setString(4, unit);
        statement.setString(5, quantity);
        statement.setString(6, mark);

        // 执行插入操作
        statement.executeUpdate();

        // 关闭连接和语句
        statement.close();
        connection.close();
    }
    public void showAddMaterialScene() {
        Stage addMaterialStage = new Stage(StageStyle.UTILITY);
        addMaterialStage.initOwner(primaryStage);
        addMaterialStage.initModality(Modality.WINDOW_MODAL);

        // 创建添加物料界面的控件
        Label idLabel = new Label("物料代码:");
        TextField idField = new TextField();
        Label nameLabel = new Label("物料名称:");
        TextField nameField = new TextField();

        Label specificationLabel = new Label("规格型号:");
        TextField specificationField = new TextField();

        ComboBox<String> unitComboBox = new ComboBox<>();
        unitComboBox.getItems().addAll("台","件", "套", "公斤", "吨", "升", "米", "毫米", "个");
        unitComboBox.setEditable(true);

        Label quantityLabel = new Label("物料数量:");
        TextField quantityField = new TextField();
        quantityField.setText("0");


        Label markLabel = new Label("备注(选填):");
        TextField markField = new TextField();



        Button addButton = new Button("添加");

        // 设置添加按钮的事件处理程序
        addButton.setOnAction(event -> {
            String id = idField.getText();
            String name = nameField.getText();
            String quantity = quantityField.getText();
            String specification = specificationField.getText();
            String unit = unitComboBox.getValue();
            String mark = markField.getText();



            try {
                addMaterial(id, name, quantity,specification,unit, mark);
                showSuccessAlert("物料添加成功！");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("物料添加失败，数据不完整或数据库中已存在该物料！");
            }
        });

        // 创建布局并设置控件位置
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        gridPane.add(idLabel, 0, 0);
        gridPane.add(idField, 1, 0);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);

        gridPane.add(quantityLabel, 0, 2);
        gridPane.add(quantityField, 1, 2);

        gridPane.add(specificationLabel, 0, 3);
        gridPane.add(specificationField, 1, 3);

        gridPane.add(new Label("计量单位:"), 0, 4);
        gridPane.add(unitComboBox, 1, 4);

        gridPane.add(markLabel, 0, 5);
        gridPane.add(markField, 1, 5);




        gridPane.add(addButton, 1, 6);

        // 创建场景并显示
        Scene scene = new Scene(gridPane, 500, 250);
        addMaterialStage.setTitle("添加物料");
        addMaterialStage.setScene(scene);
        addMaterialStage.show();

    }

    private  void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public  void main(String[] args) {

        launch(args);
    }
}



