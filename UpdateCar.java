import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateCar extends JFrame {

    private JTextField idField, nameField, brandField, modelField, priceField;
    private JComboBox<String> engineBox, transmissionBox, availableBox;

    public UpdateCar() {

        setTitle("Update Car");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);

        idField = new JTextField(8);
        JButton searchBtn = new JButton("Search");

        searchPanel.add(new JLabel("Enter Car ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);

        add(searchPanel, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField();
        brandField = new JTextField();
        modelField = new JTextField();
        priceField = new JTextField();

        engineBox = new JComboBox<>(new String[]{"Petrol","Diesel","Electric"});
        transmissionBox = new JComboBox<>(new String[]{"Manual","Automatic"});
        availableBox = new JComboBox<>(new String[]{"Yes","No"});

        addRow(formPanel, gbc, 0, "Car Name:", nameField);
        addRow(formPanel, gbc, 1, "Brand:", brandField);
        addRow(formPanel, gbc, 2, "Model:", modelField);
        addRow(formPanel, gbc, 3, "Engine Type:", engineBox);
        addRow(formPanel, gbc, 4, "Transmission:", transmissionBox);
        addRow(formPanel, gbc, 5, "Price Per Day:", priceField);
        addRow(formPanel, gbc, 6, "Available:", availableBox);

        add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton updateBtn = new JButton("Update");
        JButton closeBtn = new JButton("Close");

        updateBtn.setBackground(new Color(25,118,210));
        updateBtn.setForeground(Color.WHITE);

        buttonPanel.add(updateBtn);
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ACTIONS
        searchBtn.addActionListener(e -> searchCar());
        updateBtn.addActionListener(e -> updateCar());
        closeBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ===== ROW CREATOR =====
    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
                        String labelText, JComponent field) {

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // ===== SEARCH =====
    private void searchCar() {

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM cars WHERE id=?");
            ps.setInt(1, Integer.parseInt(idField.getText()));

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                nameField.setText(rs.getString("car_name"));
                brandField.setText(rs.getString("brand"));
                modelField.setText(rs.getString("model"));
                priceField.setText(String.valueOf(rs.getDouble("price_per_day")));

                engineBox.setSelectedItem(rs.getString("engine_type"));
                transmissionBox.setSelectedItem(rs.getString("transmission"));
                availableBox.setSelectedItem(
                        rs.getBoolean("available") ? "Yes" : "No");

            } else {
                JOptionPane.showMessageDialog(this,"Car Not Found!");
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Invalid ID!");
        }
    }

    // ===== UPDATE =====
    private void updateCar() {

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE cars SET car_name=?, brand=?, model=?, engine_type=?, transmission=?, price_per_day=?, available=? WHERE id=?");

            ps.setString(1, nameField.getText());
            ps.setString(2, brandField.getText());
            ps.setString(3, modelField.getText());
            ps.setString(4, engineBox.getSelectedItem().toString());
            ps.setString(5, transmissionBox.getSelectedItem().toString());
            ps.setDouble(6, Double.parseDouble(priceField.getText()));
            ps.setBoolean(7, availableBox.getSelectedItem().toString().equals("Yes"));
            ps.setInt(8, Integer.parseInt(idField.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Car Updated Successfully!");

        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Update Failed!");
        }
    }
}