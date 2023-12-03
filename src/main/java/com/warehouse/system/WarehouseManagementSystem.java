package com.warehouse.system;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.util.Duration;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class WarehouseManagementSystem extends Application {
    private static int username;

    public static int getUserID() {
        return username;
    }

    public static void setUserID(String username) {
        WarehouseManagementSystem.username = Integer.parseInt(username);
    }


    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    private Connection connection;
    private Stage primaryStage;
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

    @Override
        public void start(Stage primaryStage) {
            try {
                this.primaryStage = primaryStage;
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/panda.png")));

                // 连接数据库
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                // 显示登录界面
                showLoginScene();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("应用程序启动失败: " + e.getMessage());
            }
        }

    public void showLoginScene() {
        // 创建登录界面的控件

            // 创建登录界面的控件
            Label titleLabel = new Label("仓库管理系统");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;"); // 设置标题样式

            Label usernameLabel = new Label("用户名:");
            TextField usernameField = new TextField();
            Label passwordLabel = new Label("密码:");
            PasswordField passwordField = new PasswordField();
            Button loginButton = new Button("登录");

            // 创建布局并设置控件位置
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10));
            gridPane.setHgap(5);
            gridPane.setVgap(10);
            gridPane.add(titleLabel, 0, 0, 4, 1); // 将标题跨足两列
            gridPane.add(usernameLabel, 0, 1);
            gridPane.add(usernameField, 1, 1);
            gridPane.add(passwordLabel, 0, 2);
            gridPane.add(passwordField, 1, 2);
            gridPane.add(loginButton, 1, 3);
            gridPane.setAlignment(Pos.CENTER);

        passwordField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    loginButton.fire();
                    break;
                default:
                    break;
            }
        });        // 设置登录按钮点击事件
        loginButton.setOnAction(event -> {
            ProgressIndicator progressIndicator = new ProgressIndicator();
            gridPane.add(progressIndicator, 1, 3);
            progressIndicator.setVisible(false);

            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                // 验证用户名和密码
                if (validateUser(username, password)) {
                    // 登录成功，显示加载动画
                    progressIndicator.setVisible(true);
                    WarehouseManagementSystem.setUserID(username);
                    // 设置加载动画的显示时间为1秒
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> {
                        progressIndicator.setVisible(false);
                        // 显示主界面
                        /*showMainScene();*/
                        MainScene.showMainScene();
                        // 关闭当前窗口
                        primaryStage.close();
                        // 显示欢迎消息
                        showSuccessAlert("欢迎进入仓库管理系统，designed by 王昆鹏，查晓琪");

                    });
                    pause.play();
                } else {
                    showErrorAlert("用户名或密码错误！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("登录失败！");
            }
        });
        // 设置背景图片
        BackgroundImage backgroundImage = new BackgroundImage(new Image (getClass().getResource("/img/login.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        gridPane.setBackground(new Background(backgroundImage));


        // 创建场景并显示
        Scene scene = new Scene(gridPane, 600, 350);
        primaryStage.setTitle("登录");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user_login WHERE person_id = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count == 1;
        }
    }


    private void showErrorAlert(String message) {
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
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
