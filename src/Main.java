import javax.swing.SwingUtilities;
import gui.MainFrame;

/**
 * Main
 * Entry point for the ISO 15939 Measurement Process Simulator.
 * Launches the Swing GUI on the Event Dispatch Thread as required by Swing threading rules.
 *
 * Compilation : javac -d out -sourcepath src src/Main.java
 * Run         : java -cp out Main
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
