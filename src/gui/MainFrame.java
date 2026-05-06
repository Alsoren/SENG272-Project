package gui;

import app.AppState;
import service.ScenarioRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

/**
 * MainFrame - the main application window.
 * Uses CardLayout to switch between the five wizard step panels.
 * Manages Back/Next navigation and delegates step validation to each panel.
 */
public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final WizardPanel[] wizardPanels;
    private final StepIndicatorPanel stepIndicatorPanel;
    private final JButton backButton;
    private final JButton nextButton;
    private int currentStep;

    public MainFrame() {
        super("ISO 15939 Measurement Process Simulator - V3");

        // Shared state and data repository passed to all panels
        AppState appState = new AppState();
        ScenarioRepository scenarioRepository = new ScenarioRepository();

        // Instantiate all five wizard step panels
        wizardPanels = new WizardPanel[]{
                new ProfilePanel(appState, scenarioRepository),
                new DefinePanel(appState, scenarioRepository),
                new PlanPanel(appState, scenarioRepository),
                new CollectPanel(appState, scenarioRepository),
                new AnalysePanel(appState, scenarioRepository)
        };

        // Register each panel in the CardLayout container
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        for (int i = 0; i < wizardPanels.length; i++) {
            cardPanel.add(wizardPanels[i], "step" + i);
        }

        // Step indicator bar displayed at the top of the window
        stepIndicatorPanel = new StepIndicatorPanel();
        stepIndicatorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Navigation buttons displayed at the bottom
        backButton = new JButton("Back");
        nextButton = new JButton("Next");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);

        setLayout(new BorderLayout());
        add(stepIndicatorPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> previousStep());
        nextButton.addActionListener(e -> nextStep());

        // Start at the first step
        currentStep = 0;
        updateStepView();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Validates the current step and advances to the next one if valid.
     * On the last step, shows a completion dialog instead of advancing.
     */
    private void nextStep() {
        if (!wizardPanels[currentStep].validateStep()) {
            return;
        }

        if (currentStep < wizardPanels.length - 1) {
            currentStep++;
            updateStepView();
        } else {
            // Last step: show a session-complete message
            JOptionPane.showMessageDialog(
                    this,
                    "Measurement session completed successfully!\nThank you for using the ISO 15939 Simulator.",
                    "Session Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Moves back to the previous step without validation.
     */
    private void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            updateStepView();
        }
    }

    /**
     * Refreshes the visible panel, step indicator, and button states.
     */
    private void updateStepView() {
        wizardPanels[currentStep].onEnterStep();
        cardLayout.show(cardPanel, "step" + currentStep);
        stepIndicatorPanel.updateIndicator(currentStep);
        backButton.setEnabled(currentStep > 0);
        nextButton.setText(currentStep == wizardPanels.length - 1 ? "Finish" : "Next");
    }
}
