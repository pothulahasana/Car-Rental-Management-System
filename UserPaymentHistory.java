import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserPaymentHistory extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public UserPaymentHistory(int userId) {

        this.userId = userId;

        setTitle("My Payment History");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {
                "Payment ID",
                "Booking ID",
                "Car Name",
                "Amount",
                "Payment Mode",
                "Payment Date"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadPayments();
        setVisible(true);
    }

    private void loadPayments() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection()) {

            String query =
            "SELECT p.id, p.booking_id, c.car_name, p.amount, p.payment_mode, p.payment_date " +
            "FROM payments p " +
            "JOIN bookings b ON p.booking_id = b.id " +
            "JOIN cars c ON b.car_id = c.id " +
            "WHERE b.user_id = ? " +
            "ORDER BY p.payment_date DESC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getString("car_name"),
                        rs.getDouble("amount"),
                        rs.getString("payment_mode"), // ✅ FIXED
                        rs.getTimestamp("payment_date") // ✅ FIXED
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No payment history found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading payments!");
        }
    }
}