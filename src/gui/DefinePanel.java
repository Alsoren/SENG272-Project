package gui;

import app.AppState;
import model.QualityType;
import model.Scenario;
import service.ScenarioRepository;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Step 2 - Define Quality Dimensions
 * The user selects exactly one quality type, one measurement mode, and one scenario.
 * JRadioButton with ButtonGroup enforces the single-selection constraint for each group.
 * Restores all selections when the user navigates back.
 */
public class DefinePanel extends WizardPanel {

    private final JRadioButton productQualityButton;
    private final JRadioButton processQualityButton;
    private final ArrayList<JRadioButton> modeButtons;
    private final JComboBox<Scenario> scenarioComboBox;

    public DefinePanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);
        modeButtons = new ArrayList<>();

        // Title label
        JLabel title = new JLabel("Step 2 - Define Quality Dimensions");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 2a: Quality Type - ButtonGroup ensures only one can be selected at a time
        productQualityButton = new JRadioButton(QualityType.PRODUCT_QUALITY.getDisplayName());
        processQualityButton = new JRadioButton(QualityType.PROCESS_QUALITY.getDisplayName());
        ButtonGroup qualityTypeGroup = new ButtonGroup();
        qualityTypeGroup.add(productQualityButton);
        qualityTypeGroup.add(processQualityButton);

        JPanel qualityPanel = new JPanel();
        qualityPanel.add(productQualityButton);
        qualityPanel.add(processQualityButton);

        // 2b: Mode - ButtonGroup ensures only one mode is active at a time
        JPanel modePanel = new JPanel();
        ButtonGroup modeGroup = new ButtonGroup();
        for (String mode : scenarioRepository.getModes()) {
            JRadioButton modeButton = new JRadioButton(mode);
            // Refresh scenario list whenever a different mode is selected
            modeButton.addActionListener(e -> refreshScenarios());
            modeGroup.add(modeButton);
            modePanel.add(modeButton);
            modeButtons.add(modeButton);
        }

        // 2c: Scenario - JComboBox naturally enforces single selection
        scenarioComboBox = new JComboBox<>();
        refreshScenarios();

        // Layout rows: Quality Type, Mode, Scenario
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Quality Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(qualityPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Mode:"), gbc);
        gbc.gridx = 1;
        formPanel.add(modePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Scenario:"), gbc);
        gbc.gridx = 1;
        formPanel.add(scenarioComboBox, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Repopulates the scenario combo box based on the currently selected mode.
     */
    private void refreshScenarios() {
        scenarioComboBox.removeAllItems();
        String selectedMode = getSelectedMode();
        if (selectedMode == null) {
            return;
        }
        List<Scenario> scenarios = scenarioRepository.getScenariosByMode(selectedMode);
        for (Scenario scenario : scenarios) {
            scenarioComboBox.addItem(scenario);
        }
    }

    /**
     * Returns the label of the currently selected mode radio button, or null if none selected.
     */
    private String getSelectedMode() {
        for (JRadioButton button : modeButtons) {
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    /**
     * Restores quality type, mode, and scenario selections when the user navigates back.
     */
    @Override
    public void onEnterStep() {
        // Restore quality type selection
        QualityType savedQualityType = appState.getQualityType();
        if (savedQualityType == QualityType.PRODUCT_QUALITY) {
            productQualityButton.setSelected(true);
        } else if (savedQualityType == QualityType.PROCESS_QUALITY) {
            processQualityButton.setSelected(true);
        }

        // Restore mode selection and reload scenario list
        String savedMode = appState.getMode();
        if (savedMode != null) {
            for (JRadioButton button : modeButtons) {
                if (button.getText().equals(savedMode)) {
                    button.setSelected(true);
                    break;
                }
            }
            refreshScenarios();
        }

        // Restore scenario selection in the combo box
        Scenario savedScenario = appState.getSelectedScenario();
        if (savedScenario != null) {
            for (int i = 0; i < scenarioComboBox.getItemCount(); i++) {
                Scenario candidate = scenarioComboBox.getItemAt(i);
                if (candidate.getName().equals(savedScenario.getName())) {
                    scenarioComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Validates that quality type, mode, and scenario are all selected.
     * Saves the selections into AppState before proceeding.
     */
    @Override
    public boolean validateStep() {
        if (!productQualityButton.isSelected() && !processQualityButton.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select one quality type to continue.");
            return false;
        }

        String selectedMode = getSelectedMode();
        Scenario selectedScenario = (Scenario) scenarioComboBox.getSelectedItem();

        if (selectedMode == null) {
            JOptionPane.showMessageDialog(this, "Please select one measurement mode to continue.");
            return false;
        }
        if (selectedScenario == null) {
            JOptionPane.showMessageDialog(this, "Please select one scenario to continue.");
            return false;
        }

        // Determine and persist quality type
        QualityType selectedQualityType = productQualityButton.isSelected()
                ? QualityType.PRODUCT_QUALITY
                : QualityType.PROCESS_QUALITY;

        appState.setQualityType(selectedQualityType);
        appState.setMode(selectedMode);
        appState.setSelectedScenario(selectedScenario);
        return true;
    }
}
