package DAO;//package DAO;
//
//
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//
//public class UpdateMaterial {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/warehouse?serverTimezone=GMT%2B8";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "0701";
//
//    public static void updateMaterial(Material material) {
//        try {
//            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//            String updateQuery = "UPDATE material SET material_name = ?, specification = ?, unit = ?, stock_quantity = ?, remark = ? WHERE material_id = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
//
//            // Set the update parameters
//            preparedStatement.setString(1, material.getMaterialName());
//            preparedStatement.setString(2, material.getSpecification());
//            preparedStatement.setString(3, material.getUnit());
//            preparedStatement.setInt(4, material.getStockQuantity());
//            preparedStatement.setString(5, material.getRemark());
//            preparedStatement.setString(6, material.getMaterialId());
//
//            preparedStatement.executeUpdate();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
