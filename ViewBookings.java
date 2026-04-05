import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class ViewBookings extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;
    TableRowSorter<DefaultTableModel> sorter;

    public ViewBookings() {

        setTitle("View Bookings");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== HEADER WITH SEARCH =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setPreferredSize(new Dimension(0, 70));

        JLabel title = new JLabel("BOOKING LIST");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        searchField = new JTextField(20);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(25, 118, 210));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = {
                "Booking ID", "User", "Car",
                "Rent Date", "Return Date",
                "Total Amount", "Status"
        };

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel panel = new JPanel();
        JButton returnBtn = new JButton("Return Car");
        panel.add(returnBtn);
        add(panel, BorderLayout.SOUTH);

        loadBookings();

        // ===== SEARCH LOGIC =====
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        returnBtn.addActionListener(e -> returnCar());

        setVisible(true);
    }

    // ================= LOAD BOOKINGS =================
    void loadBookings() {

        model.setRowCount(0);

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT b.id, u.username, c.car_name, " +
                             "b.rent_date, b.return_date, b.total_amount, b.status " +
                             "FROM bookings b " +
                             "JOIN users u ON b.user_id = u.id " +
                             "JOIN cars c ON b.car_id = c.id")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("car_name"),
                        rs.getDate("rent_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= RETURN CAR =================
    void returnCar() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select Booking First!");
            return;
        }

        int bookingId = (int) table.getValueAt(row, 0);
        String carName = (String) table.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Return car: " + carName + " ?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT car_id FROM bookings WHERE id=?");
            ps1.setInt(1, bookingId);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            int carId = rs.getInt("car_id");

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE bookings SET status='RETURNED' WHERE id=?");
            ps2.setInt(1, bookingId);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE cars SET available=TRUE WHERE id=?");
            ps3.setInt(1, carId);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car Returned Successfully!");

            loadBookings();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}