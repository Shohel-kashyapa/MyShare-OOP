package example.view;

import example.config.Db;
import example.model.Inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InventoryList extends JFrame {
    private JTable table;
    private InventoryTableModel tableModel;

    public InventoryList() {
        // Create the inventory list panel and add components to it
        JPanel inventoryListPanel = new JPanel(new BorderLayout());

        // Create the InventoryTableModel and set it to the JTable
        tableModel = new InventoryTableModel();
        table = new JTable(tableModel);

        // Customize the "Action" column to display buttons
        table.getColumnModel().getColumn(InventoryTableModel.COLUMN_ACTION).setCellRenderer(new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener());

        // Add the JTable to a JScrollPane to enable scrolling if necessary
        JScrollPane scrollPane = new JScrollPane(table);
        inventoryListPanel.add(scrollPane, BorderLayout.CENTER);

        // Set up JFrame properties and add the inventory list panel to the JFrame's content pane
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setTitle("Current Inventories");
        add(inventoryListPanel);
        setVisible(true);

        // Fetch inventory data from the database and populate the table
        initData();
    }

    private void initData() {
        // Fetch inventory data from the database
        Db db = Db.getSingleInstance();
        List<Inventory> inventoryList = db.getInventoryList();

        // Populate the table with inventory data
        List<Object[]> data = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            Object[] rowData = { inventory.getItemId(), inventory.getItemName(), inventory.getItemPrice(), inventory.getQuantity(), "Update" };
            data.add(rowData);
        }
        tableModel.setData(data);
    }

    // Custom cell renderer for the "Action" column to display buttons
    private class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public ButtonRenderer() {
            button = new JButton("Update");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return button;
        }
    }

    // Mouse listener to handle the "Update" button click
    private class ButtonMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();

            // Check if the click is within the bounds of the "Action" column and not outside the table
            if (row < table.getRowCount() && row >= 0 && column == InventoryTableModel.COLUMN_ACTION) {
                // Handle the "Update" action here
                // You can get the item ID from the selected row and open the "UpdateInventory" UI
                int itemId = (int) table.getValueAt(row, InventoryTableModel.COLUMN_ITEM_ID);
                openUpdateInventoryUI(itemId);
            }
        }
    }

    // Method to open the "UpdateInventory" UI with the selected item ID
    private void openUpdateInventoryUI(int itemId) {
        // Close the current frame (InventoryList)
        dispose();

        // Create and display the UpdateInventoryForm with the selected item ID
        SwingUtilities.invokeLater(() -> new UpdateInventory(itemId));
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(InventoryList::new);
    }
}