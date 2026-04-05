import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
        setPreferredSize(new Dimension(200, 200)); // PERFECT SQUARE
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

        g2.dispose();
    }
}