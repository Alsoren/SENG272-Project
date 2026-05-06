package gui;

import app.AppState;
import service.ScenarioRepository;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * WizardPanel - abstract base class for all five wizard step panels.
 *
 * Each concrete step panel must override:
 *   - validateStep()  : returns true when the panel's inputs are complete and valid.
 *   - onEnterStep()   : refreshes the panel's content from AppState each time it is shown.
 *
 * Encapsulates the shared AppState and ScenarioRepository references so subclasses
 * do not need to receive them through any other mechanism.
 */
public abstract class WizardPanel extends JPanel {

    /** Shared application state passed between wizard steps. */
    protected final AppState appState;

    /** Central repository for all predefined scenario data. */
    protected final ScenarioRepository scenarioRepository;

    public WizardPanel(AppState appState, ScenarioRepository scenarioRepository) {
        this.appState = appState;
        this.scenarioRepository = scenarioRepository;
        setLayout(new BorderLayout(10, 10));
    }

    /**
     * Called by MainFrame before advancing to the next step.
     * Implementations should validate user input and, if valid, persist
     * the relevant data into AppState.
     *
     * @return true if the step is complete and navigation may proceed
     */
    public abstract boolean validateStep();

    /**
     * Called by MainFrame every time this panel becomes the active step.
     * Implementations should refresh displayed data from AppState so that
     * back-navigation always reflects the latest selections.
     * Default implementation is a no-op for read-only steps.
     */
    public void onEnterStep() {
        // Default: no refresh needed (e.g. read-only steps override this as needed)
    }
}
