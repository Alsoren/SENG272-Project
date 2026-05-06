package model;

/**
 * UserProfile
 * Stores the profile information entered in Step 1.
 * Fields are private (encapsulation) and accessed through getters and setters.
 */
public class UserProfile {

    private String username;
    private String school;
    private String sessionName;

    public UserProfile(String username, String school, String sessionName) {
        this.username = username;
        this.school = school;
        this.sessionName = sessionName;
    }

    public String getUsername()                  { return username; }
    public void setUsername(String username)     { this.username = username; }

    public String getSchool()                    { return school; }
    public void setSchool(String school)         { this.school = school; }

    public String getSessionName()               { return sessionName; }
    public void setSessionName(String name)      { this.sessionName = name; }
}
