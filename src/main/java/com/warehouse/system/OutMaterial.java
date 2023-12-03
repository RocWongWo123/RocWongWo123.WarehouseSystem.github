package com.warehouse.system;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class OutMaterial extends Application {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        loadDatabaseProperties();
    }
    private Connection connection;
    private Stage primaryStage;
    private static int serialNumber = 500;

    private static int getNextSerialNumber() {
        return ++serialNumber;
    }

    public OutMaterial(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try { // 连接数据库
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("无法连接到数据库！");
        }
    }

    public void showAddMaterialScene(List<Material> selectedMaterialsList) {
        if (selectedMaterialsList.isEmpty()) {
            showErrorAlert("请先选择物料！");
            return;
        }

        Stage addMaterialStage = new Stage();
        addMaterialStage.initOwner(primaryStage);
        addMaterialStage.initModality(Modality.WINDOW_MODAL);

        // 创建添加物料界面的布局
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(10);

        // 创建列约束，使得第二列（物料名）和第三列（现有库存）宽度随内容而变化
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column2, column3);

        // 添加标题行
        gridPane.add(new Label("物料代码"), 0, 0);
        gridPane.add(new Label("物料名"), 1, 0);
        gridPane.add(new Label("现有库存"), 2, 0);
        gridPane.add(new Label("数量"), 3, 0);
        gridPane.add(new Label("备注"), 4, 0);

        int row = 1;

        List<String> successMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (Object material : selectedMaterialsList) {
            if (material instanceof Material) {
                Material materialObj = (Material) material;

                // 添加物料信息
                gridPane.add(new Label(String.valueOf(materialObj.getMid())), 0, row);
                gridPane.add(new Label(materialObj.getMaterialName()), 1, row);
                gridPane.add(new Label(String.valueOf(materialObj.getStockQuantity())), 2, row);

                // 添加数量 TextField
                TextField quantityField = new TextField();
                quantityField.setPrefWidth(30);
                quantityField.setText("0");
                gridPane.add(quantityField, 3, row);

                // 添加备注 TextField
                TextField remarkField = new TextField();
                remarkField.setPromptText("请输入备注");
                gridPane.add(remarkField, 4, row);

                // 添加删除按钮
                Button deleteButton = new Button("删除");

                gridPane.add(deleteButton, 5, row);
                deleteButton.setOnAction(event -> {
                    Node source = (Node) event.getSource();
                    Integer rowIndex = GridPane.getRowIndex(source);

                    if (rowIndex != null && rowIndex > 0) {
                        // 移除事件处理器
                        deleteButton.setOnAction(null);

                        // 删除该行的所有节点
                        gridPane.getChildren().removeIf(node -> {
                            Integer nodeRowIndex = GridPane.getRowIndex(node);
                            return nodeRowIndex != null && nodeRowIndex.equals(rowIndex);
                        });

                        // 删除该行的所有约束
                        ObservableList<RowConstraints> rowConstraints = gridPane.getRowConstraints();
                        if (rowIndex != null && rowIndex > 0 && rowIndex <= rowConstraints.size()) {
                            rowConstraints.remove(rowIndex - 1);
                        }
                    }
                });
                row++;
            }
        }
        Button okButton = new Button("确认");
        okButton.setOnAction(event -> {
            // 清除已删除的节点
            gridPane.getChildren().removeIf(node -> !node.isVisible());
            ObservableList<Node> nodes = gridPane.getChildren();

            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);

                if (node instanceof Label) {
                    Label idLabel = (Label) node;
                    String materialId = idLabel.getText();
                    TextField quantityField = findTextField(nodes, i + 3);
                    TextField remarkField = findTextField(nodes, i + 4);


                    if (quantityField != null && remarkField != null) {
                        String quantityText = quantityField.getText().trim();
                        if (!quantityText.isEmpty()) {
                            int quantity = Integer.parseInt(quantityText);
                            if (quantity > 0) {

                                String remark = remarkField.getText();

                                String result = callOutWarehouseProcedure(materialId, quantity, remark);


                                if (result.equals("操作成功")) {
                                    successMessages.add("物料" + materialId + ": 出仓成功");
                                    Iterator<Material> iterator = selectedMaterialsList.iterator();
                                    while (iterator.hasNext()) {
                                        Material material = iterator.next();
                                        if (String.valueOf(material.getMid()).equals(materialId)) {
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                } else {
                                    errorMessages.add("物料" + materialId + ": 出错" + result);
                                }
                            } else {
                                errorMessages.add("物料" + materialId + ": 数量必须大于 0");
                            }
                        } else {
                            errorMessages.add("物料" + materialId + ": 数量不能为空");
                        }
                    }
                }
            }


            StringBuilder summaryMessage = new StringBuilder();
            if (!successMessages.isEmpty()) {
                summaryMessage.append("以下物料成功出仓:\n");
                for (String successMessage : successMessages) {
                    summaryMessage.append(successMessage).append("\n");
                }
            }
            if (!errorMessages.isEmpty()) {
                summaryMessage.append("\n以下物料出仓失败:,请重新尝试\n");
                for (String errorMessage : errorMessages) {
                    summaryMessage.append(errorMessage).append("\n");
                }
            }

            showSummaryAlert(summaryMessage.toString());
            addMaterialStage.close();
        });

        gridPane.add(okButton, 1, row);

        // 创建场景并显示
        Scene scene = new Scene(gridPane, 500, Region.USE_COMPUTED_SIZE);
        addMaterialStage.setTitle("出仓");
        addMaterialStage.setScene(scene);
        addMaterialStage.show();
    }

    // Helper method to find TextField in the list of nodes
    private TextField findTextField(ObservableList<Node> nodes, int index) {
        if (index < nodes.size()) {
            Node node = nodes.get(index);
            if (node instanceof TextField) {
                return (TextField) node;
            }
        }
        return null;
    }

    // 新增显示总结信息的方法
    private void showSummaryAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("出仓总结");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    // 调用存储过程的函数
    private String callOutWarehouseProcedure(String materialId, int quantity, String remark) {
        try {
            start(primaryStage);
            if (connection != null) {
                int username = WarehouseManagementSystem.getUserID();
                // 准备调用存储过程的语句
                String callProcedure = "{CALL 210312outWarehouse( ?, ?, ?, ?, ?, ?)}";
                CallableStatement statement = connection.prepareCall(callProcedure);

                // 设置存储过程的参数
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));

                statement.setInt(2, username); // 用实际的操作员代码替换
                statement.setString(3, remark); // 用实际的备注替换
                statement.setString(4, materialId);
                statement.setInt(5, quantity);
                // 注册输出参数
                statement.registerOutParameter(6, Types.VARCHAR);
                // 执行存储过程
                statement.execute();

                // 获取存储过程的输出参数值
                return statement.getString(6);
            } else {
                showErrorAlert("数据库连接为空！");
                return "数据库连接为空";
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
            showErrorAlert("无法调用存储过程！详细信息：" + e.getMessage());
            return "无法调用存储过程";
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
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
}
