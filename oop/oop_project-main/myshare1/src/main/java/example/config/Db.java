package example.config;

import example.model.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Db {
    private final String UName = "root";
    private final String Password = "";
    private final String URL = "jdbc:mysql://localhost:3306/myshare";

    private static Db instance;
    private Connection con;

    private Db() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, UName, Password);
            System.out.println("Database Connection Success");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver Class Error " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("Database Connection Error " + ex.getMessage());
        }
    }

    public static Db getSingleInstance() {
        try {
            if (instance == null) {
                instance = new Db();
            } else if (instance.con.isClosed()) {
                instance = new Db();
            } else {
                return instance;
            }
            return instance;
        } catch (SQLException ex) {
            System.out.println("Database Connection Error " + ex.getMessage());
            return null;
        }
    }

    public boolean ExecuteQuery(String sqlQ) {
        try {
            Statement st = con.createStatement();
            int result = st.executeUpdate(sqlQ);
            return result > 0;
        } catch (SQLException ex) {
            System.out.println("SQL Error " + ex.getMessage());
            return false;
        }
    }

    public Connection getConnection() {
        return con;
    }

    public List<Inventory> getInventoryList() {
        List<Inventory> inventoryList = new ArrayList<>();
        try {
            String query = "SELECT id, name, price, quantity FROM items";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                String itemName = resultSet.getString("name");
                double itemPrice = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                Inventory inventory = new Inventory(itemId, itemName, itemPrice, quantity);
                inventoryList.add(inventory);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error fetching inventory data: " + ex.getMessage());
        }

        return inventoryList;
    }

    // Method to update the inventory data in the database
    public boolean updateInventory(int itemId, String itemName, double itemPrice, int quantity) {
        try {
            // Prepare the update query using a PreparedStatement to prevent SQL injection
            String updateQuery = "UPDATE items SET name = ?, price = ?, quantity = ? WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);

            // Set the values for the prepared statement
            preparedStatement.setString(1, itemName);
            preparedStatement.setDouble(2, itemPrice);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, itemId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // Close the PreparedStatement
            preparedStatement.close();

            // Return true if at least one row is affected, indicating a successful update
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating inventory data: " + ex.getMessage());
            return false;
        }
    }


}