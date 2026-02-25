import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class RailwayLevelCrossing extends JPanel implements ActionListener {

    // --- Constants ---
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int FPS = 60;

    // Animation Speeds
    private static final int TRAIN_SPEED = 4; // Reduced speed (was 8)
    private static final double GATE_SPEED = 1.0; // Degrees per frame

    // Coordinate Anchors
    private static final int TRACK_Y = 350;     // Y position of the rails
    private static final int ROAD_X = 350;      // X position of the road center
    private static final int ROAD_WIDTH = 140;  // Slightly wider for better visibility

    // --- State Management ---
    private enum State {
        IDLE,           // Gates up, waiting
        WARNING,        // Lights blinking, gates start to lower
        CLOSING,        // Gates lowering
        TRAIN_PASSING,  // Train moving across screen
        OPENING         // Gates raising
    }

    private State currentState = State.IDLE;
    private final Timer timer;

    // --- Animation Variables ---
    private double gateAngle = 90; // 90 degrees = Up (Vertical), 0 degrees = Down (Horizontal)
    private int trainX = -800;     // Start further off-screen
    private boolean lightsOn = false;
    private int blinkCounter = 0;
    private int idleDelayCounter = 0;

    public RailwayLevelCrossing() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        // Timer for the animation loop
        timer = new Timer(1000 / FPS, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // --- Logic & State Machine ---

        switch (currentState) {
            case IDLE -> {
                lightsOn = false;
                trainX = -800; // Reset train position
                idleDelayCounter++;
                // Wait before starting sequence
                if (idleDelayCounter > 100) {
                    currentState = State.WARNING;
                    idleDelayCounter = 0;
                }
            }

            case WARNING -> {
                handleBlinking();
                idleDelayCounter++;
                if (idleDelayCounter > 120) { // 2 seconds warning
                    currentState = State.CLOSING;
                    idleDelayCounter = 0;
                }
            }

            case CLOSING -> {
                handleBlinking();
                gateAngle -= GATE_SPEED;
                if (gateAngle <= 0) {
                    gateAngle = 0;
                    currentState = State.TRAIN_PASSING;
                }
            }

            case TRAIN_PASSING -> {
                handleBlinking();
                trainX += TRAIN_SPEED;
                // Check if the train has fully left the screen
                if (trainX > WIDTH) {
                    currentState = State.OPENING;
                    trainX = WIDTH + 100; // Ensure it's fully off-screen
                }
            }

            case OPENING -> {
                lightsOn = false; // Lights stop
                gateAngle += GATE_SPEED;
                if (gateAngle >= 90) {
                    gateAngle = 90;
                    currentState = State.IDLE;
                }
            }

        }

        repaint();
    }

    private void handleBlinking() {
        blinkCounter++;
        // Toggle lights
        if (blinkCounter > 20) {
            lightsOn = !lightsOn;
            blinkCounter = 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw Environment
        drawScenery(g2d);

        // 2. Draw Road
        drawRoad(g2d);

        // 3. Draw Tracks
        drawTracks(g2d);

        // 4. Draw Train
        var oldTransform = g2d.getTransform();
        g2d.translate(trainX, TRACK_Y - 55);
        drawTrain(g2d);
        g2d.setTransform(oldTransform);

        // 5. Draw Traffic Signals and Gates
        drawSignalsAndGates(g2d);

        // 6. HUD
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2d.drawString("State: " + currentState, 10, 25);
        g2d.drawString("Gate Angle: " + (int) gateAngle + "°", 10, 45);
    }

    // --- Drawing Helpers ---

    private void drawScenery(Graphics2D g2d) {
        // Sky
        g2d.setColor(new Color(135, 206, 235));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Sun
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(WIDTH - 120, 60, 70, 70);

        // Grass
        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, TRACK_Y - 50, WIDTH, HEIGHT - (TRACK_Y - 50));
    }

    private void drawRoad(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(ROAD_X, TRACK_Y - 50, ROAD_WIDTH, HEIGHT);

        // Road dashed lines
        g2d.setColor(Color.WHITE);
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{15}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(ROAD_X + ROAD_WIDTH / 2, TRACK_Y - 50, ROAD_X + ROAD_WIDTH / 2, HEIGHT);
        g2d.setStroke(new BasicStroke(1));
    }

    private void drawTracks(Graphics2D g2d) {
        // Sleepers
        g2d.setColor(new Color(80, 40, 10));
        for (int i = 0; i < WIDTH; i += 30) {
            g2d.fillRect(i, TRACK_Y - 5, 12, 35);
        }

        // Rails
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, TRACK_Y + 2, WIDTH, 4);
        g2d.fillRect(0, TRACK_Y + 22, WIDTH, 4);
    }

    private void drawTrain(Graphics2D g2d) {
        // Engine
        g2d.setColor(Color.RED);
        g2d.fillRect(350, 0, 100, 60);
        g2d.fillRect(450, 20, 40, 40); // Cabin

        // Window
        g2d.setColor(Color.CYAN);
        g2d.fillRect(455, 25, 30, 20);

        // Roof Details
        g2d.setColor(Color.BLACK);
        g2d.fillRect(350, -5, 140, 5);
        g2d.fillRect(370, -20, 20, 20); // Chimney

        // Smoke
        g2d.setColor(new Color(220, 220, 220, 180));
        g2d.fillOval(355, -45, 20, 20);
        g2d.fillOval(335, -65, 30, 30);

        // Wheels
        g2d.setColor(Color.BLACK);
        g2d.fillOval(360, 50, 30, 30);
        g2d.fillOval(400, 50, 30, 30);
        g2d.fillOval(450, 50, 30, 30);

        // Wagons
        drawWagon(g2d, 200, Color.BLUE);
        drawWagon(g2d, 50, Color.GREEN);
        drawWagon(g2d, -100, Color.ORANGE);

        // Connectors
        g2d.setColor(Color.BLACK);
        g2d.fillRect(330, 40, 20, 10);
        g2d.fillRect(180, 40, 20, 10);
        g2d.fillRect(30, 40, 20, 10);
    }

    private void drawWagon(Graphics2D g2d, int xOffset, Color color) {
        g2d.setColor(color);
        g2d.fillRect(xOffset, 10, 130, 50);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(xOffset, 30, 130, 10);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(xOffset + 10, 50, 30, 30);
        g2d.fillOval(xOffset + 90, 50, 30, 30);
    }

    private void drawSignalsAndGates(Graphics2D g2d) {
        // Position the poles relative to the road
        var poleY = TRACK_Y + 50;
        var leftPoleX = ROAD_X - 15;
        var rightPoleX = ROAD_X + ROAD_WIDTH + 15;

        drawSignalPost(g2d, leftPoleX, poleY, true);   // Left side
        drawSignalPost(g2d, rightPoleX, poleY, false); // Right side
    }

    private void drawSignalPost(Graphics2D g2d, int x, int y, boolean isLeft) {
        // 1. Pole
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x - 5, y - 100, 10, 100);

        // 2. Light Box
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 15, y - 110, 30, 60);

        // 3. Lights (Logic for blinking)
        Color topLight = new Color(50, 0, 0);
        Color bottomLight = new Color(50, 0, 0);

        if (lightsOn) {
            topLight = Color.RED;
        } else if (currentState != State.IDLE && currentState != State.OPENING) {
            // In warning/closing states, if lightsOn is false, light up bottom (alternating)
            bottomLight = Color.RED;
        }

        g2d.setColor(topLight);
        g2d.fillOval(x - 10, y - 105, 20, 20);
        g2d.setColor(bottomLight);
        g2d.fillOval(x - 10, y - 80, 20, 20);

        // 4. Gate Pivot
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillOval(x - 6, y - 46, 12, 12);

        // 5. Gate Arm Rotation Logic
        AffineTransform old = g2d.getTransform();
        g2d.translate(x, y - 40); // Move to pivot center

        // ROTATION MATH:
        // We draw the gate simply as a horizontal line to the right (0 to length).
        // Angle 0 = Horizontal Right. Angle -90 = Vertical Up. Angle -180 = Horizontal Left.

        double rotationRad = getRotationRad(isLeft);

        g2d.rotate(rotationRad);

        // Draw the Arm (Standard length)
        drawGateArm(g2d);

        g2d.setTransform(old);
    }

    private double getRotationRad(boolean isLeft) {
        double rotationRad;
        if (isLeft) {
            // Left gate needs to point RIGHT when down.
            // Up = -90 deg. Down = 0 deg.
            // gateAngle goes 90 -> 0.
            // So rotation = Math.toRadians(-gateAngle).
            rotationRad = Math.toRadians(-gateAngle);
        } else {
            // Right gate needs to point LEFT when down.
            // Up = -90 deg (visually same direction). Down = -180 deg.
            // gateAngle goes 90 -> 0.
            // We need a formula that maps 90 -> -90 and 0 -> -180.
            // Formula: rotation = Math.toRadians(gateAngle - 180).
            rotationRad = Math.toRadians(gateAngle - 180);
        }
        return rotationRad;
    }

    private void drawGateArm(Graphics2D g2d) {
        // Arm body
        g2d.setColor(Color.RED);
        var armLen = (ROAD_WIDTH / 2) + 20; // Meet in the middle + slight overlap
        g2d.fillRect(0, -4, armLen, 8);

        // Stripes
        g2d.setColor(Color.WHITE);
        for (int i = 20; i < armLen; i += 30) {
            g2d.fillRect(i, -4, 15, 8);
        }
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("Railway Level Crossing Control System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new RailwayLevelCrossing());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}