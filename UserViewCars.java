import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserViewCars extends JFrame {

    JTable table;
    DefaultTableModel model;

    public UserViewCars() {

        setTitle("Available Cars");
        setSize(900,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] cols={
                "Car ID","Car Name","Brand",
                "Model","Engine","Transmission","Price Per Day"
        };

        model=new DefaultTableModel(cols,0);
        table=new JTable(model);

        add(new JScrollPane(table),BorderLayout.CENTER);

        loadCars();

        setVisible(true);
    }

    private void loadCars(){

        model.setRowCount(0);

        try(Connection con=DBConnection.getConnection()){

            String query="SELECT * FROM cars WHERE available=TRUE";

            ResultSet rs=con.createStatement().executeQuery(query);

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("car_name"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("engine_type"),
                        rs.getString("transmission"),
                        rs.getDouble("price_per_day")
                });

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}