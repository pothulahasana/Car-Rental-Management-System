import javax.swing.*;
import java.sql.*;

public class Signup extends JFrame {
    JTextField usernameField, emailField, fullNameField, phoneField;
    JPasswordField passwordField;
    JTextField questionField, answerField;
    JButton registerBtn, backBtn;

    public Signup() {
        setTitle("Sign Up");
        setSize(450, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(50, 30, 100, 25);
        add(l1);
        usernameField = new JTextField();
        usernameField.setBounds(180, 30, 180, 25);
        add(usernameField);

        JLabel l2 = new JLabel("Email:");
        l2.setBounds(50, 70, 100, 25);
        add(l2);
        emailField = new JTextField();
        emailField.setBounds(180, 70, 180, 25);
        add(emailField);

        JLabel l3 = new JLabel("Password:");
        l3.setBounds(50, 110, 100, 25);
        add(l3);
        passwordField = new JPasswordField();
        passwordField.setBounds(180, 110, 180, 25);
        add(passwordField);

        JLabel l4 = new JLabel("Full Name:");
        l4.setBounds(50, 150, 100, 25);
        add(l4);
        fullNameField = new JTextField();
        fullNameField.setBounds(180, 150, 180, 25);
        add(fullNameField);

        JLabel l5 = new JLabel("Phone:");
        l5.setBounds(50, 190, 100, 25);
        add(l5);
        phoneField = new JTextField();
        phoneField.setBounds(180, 190, 180, 25);
        add(phoneField);

        JLabel l6 = new JLabel("Question:");
        l6.setBounds(50, 230, 100, 25);
        add(l6);
        questionField = new JTextField();
        questionField.setBounds(180, 230, 180, 25);
        add(questionField);

        JLabel l7 = new JLabel("Answer:");
        l7.setBounds(50, 270, 100, 25);
        add(l7);
        answerField = new JTextField();
        answerField.setBounds(180, 270, 180, 25);
        add(answerField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(80, 350, 120, 30);
        add(registerBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(220, 350, 120, 30);
        add(backBtn);

        registerBtn.addActionListener(e -> registerUser());

        backBtn.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    private void registerUser() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO users(username,email,password,full_name,phone,security_question,security_answer) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, usernameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, new String(passwordField.getPassword()));
            ps.setString(4, fullNameField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, questionField.getText());
            ps.setString(7, answerField.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Registration Successful");
            dispose();
            new Login();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}