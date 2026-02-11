import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

void main() {
    var frame = new JFrame("Railway Level Crossing Animation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

class RailwayCrossingAnimation extends JFrame implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

