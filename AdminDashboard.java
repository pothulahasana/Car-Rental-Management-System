import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    AdminDashboard() {

        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // ✅ FULL SCREEN
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // TITLE
        JLabel title = new JLabel("CAR RENTAL ADMIN PANEL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // CENTER PANEL
        JPanel centerPanel = new JPanel(new GridBagLayout()); // ✅ CENTER ALIGN
        centerPanel.setBackground(Color.WHITE);

        JPanel grid = new JPanel(new GridLayout(3, 3, 40, 40));
        grid.setBackground(Color.WHITE);

        // ADD BOXES
        grid.add(createBox("Add Car", "images/add.png"));
        grid.add(createBox("View Cars", "images/view.png"));
        grid.add(createBox("Update Car", "images/update.png"));

        grid.add(createBox("Delete Car", "images/delete.png"));
        grid.add(createBox("Users", "images/users.png"));
        grid.add(createBox("Bookings", "images/bookings.png"));

        grid.add(createBox("Payments", "images/payments.png"));
        grid.add(new JLabel()); // empty space
        grid.add(createBox("Logout", "images/logout.png"));

        centerPanel.add(grid);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // 🔥 UPDATED BOX (NO x,y)
    JPanel createBox(String name, String path) {

        int size = 140;

        RoundedPanel box = new RoundedPanel(20);
        box.setLayout(new BorderLayout());
        box.setPreferredSize(new Dimension(size, size));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        box.setBackground(Color.WHITE);

        // IMAGE LOAD
        ImageIcon icon = null;
        try {
            java.net.URL url = getClass().getResource("/" + path);
            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                System.out.println("Image not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(70, 60, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
        }

        JLabel text = new JLabel(name, SwingConstants.CENTER);
        text.setFont(new Font("Arial", Font.BOLD, 13));

        box.add(iconLabel, BorderLayout.CENTER);
        box.add(text, BorderLayout.SOUTH);

        // CLICK EVENTS
        box.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {

                switch(name) {

                    case "Add Car": new AddCar(); break;
                    case "View Cars": new ViewCars(); break;
                    case "Update Car": new UpdateCar(); break;
                    case "Delete Car": new DeleteCar(); break;
                    case "Users": new Users(); break;
                    case "Bookings": new ViewBookings(); break;
                    case "Payments": new Payments(); break;

                    case "Logout":
                        dispose();
                        new Login();
                        break;
                }
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                box.setBackground(new Color(230, 240, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                box.setBackground(Color.WHITE);
            }
        });

        return box;
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}