package example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import example.model.User;

public class Login extends JDialog {
    private JTextField users;
    private JPasswordField ps;
    private JButton lg;
    private JButton acc;
    private JPanel loginPanel;
    private JComboBox<String> comboBox1;

    private User loggedInUser;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Login");

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(loginPanel, BorderLayout.CENTER);
        setContentPane(contentPane);

        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        lg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = users.getText();
                String password = new String(ps.getPassword());
                String selectedRole = (String) comboBox1.getSelectedItem();

                loggedInUser = getAuthenticatedUser(username, password);

                if (loggedInUser != null && loggedInUser.getSetRole() != null && loggedInUser.getSetRole().equalsIgnoreCase(selectedRole)) {
                    dispose();
                    openAppropriatePage(loggedInUser.getSetRole());
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Invalid username or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        acc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register registerDialog = new Register(Login.this);
                registerDialog.setVisible(true);
            }
        });

        setVisible(true);
    }

    private User getAuthenticatedUser(String username, String password) {
        User user = null;

        final String URL = "jdbc:mysql://localhost:3306/myshare?user=root&password=";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String role = resultSet.getString("setRole");

                user = new User(name, email, phone, address, password, null);
                user.setUsername(username);
                user.setSetRole(role);
            }

            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private void openAppropriatePage(String role) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if ("customer".equalsIgnoreCase(role)) {
                    new Customer();
                } else if ("supplier".equalsIgnoreCase(role)) {
                    new Supplier();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid user role", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        Login loginForm = new Login(null);
        User user = loginForm.loggedInUser;
        if (user != null) {
            System.out.println("          Username: " + user.getUsername());
            System.out.println("          Password: " + user.getPassword());
            System.out.println("          Role: " + user.getSetRole());
        } else {
            System.out.println("Authentication canceled");
        }
    }
}
