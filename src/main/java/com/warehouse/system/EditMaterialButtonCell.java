package com.warehouse.system;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.Properties;

public class EditMaterialButtonCell extends TableCell<Material, Void> {
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
    private final Button editButton = new Button("修改");

    public EditMaterialButtonCell() {
        setGraphic(editButton);


        // 使用 updateItem 来确定按钮的可见性
        updateItem(null, true);

        editButton.setOnAction(event -> {
            Material material = getTableView().getItems().get(getIndex());

            // 创建一个自定义编辑对话框
            Dialog<Material> dialog = new Dialog<>();
            dialog.setTitle("编辑物料");

            // 创建输入字段和标签
            TextField fieldA = new TextField(material.getMaterialId());
            TextField fieldB = new TextField(material.getMaterialName());
            TextField fieldC = new TextField(material.getSpecification());
            TextField fieldD = new TextField(material.getUnit());
            TextField fieldE = new TextField(String.valueOf(material.getStockQuantity()));
            TextField fieldF = new TextField(material.getRemark());

            Label labelA = new Label("物料ID:");
            Label labelB = new Label("物料名称:");
            Label labelC = new Label("规格:");
            Label labelD = new Label("单位:");
            Label labelE = new Label("库存数量:");
            Label labelF = new Label("备注:");

            // 创建一个 GridPane 用于布局
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            // 将标签和输入字段添加到 GridPane
            grid.add(labelA, 0, 0);
            grid.add(fieldA, 1, 0);
            grid.add(labelB, 0, 1);
            grid.add(fieldB, 1, 1);
            grid.add(labelC, 0, 2);
            grid.add(fieldC, 1, 2);
            grid.add(labelD, 0, 3);
            grid.add(fieldD, 1, 3);
            grid.add(labelE, 0, 4);
            grid.add(fieldE, 1, 4);
            grid.add(labelF, 0, 5);
            grid.add(fieldF, 1, 5);

            // 将 GridPane 设置为对话框的内容
            dialog.getDialogPane().setContent(grid);

            // 添加“确定”和“取消”按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 为对话框设置结果转换器
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        // 尝试解析库存数量
                        int stockQuantity = Integer.parseInt(fieldE.getText());
                        material.setStockQuantity(stockQuantity);
                        // 如果解析成功，更新物料值
                        material.setMaterialId(fieldA.getText());
                        material.setMaterialName(fieldB.getText());
                        material.setSpecification(fieldC.getText());
                        material.setUnit(fieldD.getText());
                        material.setRemark(fieldF.getText());
                        return material;
                    } catch (NumberFormatException e) {
                        // 如果解析失败，显示错误警告
                        showAlert(Alert.AlertType.ERROR, "输入错误", "库存数量必须为整数。");
                    }
                }
                return null;
            });

            Optional<Material> result = dialog.showAndWait();
            result.ifPresent(updatedMaterial -> {
                boolean updateSuccess = updateMaterialInDatabase(updatedMaterial);
                if (updateSuccess) {
                    showAlert(Alert.AlertType.INFORMATION, "修改成功", "物料修改成功！");
                    getTableView().getItems().set(getIndex(), updatedMaterial);
                } else {
                    showAlert(Alert.AlertType.ERROR, "修改失败", "物料修改失败，请重试。");
                }
            });
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || !isValidRow()) {
            // 如果行为空或不包含有效数据，隐藏按钮
            setGraphic(null);
        } else {
            // 如果行包含有效数据，显示按钮
            setGraphic(editButton);
        }
    }

    private boolean isValidRow() {
        // 检查行是否包含有效数据（例如，Material 对象不为空）
        Material material = getTableView().getItems().get(getIndex());
        return material != null;
    }

    public boolean updateMaterialInDatabase(Material material) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String updateQuery = "UPDATE material SET material_name = ?, specification = ?, unit = ?, stock_quantity = ?, remark = ? WHERE mid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            // 设置更新参数
            preparedStatement.setString(1, material.getMaterialName());
            preparedStatement.setString(2, material.getSpecification());
            preparedStatement.setString(3, material.getUnit());
            preparedStatement.setInt(4, material.getStockQuantity());
            preparedStatement.setString(5, material.getRemark());
            preparedStatement.setInt(6, material.getMid());

            preparedStatement.executeUpdate();
            connection.close();
            return true; // 成功返回 true
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 失败返回 false
        }
    }
}
