package com.warehouse.system;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Callback;


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class MainScene extends Application {
    public String version= "v1.0";
    private static List<Material> selectedMaterialsList = new ArrayList<>();
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

    public static void main(String[] args) {
        launch(args);
    }
    public static void showMainScene() {

        Stage primaryStage = new Stage();
        primaryStage.getIcons().add(new Image(MainScene.class.getResourceAsStream("/img/panda.png")));
        // 创建导航栏
        HBox navigationBar = new HBox();
        navigationBar.getStyleClass().add("navigation-bar");
        // 创建导航按钮
        Button materialsButton = new Button("物料管理");
        Button personnelButton = new Button("人员管理");
        Button addmaterialButton = new Button("添加物料");
        Button printButton = new Button("报表打印");
        Button exitButton = new Button("退出");
        Person person1 =new Person();
        String name1 = person1.getName();
        Label nameLabel = new Label("欢迎你, " + "王昆鹏 ");
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setAlignment(Pos.TOP_RIGHT);

        TableColumn<Material, Boolean> selectColumn = new TableColumn<>("选择");
        selectColumn.setPrefWidth(60);
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(column -> {
            CheckBoxTableCell<Material, Boolean> cell = new CheckBoxTableCell<>();
            cell.getStyleClass().add("check-box-table-cell");
            return cell;
        });

        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);
        selectColumn.setOnEditCommit(event -> {
            Material material = new Material();
            material.setSelected(event.getNewValue());
            // 如果复选框被选中，将物料添加到 selectedMaterialsList
            if (event.getNewValue()) {
                selectedMaterialsList.add(material);
            }
        });

        // 设置导航按钮的样式
        materialsButton.getStyleClass().add("navigation-button");
        personnelButton.getStyleClass().add("navigation-button");
        addmaterialButton.getStyleClass().add("navigation-button");
        printButton.getStyleClass().add("navigation-button");
        nameLabel.getStyleClass().add("navigation-button");
        exitButton.getStyleClass().add("navigation-button");

        // 添加导航按钮到导航栏
        navigationBar.getChildren().addAll(materialsButton, personnelButton, addmaterialButton,printButton, exitButton,nameLabel);

        BorderPane mainLayout = new BorderPane();

        BackgroundImage backgroundImage = new BackgroundImage(new Image(MainScene.class.getResource("/img/login2.jpg").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(backgroundImage));

        TableView<Material> tableView = new TableView<>();
        // 在 showMainScene() 方法中的相应位置添加以下代码

        tableView.getStyleClass().add("table-view");


        TableColumn<Material, Integer> columna = new TableColumn<>("物料序号");
        TableColumn<Material, String> column1 = new TableColumn<>("物料编号");
        TableColumn<Material, String> column2 = new TableColumn<>("物料名");
        TableColumn<Material, String> column3 = new TableColumn<>("规格");
        TableColumn<Material, String> column4 = new TableColumn<>("单位");
        TableColumn<Material, Integer> column5 = new TableColumn<>("数量");
        TableColumn<Material, String> column6 = new TableColumn<>("备注");
        // 创建两新列，用于放置修改和删除按钮
        TableColumn<Material, Void> editColumn = new TableColumn<>("修改");
        editColumn.setCellFactory(param -> new EditMaterialButtonCell());
        TableColumn<Material, Void> deleteColumn = new TableColumn<>("删除");

// 启用TableView的编辑模式
        tableView.setEditable(true);

        editColumn.setPrefWidth(80);
        deleteColumn.setPrefWidth(80);

        Button checkoutButton = new Button("出仓");
        Button checkinButton = new Button("入库");
        checkoutButton.setId("/css/button.css");


        columna.setPrefWidth(80);
        column1.setPrefWidth(120);
        column2.setPrefWidth(80);
        column3.setPrefWidth(80);
        column4.setPrefWidth(60);
        column5.setPrefWidth(60);
        column6.setPrefWidth(170);
        column1.getStyleClass().add("table-column");
        columna.setCellValueFactory(new PropertyValueFactory<>("mid"));
        column1.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        column2.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        column3.setCellValueFactory(new PropertyValueFactory<>("specification"));
        column4.setCellValueFactory(new PropertyValueFactory<>("unit"));
        column5.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        column6.setCellValueFactory(new PropertyValueFactory<>("remark"));

        //人员表
        TableView<Person> tableView1 = new TableView<>();
        TableColumn<Person, String> column7 = new TableColumn<>("人员编号");
        TableColumn<Person, String> column8 = new TableColumn<>("姓名");
        TableColumn<Person, String> column9 = new TableColumn<>("性别");
        TableColumn<Person, Date> column10 = new TableColumn<>("出生日期");
        TableColumn<Person, String> column11 = new TableColumn<>("身份证号");
        TableColumn<Person, String> column12 = new TableColumn<>("籍贯");
        TableColumn<Person, String> column13 = new TableColumn<>("家庭住址");
        TableColumn<Person, String> column14 = new TableColumn<>("联系方式");
        TableColumn<Person, String> column15 = new TableColumn<>("备注");

        column7.setPrefWidth(80);
        column8.setPrefWidth(50);
        column9.setPrefWidth(40);
        column10.setPrefWidth(70);
        column11.setPrefWidth(150);
        column12.setPrefWidth(80);
        column13.setPrefWidth(160);
        column14.setPrefWidth(90);
        column15.setPrefWidth(100);

        column7.setCellValueFactory(new PropertyValueFactory<>("person_id"));
        column8.setCellValueFactory(new PropertyValueFactory<>("name"));
        column9.setCellValueFactory(new PropertyValueFactory<>("sex"));
        column10.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        column11.setCellValueFactory(new PropertyValueFactory<>("id_card"));
        column12.setCellValueFactory(new PropertyValueFactory<>("native_place"));
        column13.setCellValueFactory(new PropertyValueFactory<>("address"));
        column14.setCellValueFactory(new PropertyValueFactory<>("phone"));
        column15.setCellValueFactory(new PropertyValueFactory<>("other_info"));



// 将列添加到表格tableView1
        tableView1.getColumns().addAll(column7, column8, column9, column10, column11, column12, column13, column14, column15);

        // 将列添加到表格
        tableView.getColumns().addAll(columna, column1, column2, column3, column4, column5, column6, editColumn, deleteColumn, selectColumn);



        // 创建ObservableList<Material>对象来存储表格数据
        ObservableList<Material> materialList = FXCollections.observableArrayList();

        ObservableList<Person> personList = FXCollections.observableArrayList();

        // 从数据库中获取数据并添加到表格数据列表中
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM material");

            while (resultSet.next()) {
                int mid= resultSet.getInt("mid");
                String materialId = resultSet.getString("material_id");
                String materialName = resultSet.getString("material_name");
                String specification = resultSet.getString("specification");
                String unit = resultSet.getString("unit");
                int stockQuantity = resultSet.getInt("stock_quantity");
                String remark = resultSet.getString("remark");

                Material material = new Material(mid,materialId, materialName, specification, unit, stockQuantity, remark);
                materialList.add(material);
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HBox topContainer = new HBox(); // 修改为HBox
        topContainer.setAlignment(Pos.TOP_LEFT);
        topContainer.setSpacing(10);

        HBox nameBox = new HBox(nameLabel);  // 将nameLabel包装在HBox中
        nameBox.setAlignment(Pos.TOP_RIGHT);

// 将navigationBar和nameBox都添加到HBox，并设置HGrow属性
        HBox.setHgrow(navigationBar, Priority.ALWAYS);  // 让navigationBar占用HBox中剩余的空间
        topContainer.getChildren().addAll(navigationBar, nameBox);

        mainLayout.setTop(topContainer);
        mainLayout.setCenter(tableView);

// 创建搜索框和搜索按钮
        Label midLabel = new Label("编号:");
        TextField midField = new TextField();
        Label searchLabel = new Label("物料名:");
        TextField searchField = new TextField();
        Button searchButton = new Button("查询");
        // 每页的行数
        int rowsPerPage = 15;


// 创建一个Pagination对象
        Pagination pagination = new Pagination((materialList.size() + rowsPerPage - 1) / rowsPerPage);
        pagination.setPageFactory(null);

        searchButton.setOnAction(event -> {

            String keyword = searchField.getText();
            String idkeyword = midField.getText();

            try {
                // 在添加新的搜索结果之前清除现有数据
                materialList.clear();

                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                String query = "SELECT * FROM material WHERE material_name LIKE ? AND material_id LIKE ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + keyword + "%");
                preparedStatement.setString(2, "%" + idkeyword + "%");

                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("Executing query: " + preparedStatement.toString());

                boolean dataFound = false;  // Flag to check if any data is found

                while (resultSet.next()) {
                    int mid = resultSet.getInt("mid");
                    String materialId = resultSet.getString("material_id");
                    String materialName = resultSet.getString("material_name");
                    String specification = resultSet.getString("specification");
                    String unit = resultSet.getString("unit");
                    int stockQuantity = resultSet.getInt("stock_quantity");
                    String remark = resultSet.getString("remark");
                    Material material = new Material(mid, materialId, materialName, specification, unit, stockQuantity, remark);
                    materialList.add(material);
                    dataFound = true;  // Set flag to true if data is found

                    // 输出查询到的信息到控制台
                    System.out.println("Material: " + material);
                }

                // 如果没有找到数据，则显示提示弹窗
                if (!dataFound) {
                    showAlert("未找到匹配的数据", "请尝试使用不同的关键字进行搜索。");
                }

                Platform.runLater(() -> {
                    // 清空表格
                    tableView.getItems().clear();
                    tableView.refresh();

                    // 查询结束后，清除 Pagination 的 pageFactory，然后重新设置，触发 PageFactory 被调用
                    pagination.setPageFactory(null);
                    pagination.setPageFactory(pageIndex -> {
                        // 创建一个新的 TableView
                        TableView<Material> pageTableView = new TableView<>();
                        // 启用 TableView 编辑模式
                        pageTableView.setEditable(true);
                        // 设置 TableView 的高度
                        pageTableView.setPrefHeight(680); // 设置为你期望的高度
                        Callback<TableColumn<Material, Void>, TableCell<Material, Void>> deleteButtonCellFactory =
                                param -> new DeleteMaterialButtonCell(tableView, materialList);
                        deleteColumn.setCellFactory(deleteButtonCellFactory);

                        pagination.setPageCount((materialList.size() + rowsPerPage - 1) / rowsPerPage);

                        deleteColumn.setCellFactory(deleteButtonCellFactory);

                        pageTableView.refresh();
                        Callback<TableColumn<Material, Void>, TableCell<Material, Void>> editButtonCellFactory = param -> new EditMaterialButtonCell();
                        editColumn.setCellFactory(editButtonCellFactory);

                        // Set the columns of the TableView
                        pageTableView.getColumns().addAll(columna,column1, column2, column3, column4, column5, column6, editColumn, deleteColumn, selectColumn);

                        // Calculate the start and end index of the sublist
                        int fromIndex = pageIndex * rowsPerPage;
                        int toIndex = Math.min(fromIndex + rowsPerPage, materialList.size());

                        // Check if fromIndex is less than the size of materialList
                        if (fromIndex < materialList.size()) {
                            // Fill the data for the corresponding page
                            pageTableView.setItems(FXCollections.observableArrayList(materialList.subList(fromIndex, toIndex)));
                        }

                        return pageTableView;
                    });

                    // 使用新的 materialList 对象填充表格
                    tableView.getItems().setAll(materialList);
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        HBox searchBox = new HBox(10, midLabel,midField,searchLabel,searchField, searchButton);
        searchBox.getStyleClass().add("search-box");
        searchBox.setAlignment(Pos.CENTER);

// 创建一个新的水平布局，包括搜索框、查询按钮和出仓按钮
        HBox buttonBox = new HBox(20, searchBox,checkinButton, checkoutButton);
        checkoutButton.getStyleClass().add("checkoutButton");
        checkinButton.getStyleClass().add("checkoutButton");
        searchButton.getStyleClass().add("checkoutButton");

        buttonBox.setAlignment(Pos.CENTER);

// 创建一个VBox来存放所有元素
        VBox vBox = new VBox();

// 添加按钮盒子到VBox
        vBox.getChildren().addAll(buttonBox);

// 设置垂直布局中表格的高度
        VBox.setVgrow(tableView, Priority.ALWAYS);


// 设置页面工厂
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                // 创建一个新的 TableView
                TableView<Material> pageTableView = new TableView<>();
                // 启用 TableView 编辑模式
                pageTableView.setEditable(true);
                // 设置 TableView 的高度
                pageTableView.setPrefHeight(680); // 设置为你期望的高度
                Callback<TableColumn<Material, Void>, TableCell<Material, Void>> deleteButtonCellFactory =
                        param -> new DeleteMaterialButtonCell(tableView, materialList);
                deleteColumn.setCellFactory(deleteButtonCellFactory);

                pagination.setPageCount((materialList.size() + rowsPerPage - 1) / rowsPerPage);

                deleteColumn.setCellFactory(deleteButtonCellFactory);

                pageTableView.refresh();
                Callback<TableColumn<Material, Void>, TableCell<Material, Void>> editButtonCellFactory = param -> new EditMaterialButtonCell();
                editColumn.setCellFactory(editButtonCellFactory);

                // Set the columns of the TableView
                pageTableView.getColumns().addAll(columna,column1, column2, column3, column4, column5, column6, editColumn, deleteColumn, selectColumn);

                // Calculate the start and end index of the sublist
                int fromIndex = pageIndex * rowsPerPage;
                int toIndex = Math.min(fromIndex + rowsPerPage, materialList.size());

                // Check if fromIndex is less than the size of materialList
                if (fromIndex < materialList.size()) {
                    // Fill the data for the corresponding page
                    pageTableView.setItems(FXCollections.observableArrayList(materialList.subList(fromIndex, toIndex)));
                }

                return pageTableView;
            }
        });


// 将Pagination对象添加到VBox
        vBox.getChildren().add(pagination);

// 将VBox设置为中心内容
        mainLayout.setCenter(vBox);



        checkoutButton.setOnAction(event -> {
            // 遍历所有的物料
            for (Material material : materialList) {
                if (material.isSelected()) {
                    // 将勾选的物料的mid加入到selectedMaterials中
                    selectedMaterialsList.add(material);
                }
            }
            // 在处理完选中物料并执行出仓操作之后，设置 pagination 的页数
            pagination.setPageCount((materialList.size() + rowsPerPage - 1) / rowsPerPage);
            // 执行出仓操作
            OutMaterial outMaterial = new OutMaterial(primaryStage);
            outMaterial.showAddMaterialScene(selectedMaterialsList);
            for (Material material : materialList) {
                material.resetSelection();
            }
            selectedMaterialsList.clear();
        });


        Scene scene = new Scene(mainLayout, 1000, 750);
        scene.getStylesheets().add("/css/navigate.css");
        scene.getStylesheets().add("/css/tableview.css");
        scene.getStylesheets().add("/css/button.css");

        scene.getStylesheets().add("/css/tablecell.css");
        scene.getStylesheets().add("/css/table-center-align.css");//居中显示
        primaryStage.setTitle("仓库管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();

        AddMaterial addMaterial = new AddMaterial(primaryStage);
        exitButton.setOnAction((ActionEvent event) -> {
            // 关闭主窗口
            primaryStage.close();
            // 显示登录窗口

        });

        personnelButton.setOnAction(event -> {

            // 清空TableView中的现有数据
            tableView1.getItems().clear();

            // 设置中心内容为tableView1
            mainLayout.setCenter(tableView1);

            // 检索新数据并添加到tableView1
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM person");

                while (resultSet.next()) {
                    int person_id = resultSet.getInt("person_id");
                    String name = resultSet.getString("name");
                    String sex = resultSet.getString("sex");
                    Date birthdate = resultSet.getDate("birthdate");
                    String id_card = resultSet.getString("id_card");
                    String native_place = resultSet.getString("native_place");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String other_info = resultSet.getString("other_info");

                    Person person = new Person(person_id, name, sex, birthdate, id_card, native_place, address, phone, other_info);
                    personList.add(person);
                }

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            tableView1.setItems(personList);
        });

        materialsButton.setOnAction(event -> {
            mainLayout.setCenter(vBox);
            materialList.clear();

            // Retrieve new data from the database and add it to the materialList
            try {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM material");

                while (resultSet.next()) {
                    int mid= resultSet.getInt("mid");
                    String materialId = resultSet.getString("material_id");
                    String materialName = resultSet.getString("material_name");
                    String specification = resultSet.getString("specification");
                    String unit = resultSet.getString("unit");
                    int stockQuantity = resultSet.getInt("stock_quantity");
                    String remark = resultSet.getString("remark");

                    Material material = new Material(mid,materialId, materialName, specification, unit, stockQuantity, remark);
                    materialList.add(material);
                }

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Reset the page count of the Pagination
            pagination.setPageCount((materialList.size() + rowsPerPage - 1) / rowsPerPage);
        });

        addmaterialButton.setOnAction(event -> {

            addMaterial.showAddMaterialScene();
        });


    }



    // 弹窗方法
    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @Override
    public void start(Stage primaryStage) {
        showMainScene();
    }
}