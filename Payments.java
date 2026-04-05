import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Payments extends JFrame {

    JTable table;
    DefaultTableModel model;

    public Payments() {

        setTitle("All Payments");
        setSize(1000, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {
                "Payment ID",
                "Booking ID",
                "Username",
                "Car Name",
                "Amount",
                "Payment Mode",
                "Payment Date"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        panel.add(refreshBtn);
        add(panel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadPayments());

        loadPayments();
        setVisible(true);
    }

    void loadPayments() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection()) {

            // ✅ FIXED QUERY (includes payment_mode)
            String query =
                    "SELECT p.id, p.booking_id, u.username, " +
                    "c.car_name, p.amount, p.payment_mode, p.payment_date " +
                    "FROM payments p " +
                    "JOIN bookings b ON p.booking_id = b.id " +
                    "JOIN users u ON b.user_id = u.id " +
                    "JOIN cars c ON b.car_id = c.id " +
                    "ORDER BY p.payment_date DESC";

            ResultSet rs = con.createStatement().executeQuery(query);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getString("username"),
                        rs.getString("car_name"),
                        rs.getDouble("amount"),
                        rs.getString("payment_mode"), // ✅ FIXED
                        rs.getTimestamp("payment_date") // ✅ timestamp support
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payments");
        }
    }
}