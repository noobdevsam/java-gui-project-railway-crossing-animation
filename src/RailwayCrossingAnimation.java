import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

void main() {
    var frame = new JFrame("Railway Level Crossing Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new CrossingAnimation());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

static class CrossingAnimation extends JPanel implements ActionListener {

    private Timer timer;

    // Train properties
    private int trainX = -300;
    private final int trainY = 250;
    private final int trainSpeed = 3;

    // Gate properties
    private double gateAngle = 0; // 0 = open, 90 = closed
    private boolean gateClosing = false;
    private boolean gateOpening = false;

    // Warning lights
    private boolean lightOn = false;
    private int lightTimer = 0;

    // Crossing trigger zone
    private final int crossingX = 400;

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
    }

    private void drawTrain(Graphics2D graphics2D) {
    }

    private void drawRailway(Graphics2D graphics2D) {
    }

    private void drawRoad(Graphics2D graphics2D) {
    }

}

