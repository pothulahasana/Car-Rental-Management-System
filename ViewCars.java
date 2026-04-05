import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class ViewCars extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public ViewCars() {

        setTitle("View Cars");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25,118,210));
        header.setPreferredSize(new Dimension(0,70));

        JLabel title = new JLabel("CAR LIST");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial",Font.BOLD,22));
        title.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));

        // ===== SEARCH =====
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial",Font.PLAIN,14));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(25,118,210));

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setForeground(Color.WHITE);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        header.add(title,BorderLayout.WEST);
        header.add(searchPanel,BorderLayout.EAST);

        add(header,BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel();

        model.setColumnIdentifiers(new String[]{
                "ID",
                "Car Name",
                "Brand",
                "Model",
                "Engine",
                "Transmission",
                "Price/Day",
                "Available",
                "Created At"
        });

        table = new JTable(model);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);

        // Load data
        loadCarData();

        // ===== SEARCH LOGIC =====
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {

                String text = searchField.getText();

                if(text.trim().length()==0)
                {
                    sorter.setRowFilter(null);
                }
                else
                {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+text));
                }

            }
        });

        setVisible(true);
    }

    // ===== LOAD DATA FROM DATABASE =====
    private void loadCarData() {

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM cars");

            // Clear old rows
            model.setRowCount(0);

            while(rs.next())
            {

                String availability;

                if(rs.getBoolean("available"))
                {
                    availability = "Yes";
                }
                else
                {
                    availability = "No";
                }

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("engine_type"),
                        rs.getString("transmission"),
                        rs.getDouble("price_per_day"),
                        availability,
                        rs.getTimestamp("created_at")
                });

            }

            con.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Database Error!");
        }

    }

}