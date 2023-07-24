package example.view;

import example.model.Inventory;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class InventoryTableModel extends DefaultTableModel {

    // Column constants
    public static final int COLUMN_ITEM_ID = 0;
    public static final int COLUMN_ITEM_NAME = 1;
    public static final int COLUMN_ITEM_PRICE = 2;
    public static final int COLUMN_QUANTITY = 3;
    public static final int COLUMN_ACTION = 4;
    private List<Inventory> data;
    // Constructor
    public InventoryTableModel() {
        super();
        // Set the column names for the table
        String[] columnNames = { "ItemId", "Item Name", "Item Price", "Quantity", "Action" };
        this.setColumnIdentifiers(columnNames);
    }

    // Set your data
    public void setData(List<Object[]> data) {
        for (Object[] rowData : data) {
            this.addRow(rowData);
        }
    }

    public List<Inventory> getData (){
        return data;
    }



    // Override the isCellEditable method to make cells non-editable
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

