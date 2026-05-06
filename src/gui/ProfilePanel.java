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
import java.awt.Font;
import java.awt.GridLayout;

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

        // Title label at the top of the panel
        JLabel title = new JLabel("Step 1 - Profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Form with three labeled input fields
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        usernameField = new JTextField();
        schoolField = new JTextField();
        sessionNameField = new JTextField();

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("School:"));
        formPanel.add(schoolField);
        formPanel.add(new JLabel("Session Name:"));
        formPanel.add(sessionNameField);

        // Center the form vertically
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(formPanel);
        wrapper.add(Box.createVerticalGlue());
        add(wrapper, BorderLayout.CENTER);
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
        String username = usernameField.getText().trim();
        String school = schoolField.getText().trim();
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
