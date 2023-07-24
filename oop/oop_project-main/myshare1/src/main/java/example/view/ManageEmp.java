package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;



public class ManageEmp extends JFrame {
    private JButton ADDButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton updateButton;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton removeButton;
    private JTextField textField4;

    private Map<Integer, Employee> employees;

    public ManageEmp() {
        employees = new HashMap<>(); // Initialize the map to store employees

        // Create the manage employees panel and add components to it
        JPanel manageEmpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Manage Employees");
        manageEmpPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel addEmpLabel = new JLabel("Add Employee");
        manageEmpPanel.add(addEmpLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Name:");
        manageEmpPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField1 = new JTextField(20);
        manageEmpPanel.add(textField1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel empIdLabel = new JLabel("Emp ID:");
        manageEmpPanel.add(empIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField2 = new JTextField(10);
        manageEmpPanel.add(textField2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel emailLabel = new JLabel("Email:");
        manageEmpPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField3 = new JTextField(20);
        manageEmpPanel.add(textField3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        ADDButton = new JButton("ADD");
        manageEmpPanel.add(ADDButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel updateEmpLabel = new JLabel("Update Employee");
        manageEmpPanel.add(updateEmpLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel empIdToUpdateLabel = new JLabel("Emp ID:");
        manageEmpPanel.add(empIdToUpdateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField5 = new JTextField(10);
        manageEmpPanel.add(textField5, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel newNameLabel = new JLabel("Change Name:");
        manageEmpPanel.add(newNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField6 = new JTextField(20);
        manageEmpPanel.add(textField6, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        JLabel newEmailLabel = new JLabel("Change Email:");
        manageEmpPanel.add(newEmailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField7 = new JTextField(20);
        manageEmpPanel.add(textField7, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        updateButton = new JButton("Update");
        manageEmpPanel.add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        JLabel removeEmpLabel = new JLabel("Remove Employee");
        manageEmpPanel.add(removeEmpLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        JLabel empIdToRemoveLabel = new JLabel("Emp ID:");
        manageEmpPanel.add(empIdToRemoveLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textField4 = new JTextField(10);
        manageEmpPanel.add(textField4, gbc);

        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        removeButton = new JButton("Remove");
        manageEmpPanel.add(removeButton, gbc);

        // Set up JFrame properties and add the manage employees panel to the JFrame's content pane
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setTitle("Manage Employees");
        add(manageEmpPanel);
        setVisible(true);

        // Add action listeners to buttons
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEmployee();
            }
        });
    }

    private void addEmployee() {
        String name = textField1.getText();
        String empIdInput = textField2.getText();
        String email = textField3.getText();

        // Validate that empId contains only numerical characters
        if (!empIdInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Emp ID. Please enter a valid integer.");
            return;
        }

        int empId = Integer.parseInt(empIdInput);

        if (employees.containsKey(empId)) {
            JOptionPane.showMessageDialog(this, "Employee with the same Emp ID already exists.");
            return;
        }

        Employee employee = new Employee(name, email);
        employees.put(empId, employee);

        JOptionPane.showMessageDialog(this, "Employee added successfully!");

        // Clear text fields after adding employee
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
    }

    private void updateEmployee() {
        String empIdToUpdateInput = textField5.getText();

        // Validate that empIdToUpdate contains only numerical characters
        if (!empIdToUpdateInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Emp ID. Please enter a valid integer.");
            return;
        }

        int empIdToUpdate = Integer.parseInt(empIdToUpdateInput);

        if (!employees.containsKey(empIdToUpdate)) {
            JOptionPane.showMessageDialog(this, "Employee with the specified Emp ID does not exist.");
            return;
        }

        String newName = textField6.getText();
        String newEmail = textField7.getText();

        Employee employee = employees.get(empIdToUpdate);
        employee.setName(newName);
        employee.setEmail(newEmail);

        JOptionPane.showMessageDialog(this, "Employee updated successfully!");

        // Clear text fields after updating employee
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    private void removeEmployee() {
        String empIdToRemoveInput = textField4.getText();

        // Validate that empIdToRemove contains only numerical characters
        if (!empIdToRemoveInput.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid Emp ID. Please enter a valid integer.");
            return;
        }

        int empIdToRemove = Integer.parseInt(empIdToRemoveInput);

        if (!employees.containsKey(empIdToRemove)) {
            JOptionPane.showMessageDialog(this, "Employee with the specified Emp ID does not exist.");
            return;
        }

        employees.remove(empIdToRemove);

        JOptionPane.showMessageDialog(this, "Employee removed successfully!");

        // Clear text field after removing employee
        textField4.setText("");
    }

    private static class Employee {
        private String name;
        private String email;

        public Employee(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManageEmp();
            }
        });
    }
}