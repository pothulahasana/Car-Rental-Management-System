import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Users extends JFrame {

    JTable table;
    DefaultTableModel model;
    JButton blockBtn, unblockBtn, deleteBtn, refreshBtn;

    public Users() {
        setTitle("Users Management");
        setSize(950, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Table columns
        String[] cols = {
                "ID", "Username", "Email", "Full Name",
                "Phone", "Role", "Status", "Created At"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom buttons
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        blockBtn = new JButton("Block");
        unblockBtn = new JButton("Unblock");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");

        panel.add(blockBtn);
        panel.add(unblockBtn);
        panel.add(deleteBtn);
        panel.add(refreshBtn);

        add(panel, BorderLayout.SOUTH);

        loadUsers();

        blockBtn.addActionListener(e -> updateStatus("BLOCKED"));
        unblockBtn.addActionListener(e -> updateStatus("ACTIVE"));
        deleteBtn.addActionListener(e -> deleteUser());
        refreshBtn.addActionListener(e -> loadUsers());

        setVisible(true);
    }

    void loadUsers() {
        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users");
            e.printStackTrace();
        }
    }

    void updateStatus(String status) {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement("UPDATE users SET status=? WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User status updated");
            loadUsers();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update failed");
            e.printStackTrace();
        }
    }

    void deleteUser() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement("DELETE FROM users WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User deleted successfully");
            loadUsers();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete failed");
            e.printStackTrace();
        }
    }
}