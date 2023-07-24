package example.view;

import example.config.Db;
import example.model.Inventory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateInventory extends JFrame {
    private JTextField itemNameTextField;
    private JTextField itemPriceTextField;
    private JTextField quantityTextField;
    private JButton updateButton;

    public UpdateInventory(int itemId) {
        // Fetch item data from the database based on the itemId
        Inventory inventory = fetchInventoryData(itemId);

        // Set up the UpdateInventory form with the retrieved data
        JPanel updateInventoryPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        updateInventoryPanel.add(new JLabel("Item Name:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        itemNameTextField = new JTextField(inventory.getItemName());
        updateInventoryPanel.add(itemNameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        updateInventoryPanel.add(new JLabel("Item Price:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        itemPriceTextField = new JTextField(String.valueOf(inventory.getItemPrice()));
        updateInventoryPanel.add(itemPriceTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        updateInventoryPanel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        quantityTextField = new JTextField(String.valueOf(inventory.getQuantity()));
        updateInventoryPanel.add(quantityTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        updateButton = new JButton("Update");
        updateInventoryPanel.add(updateButton, gbc);

        updateButton.addActionListener(e -> {
            // Get updated data from the text fields
            String updatedItemName = itemNameTextField.getText();
            double updatedItemPrice = Double.parseDouble(itemPriceTextField.getText());
            int updatedQuantity = Integer.parseInt(quantityTextField.getText());

            // Update the item data in the database using the itemId
            updateItemData(itemId, updatedItemName, updatedItemPrice, updatedQuantity);

            // After the update, you can close the form if needed
            dispose();
        });

        // Set up JFrame properties and add the updateInventoryPanel to the JFrame's content pane
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        setTitle("Update Inventory");
        add(updateInventoryPanel);
        pack(); // Pack the components to fit the preferred sizes
        setVisible(true);
    }

    private Inventory fetchInventoryData(int itemId) {
        // Fetch item data from the database using the Db class
        Db db = Db.getSingleInstance();
        List<Inventory> inventoryList = db.getInventoryList();

        // Find the item with the specified itemId in the list
        for (Inventory inventory : inventoryList) {
            if (inventory.getItemId() == itemId) {
                return inventory;
            }
        }

        // If no item is found with the given itemId, return null or handle the error accordingly
        return null;
    }

    private void updateItemData(int itemId, String updatedItemName, double updatedItemPrice, int updatedQuantity) {
        // Perform the update operation in the database using the Db class
        Db db = Db.getSingleInstance();

        // Call the updateInventory method to update the item data in the database
        boolean success = db.updateInventory(itemId, updatedItemName, updatedItemPrice, updatedQuantity);

        // Check if the update was successful
        if (success) {
            // If the update is successful, show a pop-up message
            JOptionPane.showMessageDialog(this, "Item data updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // If the update fails, you can display an error message or perform any error handling here
            JOptionPane.showMessageDialog(this, "Failed to update item data!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Close the UpdateInventory form
        dispose();

        SwingUtilities.invokeLater(() -> new InventoryList());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateInventory(1111));
    }
}
