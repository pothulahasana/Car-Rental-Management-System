import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteCar extends JFrame {

    private JTextField idField;

    public DeleteCar() {

        setTitle("Delete Car");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(220, 53, 69)); // Red theme
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("DELETE CAR");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        centerPanel.setBackground(Color.WHITE);

        idField = new JTextField();

        centerPanel.add(new JLabel("Enter Car ID:"));
        centerPanel.add(idField);

        add(centerPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        deleteBtn.addActionListener(e -> deleteCar());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void deleteCar() {

        String id = idField.getText();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Car ID!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this car?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM cars WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(id));

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Car Deleted Successfully!");
                idField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Car Not Found!");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }
}