package example.Servies;

import example.model.Suppliers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierManagement {
    private List<Suppliers> suppliers;
    private Connection connection;

    public SupplierManagement() {
        suppliers = new ArrayList<>();
        initializeDatabaseConnection();
        fetchSuppliersFromDatabase();
    }

    private void initializeDatabaseConnection() {
        try {
            final String URL = "jdbc:mysql://localhost:3306/myshare?user=root&password=";
            final String USERNAME = "root";
            final String PASSWORD = "";

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void fetchSuppliersFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM suppliers";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String orderDetails = resultSet.getString("order_details"); // Use correct column name 'order_details'
                String phone = resultSet.getString("phone"); // Use correct column name 'phone'

                Suppliers supplier = new Suppliers(id, name, orderDetails, phone);
                suppliers.add(supplier);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Suppliers> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(Suppliers supplier) {
        try {
            String query = "INSERT INTO suppliers (name, order_details, phone) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getOrder_details());
            preparedStatement.setString(3, supplier.getPhone());

            preparedStatement.executeUpdate();

            // Fetch the generated ID and set it in the Supplier object
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                supplier.setId(id);
            }

            generatedKeys.close();
            preparedStatement.close();

            suppliers.add(supplier);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSupplier(Suppliers updatedSupplier) {
        try {
            String query = "UPDATE suppliers SET name = ?, order_details = ?, phone = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedSupplier.getName());
            preparedStatement.setString(2, updatedSupplier.getOrder_details());
            preparedStatement.setString(3, updatedSupplier.getPhone());
            preparedStatement.setInt(4, updatedSupplier.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Update the Supplier object in the list
            for (Suppliers supplier : suppliers) {
                if (supplier.getId() == updatedSupplier.getId()) {
                    supplier.setName(updatedSupplier.getName());
                    supplier.setEmail(updatedSupplier.getOrder_details());
                    supplier.setPhone(updatedSupplier.getPhone());
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSupplier(int id) {
        try {
            String query = "DELETE FROM suppliers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Remove the Supplier object from the list
            suppliers.removeIf(supplier -> supplier.getId() == id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabaseConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
