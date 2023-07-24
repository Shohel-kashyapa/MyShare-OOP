// Register.java
package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import example.model.User;

public class Register extends JDialog {
    private JPanel registerPanel;
    private JTextField nametxt;
    private JTextField usernametxt;
    private JTextField emailtxt;
    private JTextField addresstxt;
    private JPasswordField passwordtxt;
    private JPasswordField confirmPasstxt;
    private JButton signinButton;
    private JButton cancelButton;
    private JTextField contxt;
    private JComboBox<String> comboBox1;

    public Register(JDialog parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        signinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private void registerUser() {
        String name = nametxt.getText();
        String username = usernametxt.getText();
        String email = emailtxt.getText();
        String phone = contxt.getText();
        String address = addresstxt.getText();
        String password = new String(passwordtxt.getPassword());
        String confirmPassword = new String(confirmPasstxt.getPassword());
        String setRole = (String) comboBox1.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || setRole.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Input a valid email",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = addUserToDatabase(name, username, email, phone, address, password, setRole);
        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful for: " + user.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private User addUserToDatabase(String name, String username, String email, String phone, String address, String password, String setRole) {
        User user = null;
        final String URL = "jdbc:mysql://localhost:3306/myshare?user=root&password=";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "INSERT INTO users (name, username, email, phone, address, password, setRole) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, setRole);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User(name, email, phone, address, password, setRole);
                user.setUsername(username);
            }

            preparedStatement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        Register myForm = new Register(null);
    }
}
