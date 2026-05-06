package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * StepIndicatorPanel
 * Displays a horizontal bar at the top of the window showing all five wizard steps.
 * - Active step is highlighted in blue with bold text.
 * - Completed steps are shown in green with a ✓ checkmark.
 * - Future steps remain in the default grey style.
 */
public class StepIndicatorPanel extends JPanel {

    private final JLabel[] stepLabels;
    private final String[] stepNames = {"Profile", "Define", "Plan", "Collect", "Analyse"};

    // Colour constants for the three indicator states
    private static final Color COLOR_DONE    = new Color(210, 235, 210); // green tint
    private static final Color COLOR_ACTIVE  = new Color(190, 215, 245); // blue tint
    private static final Color COLOR_PENDING = new Color(230, 230, 230); // grey

    public StepIndicatorPanel() {
        setLayout(new GridLayout(1, stepNames.length, 8, 8));
        stepLabels = new JLabel[stepNames.length];

        for (int i = 0; i < stepNames.length; i++) {
            stepLabels[i] = new JLabel((i + 1) + ". " + stepNames[i], JLabel.CENTER);
            stepLabels[i].setOpaque(true);
            stepLabels[i].setBackground(COLOR_PENDING);
            stepLabels[i].setForeground(Color.DARK_GRAY);
            stepLabels[i].setFont(stepLabels[i].getFont().deriveFont(Font.PLAIN));
            add(stepLabels[i]);
        }
    }

    /**
     * Updates the visual state of all step labels based on the current step index.
     *
     * @param currentStep zero-based index of the step the user is currently on
     */
    public void updateIndicator(int currentStep) {
        for (int i = 0; i < stepLabels.length; i++) {
            if (i < currentStep) {
                // Completed step: show checkmark and green background
                stepLabels[i].setText("✓ " + stepNames[i]);
                stepLabels[i].setBackground(COLOR_DONE);
                stepLabels[i].setFont(stepLabels[i].getFont().deriveFont(Font.BOLD));
            } else if (i == currentStep) {
                // Active step: numbered label with blue background and bold text
                stepLabels[i].setText((i + 1) + ". " + stepNames[i]);
                stepLabels[i].setBackground(COLOR_ACTIVE);
                stepLabels[i].setFont(stepLabels[i].getFont().deriveFont(Font.BOLD));
            } else {
                // Future step: numbered label with grey background and plain text
                stepLabels[i].setText((i + 1) + ". " + stepNames[i]);
                stepLabels[i].setBackground(COLOR_PENDING);
                stepLabels[i].setFont(stepLabels[i].getFont().deriveFont(Font.PLAIN));
            }
        }
    }
}
