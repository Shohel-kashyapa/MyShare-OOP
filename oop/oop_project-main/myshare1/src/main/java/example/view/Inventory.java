package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import example.config.Db;
import java.sql.*;

public class Inventory extends JFrame {
    private JTextField txt_in;
    private JTextField txt_id;
    private JTextField txt_price;
    private JTextField txt_quantity; // New field for quantity
    private JButton btn_add;
    private JTextField txt_id2;
    private JButton btn_remove;

    private Db db; // Instance of the Db class for database connection

    public Inventory() {
        db = Db.getSingleInstance(); // Get a single instance of the Db class for database connection

        // Create the inventory panel and add components to it
        JPanel inventory = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Inventory");
        inventory.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel itemNameLabel = new JLabel("Item Name:");
        inventory.add(itemNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_in = new JTextField(20);
        inventory.add(txt_in, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel itemIdLabel = new JLabel("Item ID:");
        inventory.add(itemIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_id = new JTextField(10);
        inventory.add(txt_id, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel itemPriceLabel = new JLabel("Item Price:");
        inventory.add(itemPriceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_price = new JTextField(10);
        inventory.add(txt_price, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel itemQuantityLabel = new JLabel("Quantity:"); // New label for quantity
        inventory.add(itemQuantityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_quantity = new JTextField(10); // New text field for quantity
        inventory.add(txt_quantity, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        btn_add = new JButton("ADD");
        inventory.add(btn_add, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel removeItemsLabel = new JLabel("Remove Items:");
        inventory.add(removeItemsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txt_id2 = new JTextField(10);
        inventory.add(txt_id2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        btn_remove = new JButton("Remove");
        inventory.add(btn_remove, gbc);

        // Set up JFrame properties and add the inventory panel to the JFrame's content pane
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setTitle("Inventory Management");
        add(inventory);
        setVisible(true);

        // Add action listeners to buttons
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        btn_remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItem();
            }
        });
    }

    private void addItem() {
        // Retrieve item details from text fields (txt_in, txt_id, txt_price, txt_quantity)
        String itemName = txt_in.getText();
        String itemIdInput = txt_id.getText();
        String itemPriceInput = txt_price.getText();
        String itemQuantityInput = txt_quantity.getText();

        // Validate that item ID, item price, and quantity contain only numerical characters
        if (!itemIdInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Item ID. Please enter a valid integer.");
            return; // Stop further processing
        }

        if (!itemPriceInput.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(this, "Invalid Item Price. Please enter a valid number.");
            return; // Stop further processing
        }

        if (!itemQuantityInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity. Please enter a valid integer.");
            return; // Stop further processing
        }

        int itemId = Integer.parseInt(itemIdInput);
        double itemPrice = Double.parseDouble(itemPriceInput);
        int itemQuantity = Integer.parseInt(itemQuantityInput);

        // Insert item into the database using the Db class
        String sqlQuery = "INSERT INTO items (id, name, price, quantity) VALUES (" + itemId + ", '" + itemName + "', " + itemPrice + ", " + itemQuantity + ")";
        boolean isInserted = db.ExecuteQuery(sqlQuery);

        if (isInserted) {
            // Item added successfully
            JOptionPane.showMessageDialog(this, "Item added to the inventory!");
        } else {
            // Failed to add item
            JOptionPane.showMessageDialog(this, "Failed to add item to the inventory. Please try again.");
        }

        // Clear text fields after adding item
        txt_in.setText("");
        txt_id.setText("");
        txt_price.setText("");
        txt_quantity.setText("");
    }

    private void removeItem() {
        // Retrieve item ID from text field (txt_id2)
        String itemIdToRemoveInput = txt_id2.getText();

        // Validate that item ID contains only numerical characters
        if (!itemIdToRemoveInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Item ID. Please enter a valid integer.");
            return; // Stop further processing
        }

        int itemIdToRemove = Integer.parseInt(itemIdToRemoveInput);

        // Remove item from the database using the Db class
        String sqlQuery = "DELETE FROM items WHERE id = " + itemIdToRemove;
        boolean isRemoved = db.ExecuteQuery(sqlQuery);

        if (isRemoved) {
            // Item removed successfully
            JOptionPane.showMessageDialog(this, "Item removed from the inventory!");
        } else {
            // Failed to remove item
            JOptionPane.showMessageDialog(this, "Failed to remove item from the inventory. Please try again.");
        }

        // Clear text field after removing item
        txt_id2.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Inventory();
            }
        });
    }
}
