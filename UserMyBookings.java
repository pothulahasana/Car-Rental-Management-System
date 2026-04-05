import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserMyBookings extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public UserMyBookings(int userId) {

        this.userId = userId;

        setTitle("My Bookings");
        setSize(900,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] cols = {
                "Booking ID",
                "Car Name",
                "Rent Date",
                "Return Date",
                "Total Amount",
                "Status"
        };

        model = new DefaultTableModel(cols,0);
        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton returnBtn = new JButton("Return Car");

        bottom.add(returnBtn);

        add(bottom,BorderLayout.SOUTH);

        returnBtn.addActionListener(e -> returnCar());

        loadBookings();

        setVisible(true);
    }

    // ===== LOAD USER BOOKINGS =====
    private void loadBookings() {

        model.setRowCount(0);

        try(Connection con = DBConnection.getConnection()) {

            String query =
                    "SELECT b.id, c.car_name, b.rent_date, b.return_date, b.total_amount, b.status " +
                    "FROM bookings b " +
                    "JOIN cars c ON b.car_id = c.id " +
                    "WHERE b.user_id = ? " +
                    "ORDER BY b.id DESC";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getDate("rent_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                });

            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // ===== RETURN CAR =====
    private void returnCar() {

        int row = table.getSelectedRow();

        if(row == -1) {
            JOptionPane.showMessageDialog(this,"Select a booking first!");
            return;
        }

        int bookingId = (int) model.getValueAt(row,0);
        String status = model.getValueAt(row,5).toString();

        if(status.equalsIgnoreCase("RETURNED")) {

            JOptionPane.showMessageDialog(this,"Car already returned!");
            return;
        }

        try(Connection con = DBConnection.getConnection()) {

            // Get car_id and return_date
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT car_id, return_date FROM bookings WHERE id=?");

            ps1.setInt(1,bookingId);

            ResultSet rs = ps1.executeQuery();
            rs.next();

            int carId = rs.getInt("car_id");
            Date returnDate = rs.getDate("return_date");

            // Late fine calculation
            LocalDate expected = returnDate.toLocalDate();
            LocalDate today = LocalDate.now();

            long lateDays = ChronoUnit.DAYS.between(expected,today);

            if(lateDays > 0) {

                double fine = lateDays * 500;

                JOptionPane.showMessageDialog(
                        this,
                        "Late Return!\nFine = ₹" + fine
                );
            }

            // Update booking status
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE bookings SET status='RETURNED' WHERE id=?");

            ps2.setInt(1,bookingId);
            ps2.executeUpdate();

            // Make car available again
            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE cars SET available=TRUE WHERE id=?");

            ps3.setInt(1,carId);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this,"Car Returned Successfully!");

            loadBookings();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}