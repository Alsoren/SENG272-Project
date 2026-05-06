package app;

import model.QualityType;
import model.Scenario;
import model.UserProfile;

/**
 * AppState
 * Holds the shared application state that is passed between all wizard step panels.
 * Acts as the single source of truth for the current session's selections.
 * Each panel reads from and writes to this object to transfer data between steps.
 */
public class AppState {

    private UserProfile userProfile;    // Set in Step 1 (Profile)
    private QualityType qualityType;    // Set in Step 2 (Define)
    private String mode;                // Set in Step 2 (Define)
    private Scenario selectedScenario;  // Set in Step 2 (Define), read in Steps 3–5

    public UserProfile getUserProfile()            { return userProfile; }
    public void setUserProfile(UserProfile p)      { this.userProfile = p; }

    public QualityType getQualityType()            { return qualityType; }
    public void setQualityType(QualityType qt)     { this.qualityType = qt; }

    public String getMode()                        { return mode; }
    public void setMode(String mode)               { this.mode = mode; }

    public Scenario getSelectedScenario()          { return selectedScenario; }
    public void setSelectedScenario(Scenario s)    { this.selectedScenario = s; }
}
