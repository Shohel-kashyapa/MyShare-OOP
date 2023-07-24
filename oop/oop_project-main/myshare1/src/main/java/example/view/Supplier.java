package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import example.model.Suppliers;
import example.Servies.SupplierManagement;

public class Supplier extends JFrame {
    private SupplierManagement supplierManagement;
    private DefaultListModel<Suppliers> supplierListModel;
    private JList<Suppliers> supplierList;

    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel phoneLabel;
    private JTextField phoneTextField;

    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;

    public Supplier() {
        supplierManagement = new SupplierManagement();

        supplierListModel = new DefaultListModel<>();
        supplierList = new JList<>(supplierListModel);

        // Fetch suppliers from the database and display them in the JList
        refreshSupplierList();

        supplierList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierList.addListSelectionListener(e -> {
            updateButton.setEnabled(!supplierList.isSelectionEmpty());
            removeButton.setEnabled(!supplierList.isSelectionEmpty());
        });

        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(20);
        emailLabel = new JLabel("order details");
        emailTextField = new JTextField(20);
        phoneLabel = new JLabel("price");
        phoneTextField = new JTextField(15);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        removeButton = new JButton("Remove");
        updateButton.setEnabled(false);
        removeButton.setEnabled(false);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSupplier();
            }
        });

        JPanel supplierPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        supplierPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        supplierPanel.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        supplierPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        supplierPanel.add(emailTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        supplierPanel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        supplierPanel.add(phoneTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        supplierPanel.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        supplierPanel.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        supplierPanel.add(removeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        supplierPanel.add(new JScrollPane(supplierList), gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setTitle("Manage Suppliers");
        add(supplierPanel);
        setVisible(true);
    }

    private void addSupplier() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        String phone = phoneTextField.getText();

        Suppliers newSupplier = new Suppliers(0, name, email, phone);
        supplierManagement.addSupplier(newSupplier);

        // Clear text fields after adding supplier
        nameTextField.setText("");
        emailTextField.setText("");
        phoneTextField.setText("");

        // Refresh the JList to display the updated supplier list
        refreshSupplierList();
    }

    private void updateSupplier() {
        Suppliers selectedSupplier = supplierList.getSelectedValue();
        if (selectedSupplier != null) {
            int selectedIndex = supplierList.getSelectedIndex();
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String phone = phoneTextField.getText();

            Suppliers updatedSupplier = new Suppliers(selectedSupplier.getId(), name, email, phone);
            supplierManagement.updateSupplier(updatedSupplier);

            // Clear text fields after updating supplier
            nameTextField.setText("");
            emailTextField.setText("");
            phoneTextField.setText("");

            // Refresh the JList to display the updated supplier list
            refreshSupplierList();

            // Restore the selection of the updated supplier in the JList
            supplierList.setSelectedIndex(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to update.");
        }
    }

    private void removeSupplier() {
        Suppliers selectedSupplier = supplierList.getSelectedValue();
        if (selectedSupplier != null) {
            int supplierId = selectedSupplier.getId();
            supplierManagement.removeSupplier(supplierId);

            // Clear text fields after removing supplier
            nameTextField.setText("");
            emailTextField.setText("");
            phoneTextField.setText("");

            // Refresh the JList to display the updated supplier list
            refreshSupplierList();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to remove.");
        }
    }

    private void refreshSupplierList() {
        // Clear the existing list model and re-populate it with updated supplier data
        supplierListModel.clear();
        List<Suppliers> suppliers = supplierManagement.getSuppliers();
        for (Suppliers supplier : suppliers) {
            supplierListModel.addElement(supplier);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Supplier();
            }
        });
    }
}
