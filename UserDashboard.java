import javax.swing.*;
import java.awt.*;

public class UserDashboard extends JFrame {

    private int userId;

    public UserDashboard(int userId) {

        this.userId = userId;

        setTitle("User Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // ✅ FULL SCREEN
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // TITLE
        JLabel title = new JLabel("USER DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // CENTER PANEL (THIS FIXES EVERYTHING)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridBagLayout()); // ✅ CENTER ALIGN

        JPanel grid = new JPanel();
        grid.setBackground(Color.WHITE);
        grid.setLayout(new GridLayout(3, 2, 40, 40)); // rows, cols, gap

        // ADD BOXES
        grid.add(createBox("View Cars", "images/view.png"));
        grid.add(createBox("Book Car", "images/add.png"));

        grid.add(createBox("My Bookings", "images/bookings.png"));
        grid.add(createBox("My Payments", "images/payments.png"));

        grid.add(new JLabel()); // empty space
        grid.add(createBox("Logout", "images/logout.png")); // ✅ properly aligned

        centerPanel.add(grid);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // 🔥 UPDATED createBox (NO x,y needed)
    JPanel createBox(String name, String path) {

        int size = 140;

        RoundedPanel box = new RoundedPanel(20);
        box.setLayout(new BorderLayout());
        box.setPreferredSize(new Dimension(size, size));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        box.setBackground(Color.WHITE);

        // IMAGE
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

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                switch(name) {

                    case "View Cars":
                        new UserViewCars();
                        break;

                    case "Book Car":
                        new UserBooking(userId);
                        break;

                    case "My Bookings":
                        new UserMyBookings(userId);
                        break;

                    case "My Payments":
                        new UserPaymentHistory(userId);
                        break;

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
}