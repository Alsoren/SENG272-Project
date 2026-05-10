package gui;

import app.AppState;
import model.UserProfile;
import service.ScenarioRepository;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Step 1 - Profile
 * Collects username, school, and session name from the user.
 * Validates that all fields are filled before allowing navigation to the next step.
 * Restores previously entered data when the user navigates back.
 */
public class ProfilePanel extends WizardPanel {

    private final JTextField usernameField;
    private final JTextField schoolField;
    private final JTextField sessionNameField;

    public ProfilePanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);

        // Title at the top
        JLabel title = new JLabel("Step 1 - Profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Form panel using GridBagLayout for controlled field sizing
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Fixed-width text fields so they don't stretch across the entire window
        usernameField    = fixedField();
        schoolField      = fixedField();
        sessionNameField = fixedField();

        // Row 0: Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        // Row 1: School
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("School:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(schoolField, gbc);

        // Row 2: Session Name
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Session Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(sessionNameField, gbc);

        // Outer container limits the form width and centres it
        JPanel outerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.insets = new Insets(0, 120, 0, 120);
        outerGbc.fill = GridBagConstraints.HORIZONTAL;
        outerGbc.weightx = 1.0;
        outerPanel.add(formPanel, outerGbc);

        // Centre vertically using BoxLayout glue
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(outerPanel);
        wrapper.add(Box.createVerticalGlue());
        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Creates a JTextField with a sensible preferred height so the boxes
     * are not disproportionately tall.
     */
    private JTextField fixedField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(260, 28));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return field;
    }

    /**
     * Restores previously entered profile data when the user navigates back to this step.
     */
    @Override
    public void onEnterStep() {
        UserProfile profile = appState.getUserProfile();
        if (profile == null) {
            return;
        }
        usernameField.setText(profile.getUsername());
        schoolField.setText(profile.getSchool());
        sessionNameField.setText(profile.getSessionName());
    }

    /**
     * Validates that all three fields are non-empty before proceeding.
     * Saves valid profile data into AppState.
     */
    @Override
    public boolean validateStep() {
        String username    = usernameField.getText().trim();
        String school      = schoolField.getText().trim();
        String sessionName = sessionNameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username to continue.");
            return false;
        }
        if (school.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your school to continue.");
            return false;
        }
        if (sessionName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a session name to continue.");
            return false;
        }

        // Persist the profile in shared application state
        appState.setUserProfile(new UserProfile(username, school, sessionName));
        return true;
    }
}
