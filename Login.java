import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn, signupBtn, forgotBtn;

    public Login() {
        setTitle("Car Rental - Login");
        setSize(450, 320);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(180, 20, 100, 30);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(170, 80, 200, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 130, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(170, 130, 200, 25);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 210, 100, 30);
        add(loginBtn);

        signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(160, 210, 100, 30);
        add(signupBtn);

        forgotBtn = new JButton("Forgot Password");
        forgotBtn.setBounds(280, 210, 150, 30);
        add(forgotBtn);

        loginBtn.addActionListener(e -> loginUser());

        signupBtn.addActionListener(e -> {
            dispose();
            new Signup();
        });

        forgotBtn.addActionListener(e -> {
            dispose();
            new ForgotPassword();
        });

        setVisible(true);
    }

    private void loginUser() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE username=? AND password=? AND status='ACTIVE'";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, usernameField.getText());
            ps.setString(2, new String(passwordField.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                JOptionPane.showMessageDialog(this, "✅ Login Successful");
                dispose();

                if (role.equalsIgnoreCase("ADMIN")) {
                    new AdminDashboard();
                } else {
                    new UserDashboard(rs.getInt("id"));
                }

            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}