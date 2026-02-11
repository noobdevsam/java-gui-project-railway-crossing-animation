import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

void main() {
    var frame = new JFrame("Railway Level Crossing Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new CrossingAnimation());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

static class CrossingAnimation extends JPanel implements ActionListener {

    private final int trainY = 250;
    private final int trainSpeed = 3;
    // Crossing trigger zone
    private final int crossingX = 400;
    private Timer timer;
    // Train properties
    private int trainX = -300;
    // Gate properties
    private double gateAngle = 0; // 0 = open, 90 = closed
    private boolean gateClosing = false;
    private boolean gateOpening = false;
    // Warning lights
    private boolean lightOn = false;
    private int lightTimer = 0;

    public CrossingAnimation() {
        setPreferredSize(new Dimension(900, 500));
        setBackground(Color.WHITE);
        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
        var graphics2D = (Graphics2D) g;

        // Smooth animation
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Draw train
        drawBackground(graphics2D);
        drawRoad(graphics2D);
        drawRailway(graphics2D);
        drawTrain(graphics2D);
        drawGate(graphics2D);
        drawWarningLights(graphics2D);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // Private helper methods to draw
    private void drawBackground(Graphics2D graphics2D) {
        var skyGradient = new GradientPaint(
                0, 0, new Color(135, 206, 235),
                0, getHeight(), new Color(25, 25, 112)
        );
        graphics2D.setPaint(skyGradient);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawWarningLights(Graphics2D graphics2D) {
    }

    private void drawGate(Graphics2D graphics2D) {
        var baseX = crossingX;
        var baseY = 300;

        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(
                baseX - 10,
                baseY - 80,
                20,
                80
        );

        var oldAffineTransform = graphics2D.getTransform();
        graphics2D.rotate(
                Math.toRadians(gateAngle),
                baseX,
                baseY - 80
        );

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(
                baseX,
                baseY - 90,
                120,
                10
        );

        graphics2D.setColor(Color.RED);
        for (var i = 0; i < 6; i++) {
            graphics2D.fillRect(
                    baseX + i * 20,
                    baseY - 90,
                    10,10
            );
        }

        graphics2D.setTransform(oldAffineTransform);
    }

    private void drawTrain(Graphics2D graphics2D) {
        graphics2D.setColor(
                new Color(178, 34, 34)
        );
        graphics2D.fillRect(trainX, trainY, 250, 50);

        graphics2D.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 4; i++) {
            graphics2D.fillRect(
                    trainX + 20 + i * 50,
                    trainY + 10,
                    30,
                    20
            );
        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.fillOval(
                trainX + 30,
                trainY + 45,
                30,
                30
        );
        graphics2D.fillOval(
                trainX + 170,
                trainY + 45,
                30,
                30
        );
    }

    private void drawRailway(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 270, getWidth(), 10);
        graphics2D.fillRect(0, 290, getWidth(), 10);
    }

    private void drawRoad(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(50, 50, 50));
        graphics2D.fillRect(0,300, getWidth(), 80);

        graphics2D.setColor(Color.YELLOW);
        for (var i = 0; i < getWidth(); i += 40) {
            graphics2D.fillRect(i, 335, 20, 4);
        }
    }

}

