import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ForgotPassword extends JFrame {
    private JTextField usernameField;
    private JTextField answerField;
    private JPasswordField newPasswordField;
    private JTextField questionField;
    private JButton getQuestionBtn;
    private JButton resetBtn;
    private JButton backBtn;

    public ForgotPassword() {
        setTitle("Forgot Password");
        setSize(500, 380);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title
        JLabel title = new JLabel("Reset Password");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(160, 20, 200, 30);
        add(title);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 120, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 220, 25);
        add(usernameField);

        // Get Question Button
        getQuestionBtn = new JButton("Get Question");
        getQuestionBtn.setBounds(160, 120, 150, 30);
        add(getQuestionBtn);

        // Security Question
        JLabel questionLabel = new JLabel("Security Question:");
        questionLabel.setBounds(50, 170, 120, 25);
        add(questionLabel);

        questionField = new JTextField();
        questionField.setBounds(200, 170, 220, 25);
        questionField.setEditable(false);
        add(questionField);

        // Answer
        JLabel answerLabel = new JLabel("Your Answer:");
        answerLabel.setBounds(50, 210, 120, 25);
        add(answerLabel);

        answerField = new JTextField();
        answerField.setBounds(200, 210, 220, 25);
        add(answerField);

        // New Password
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setBounds(50, 250, 120, 25);
        add(passwordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(200, 250, 220, 25);
        add(newPasswordField);

        // Buttons
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(120, 300, 100, 30);
        add(resetBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(260, 300, 100, 30);
        add(backBtn);

        // Actions
        getQuestionBtn.addActionListener(e -> loadQuestion());
        resetBtn.addActionListener(e -> resetPassword());

        backBtn.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

    private void loadQuestion() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT security_question FROM users WHERE username=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, usernameField.getText());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                questionField.setText(rs.getString("security_question"));
            } else {
                JOptionPane.showMessageDialog(this, "Username not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPassword() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE users SET password=? WHERE username=? AND security_answer=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, new String(newPasswordField.getPassword()));
            ps.setString(2, usernameField.getText());
            ps.setString(3, answerField.getText());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Password Reset Successful");
                dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Answer");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}