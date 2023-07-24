package example.Servies;

import example.model.CustomerOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderManagement {
    private List<CustomerOrder> customerOrders;
    private Connection connection;

    public CustomerOrderManagement() {
        customerOrders = new ArrayList<>();
        initializeDatabaseConnection();
        fetchCustomerOrdersFromDatabase();
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

    private void fetchCustomerOrdersFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM customer_orders";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String customerName = resultSet.getString("customer_name");
                String orderDetails = resultSet.getString("order_details");
                int quantity = resultSet.getInt("quantity");

                CustomerOrder order = new CustomerOrder(orderId, customerName, orderDetails, quantity);
                customerOrders.add(order);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void addCustomerOrder(CustomerOrder order) {
        try {
            String query = "INSERT INTO customer_orders (customer_name, order_details, quantity) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, order.getCustomerName());
            preparedStatement.setString(2, order.getOrderDetails());
            preparedStatement.setDouble(3, order.getquantity());

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Fetch the generated order_id and set it in the CustomerOrder object
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            if (resultSet.next()) {
                int orderId = resultSet.getInt(1);
                order.setOrderId(orderId);
            }
            resultSet.close();
            statement.close();

            customerOrders.add(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerOrder(CustomerOrder updatedOrder) {
        try {
            String query = "UPDATE customer_orders SET customer_name = ?, order_details = ?, quantity = ? WHERE order_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, updatedOrder.getCustomerName());
            preparedStatement.setString(2, updatedOrder.getOrderDetails());
            preparedStatement.setInt(3, updatedOrder.getquantity());
            preparedStatement.setInt(4, updatedOrder.getOrderId());

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Update the CustomerOrder object in the list
            for (CustomerOrder order : customerOrders) {
                if (order.getOrderId() == updatedOrder.getOrderId()) {
                    order.setCustomerName(updatedOrder.getCustomerName());
                    order.setOrderDetails(updatedOrder.getOrderDetails());
                    order.setquantity(updatedOrder.getquantity());
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCustomerOrder(int orderId) {
        try {
            String query = "DELETE FROM customer_orders WHERE order_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, orderId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            // Remove the CustomerOrder object from the list
            customerOrders.removeIf(order -> order.getOrderId() == orderId);
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
