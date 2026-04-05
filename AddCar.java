import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCar extends JFrame {

    private JTextField carNameField, brandField, modelField, priceField;
    private JComboBox<String> engineBox, transmissionBox, availableBox;

    public AddCar() {
        setTitle("Add Car");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 20, 50));
        formPanel.setBackground(Color.WHITE);

        carNameField = new JTextField();
        brandField = new JTextField();
        modelField = new JTextField();
        priceField = new JTextField();

        engineBox = new JComboBox<>(new String[]{"Petrol", "Diesel", "Electric"});
        transmissionBox = new JComboBox<>(new String[]{"Manual", "Automatic"});
        availableBox = new JComboBox<>(new String[]{"Yes", "No"});

        formPanel.add(new JLabel("Car Name:"));
        formPanel.add(carNameField);

        formPanel.add(new JLabel("Brand:"));
        formPanel.add(brandField);

        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);

        formPanel.add(new JLabel("Engine Type:"));
        formPanel.add(engineBox);

        formPanel.add(new JLabel("Transmission:"));
        formPanel.add(transmissionBox);

        formPanel.add(new JLabel("Price Per Day:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Available:"));
        formPanel.add(availableBox);

        add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("Save");
        JButton clearBtn = new JButton("Clear");
        JButton backBtn = new JButton("Back");

        saveBtn.setBackground(new Color(25, 118, 210));
        saveBtn.setForeground(Color.WHITE);

        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        saveBtn.addActionListener(e -> saveCar());
        clearBtn.addActionListener(e -> clearFields());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveCar() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO cars(car_name, brand, model, engine_type, transmission, price_per_day, available) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, carNameField.getText());
            pst.setString(2, brandField.getText());
            pst.setString(3, modelField.getText());
            pst.setString(4, engineBox.getSelectedItem().toString());
            pst.setString(5, transmissionBox.getSelectedItem().toString());
            pst.setDouble(6, Double.parseDouble(priceField.getText()));
            pst.setBoolean(7, availableBox.getSelectedItem().toString().equals("Yes"));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car Added Successfully");
            clearFields();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Adding Car");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        carNameField.setText("");
        brandField.setText("");
        modelField.setText("");
        priceField.setText("");
        engineBox.setSelectedIndex(0);
        transmissionBox.setSelectedIndex(0);
        availableBox.setSelectedIndex(0);
    }
}