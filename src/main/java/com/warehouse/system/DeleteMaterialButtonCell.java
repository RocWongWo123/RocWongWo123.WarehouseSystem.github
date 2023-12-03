package com.warehouse.system;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.util.Optional;
import DAO.*;

public class DeleteMaterialButtonCell extends TableCell<Material, Void> {
    private final Button deleteButton = new Button("删除");
    private final TableView<Material> tableView;
    private ObservableList<Material> materialList;
    public DeleteMaterialButtonCell(TableView<Material> tableView, ObservableList<Material> materialList){
        this.tableView = tableView;
        this.materialList = materialList;
        deleteButton.setOnAction(event -> {

            Material material = getTableView().getItems().get(getIndex());
            int stockQuantity = material.getStockQuantity();


            // 创建一个确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getDialogPane().setMinWidth(400); // 设置最小宽度
            alert.getDialogPane().setMinHeight(300); // 设置最小高度

            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/css/alert.css").toExternalForm()
            );
            alert.setTitle("确认删除");
            alert.setHeaderText("确认删除物料");
            alert.setContentText("你确定要删除物料编号为 " + material.getMaterialId() + " 的记录吗？\n"
                    + "该物料剩余库存为 " + stockQuantity);

            // 获取用户的选择
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 用户点击了确认按钮，执行删除操作
                boolean deleteSuccess = deleteMaterialFromDatabase(material);

                if (deleteSuccess) {
                    getTableView().getItems().remove(material); // 从UI中移除
                    materialList.remove(material); // 从 materialList 中移除
                    // 从底层数据列表中删除项目
                    tableView.getItems().remove(material);
                    // 刷新TableView
                    tableView.refresh();

                    System.out.println("Material removed: " + material.getMaterialId());

                    // 弹出成功删除的提示对话框
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("删除成功");
                    successAlert.setHeaderText("删除成功");
                    successAlert.setContentText("物料编号为 " + material.getMaterialId() + " 的记录已成功删除。");
                    successAlert.showAndWait();
                } else {
                    // 删除失败，弹出错误提示对话框
                    showAlert(Alert.AlertType.ERROR, "删除失败", "物料删除失败，请重试。");
                }
            }
        });
    }

    private boolean deleteMaterialFromDatabase(Material material) {
        try {
            // 执行删除物料操作
            new deleteMaterial(material.getMid());
            return true; // 成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 失败
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(deleteButton);
        }
    }
}