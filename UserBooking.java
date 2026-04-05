import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserBooking extends JFrame {

    private int userId;

    private JComboBox<String> carBox, paymentModeBox;
    private JTextField rentDateField, returnDateField, totalField;

    public UserBooking(int userId) {

        this.userId = userId;

        setTitle("Book Car");
        setSize(450,400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7,2,10,10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        carBox = new JComboBox<>();
        paymentModeBox = new JComboBox<>(new String[]{"Cash","UPI","Card"});

        rentDateField = new JTextField("YYYY-MM-DD");
        returnDateField = new JTextField("YYYY-MM-DD");

        totalField = new JTextField();
        totalField.setEditable(false);

        JButton calcBtn = new JButton("Calculate");
        JButton bookBtn = new JButton("Confirm Booking");

        add(new JLabel("Select Car"));
        add(carBox);

        add(new JLabel("Rent Date"));
        add(rentDateField);

        add(new JLabel("Return Date"));
        add(returnDateField);

        add(new JLabel("Payment Mode"));
        add(paymentModeBox);

        add(new JLabel("Total Amount"));
        add(totalField);

        add(calcBtn);
        add(bookBtn);

        loadAvailableCars();

        calcBtn.addActionListener(e -> calculateTotal());
        bookBtn.addActionListener(e -> bookCar());

        setVisible(true);
    }

    private void loadAvailableCars() {

        try(Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT id,car_name FROM cars WHERE available=TRUE");

            while(rs.next()) {
                carBox.addItem(rs.getInt("id") + " - " + rs.getString("car_name"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateTotal() {

        try {

            String carItem = (String) carBox.getSelectedItem();

            if(carItem == null) {
                JOptionPane.showMessageDialog(this,"Select a car!");
                return;
            }

            int carId = Integer.parseInt(carItem.split(" - ")[0]);

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT price_per_day FROM cars WHERE id=?");

            ps.setInt(1,carId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            double price = rs.getDouble("price_per_day");

            LocalDate start = LocalDate.parse(rentDateField.getText());
            LocalDate end = LocalDate.parse(returnDateField.getText());

            long days = ChronoUnit.DAYS.between(start,end);

            if(days <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Return date must be after rent date!");
                return;
            }

            double total = days * price;

            totalField.setText(String.valueOf(total));

            con.close();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Date Format (YYYY-MM-DD)");
        }
    }

    private void bookCar() {

        try {

            if(totalField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Calculate total first!");
                return;
            }

            String carItem = (String) carBox.getSelectedItem();
            int carId = Integer.parseInt(carItem.split(" - ")[0]);

            double amount = Double.parseDouble(totalField.getText());
            String paymentMode = paymentModeBox.getSelectedItem().toString();

            Connection con = DBConnection.getConnection();

            // INSERT BOOKING
            String bookingSQL =
            "INSERT INTO bookings(user_id,car_id,rent_date,return_date,total_amount,status) VALUES(?,?,?,?,?,'BOOKED')";

            PreparedStatement ps =
            con.prepareStatement(bookingSQL, Statement.RETURN_GENERATED_KEYS);

            LocalDate startDate = LocalDate.parse(rentDateField.getText());
            LocalDate endDate = LocalDate.parse(returnDateField.getText());

            ps.setInt(1,userId);
            ps.setInt(2,carId);
            ps.setDate(3, java.sql.Date.valueOf(startDate));
            ps.setDate(4, java.sql.Date.valueOf(endDate));
            ps.setDouble(5,amount);

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int bookingId = 0;

            if(keys.next()) {
                bookingId = keys.getInt(1);
            }

            // INSERT PAYMENT (FINAL FIX)
            String paymentSQL =
            "INSERT INTO payments(booking_id,amount,payment_mode,payment_date) VALUES(?,?,?,?)";

            PreparedStatement pay = con.prepareStatement(paymentSQL);

            pay.setInt(1,bookingId);
            pay.setDouble(2,amount);
            pay.setString(3,paymentMode);
            pay.setDate(4,new java.sql.Date(System.currentTimeMillis()));

            pay.executeUpdate();

            // UPDATE CAR
            PreparedStatement updateCar =
            con.prepareStatement("UPDATE cars SET available=FALSE WHERE id=?");

            updateCar.setInt(1,carId);
            updateCar.executeUpdate();

            JOptionPane.showMessageDialog(this,"Booking Successful!");

            dispose();
            con.close();

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());
            e.printStackTrace();
        }
    }
}