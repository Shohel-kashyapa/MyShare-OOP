package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import example.model.CustomerOrder;
import example.Servies.CustomerOrderManagement;

public class Customer extends JFrame {
    private CustomerOrderManagement orderManagement;
    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel detailsLabel;
    private JTextField detailsTextField;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JList<CustomerOrder> orderList;
    private DefaultListModel<CustomerOrder> orderListModel;
    private JPanel customer;

    public Customer() {
        orderManagement = new CustomerOrderManagement();

        nameLabel = new JLabel("Customer Name:");
        nameTextField = new JTextField(20);
        detailsLabel = new JLabel("Address");
        detailsTextField = new JTextField(20);
        amountLabel = new JLabel("Quality");
        amountTextField = new JTextField(10);

        addButton = new JButton("Add Order");
        updateButton = new JButton("Update Order");
        removeButton = new JButton("Remove Order");
        updateButton.setEnabled(false); // Initially disable update and remove buttons
        removeButton.setEnabled(false);

        // JList and DefaultListModel
        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);

        // Add a selection listener to the JList to enable/disable update and remove buttons
        orderList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean isSelected = !orderList.isSelectionEmpty();
                updateButton.setEnabled(isSelected);
                removeButton.setEnabled(isSelected);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrder();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeOrder();
            }
        });

        // Create the customer panel and add components to it
        customer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        customer.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        customer.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        customer.add(detailsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        customer.add(detailsTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        customer.add(amountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        customer.add(amountTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        customer.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        customer.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        customer.add(removeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        customer.add(new JScrollPane(orderList), gbc);

        // Set up JFrame properties and add the customer panel to the JFrame's content pane
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setTitle("Manage Customer Orders");
        add(customer);
        setVisible(true);

        // Fetch the existing customer orders from the database and display them in the JList
        refreshOrderList();
    }

    private void addOrder() {
        // Retrieve customer order details from text fields (nameTextField, detailsTextField, amountTextField)
        String name = nameTextField.getText();
        String orderDetails = detailsTextField.getText();
        int quantity = Integer.parseInt(amountTextField.getText());

        int nextOrderId = 1;
        if (!orderManagement.getCustomerOrders().isEmpty()) {
            nextOrderId = orderManagement.getCustomerOrders().get(orderManagement.getCustomerOrders().size() - 1).getOrderId() + 1;
        }

        CustomerOrder newOrder = new CustomerOrder(nextOrderId, name, orderDetails, quantity);
        orderManagement.addCustomerOrder(newOrder);

        // Clear text fields after adding order
        nameTextField.setText("");
        detailsTextField.setText("");
        amountTextField.setText("");

        // Refresh the JList to display the updated order list
        refreshOrderList();
    }

    private void updateOrder() {
        // Retrieve updated customer order details from text fields (nameTextField, detailsTextField, amountTextField)
        String name = nameTextField.getText();
        String orderDetails = detailsTextField.getText();
        int quantity = Integer.parseInt(amountTextField.getText());

        CustomerOrder selectedOrder = orderList.getSelectedValue();
        if (selectedOrder != null) {
            int selectedIndex = orderList.getSelectedIndex();
            CustomerOrder updatedOrder = new CustomerOrder(selectedOrder.getOrderId(), name, orderDetails, quantity);
            orderManagement.updateCustomerOrder(updatedOrder);

            // Clear text fields after updating order
            nameTextField.setText("");
            detailsTextField.setText("");
            amountTextField.setText("");

            // Refresh the JList to display the updated order list
            refreshOrderList();

            // Restore the selection of the updated order in the JList
            orderList.setSelectedIndex(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
        }
    }

    private void removeOrder() {
        CustomerOrder selectedOrder = orderList.getSelectedValue();
        if (selectedOrder != null) {
            int selectedIndex = orderList.getSelectedIndex();
            int orderId = selectedOrder.getOrderId();
            orderManagement.removeCustomerOrder(orderId);

            // Clear text fields after removing order
            nameTextField.setText("");
            detailsTextField.setText("");
            amountTextField.setText("");

            // Refresh the JList to display the updated order list
            refreshOrderList();

            // Restore the selection of the JList
            if (orderListModel.size() > 0) {
                // If there are still orders in the list, select the next one.
                if (selectedIndex >= orderListModel.size()) {
                    // If the last order was removed, select the previous one.
                    selectedIndex = orderListModel.size() - 1;
                }
                orderList.setSelectedIndex(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to remove.");
        }
    }

    private void refreshOrderList() {
        // Clear the existing list model and re-populate it with updated order data
        orderListModel.clear();
        for (CustomerOrder order : orderManagement.getCustomerOrders()) {
            orderListModel.addElement(order);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Customer();
            }
        });
    }
}
