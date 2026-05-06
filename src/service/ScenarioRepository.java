package service;

import model.Dimension;
import model.Direction;
import model.Metric;
import model.QualityType;
import model.Scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ScenarioRepository
 * Central repository that stores and provides all predefined scenario data.
 * All scenario definitions are hard-coded here as required by the assignment —
 * no file reading or database access is used.
 * GUI classes must not contain scenario data; they must obtain it through this class.
 *
 * Modes available: Education, Health
 * Each mode contains at least two scenarios, each with multiple dimensions and metrics.
 */
public class ScenarioRepository {

    // Map from mode name to the list of scenarios for that mode
    private final HashMap<String, ArrayList<Scenario>> scenariosByMode;

    public ScenarioRepository() {
        scenariosByMode = new HashMap<>();
        loadScenarios();
    }

    /** Returns all available mode names. */
    public List<String> getModes() {
        return new ArrayList<>(scenariosByMode.keySet());
    }

    /**
     * Returns the list of scenarios for the given mode.
     *
     * @param mode the mode name (e.g. "Education", "Health")
     * @return list of scenarios, or an empty list if the mode is not found
     */
    public List<Scenario> getScenariosByMode(String mode) {
        return scenariosByMode.getOrDefault(mode, new ArrayList<>());
    }

    /** Builds and registers all scenarios into the mode map. */
    private void loadScenarios() {
        // Education mode — two scenarios
        ArrayList<Scenario> educationScenarios = new ArrayList<>();
        educationScenarios.add(createEducationTeamAlpha());
        educationScenarios.add(createEducationTeamBeta());
        scenariosByMode.put("Education", educationScenarios);

        // Health mode — two scenarios
        ArrayList<Scenario> healthScenarios = new ArrayList<>();
        healthScenarios.add(createHospitalSystem());
        healthScenarios.add(createClinicSystem());
        scenariosByMode.put("Health", healthScenarios);
    }

    // ─────────────────────────────────────────────────────────────────
    // Education scenarios
    // ─────────────────────────────────────────────────────────────────

    /**
     * Scenario C — Team Alpha (Education / Product Quality)
     * Five dimensions with two metrics each, following the assignment's sample dataset.
     */
    private Scenario createEducationTeamAlpha() {
        Scenario scenario = new Scenario("Scenario C - Team Alpha", "Education", QualityType.PRODUCT_QUALITY);

        Dimension usability = new Dimension("Usability", 25);
        usability.addMetric(new Metric("SUS score",        50, Direction.HIGHER_IS_BETTER, 0,  100, "points", 89));
        usability.addMetric(new Metric("Onboarding time",  50, Direction.LOWER_IS_BETTER,  0,  60,  "min",    5));

        Dimension performance = new Dimension("Performance Efficiency", 20);
        performance.addMetric(new Metric("Video start time",  50, Direction.LOWER_IS_BETTER,  0,  15,  "sec",   3));
        performance.addMetric(new Metric("Concurrent exams",  50, Direction.HIGHER_IS_BETTER, 0,  600, "users", 480));

        Dimension accessibility = new Dimension("Accessibility", 20);
        accessibility.addMetric(new Metric("WCAG compliance",     50, Direction.HIGHER_IS_BETTER, 0, 100, "%", 92));
        accessibility.addMetric(new Metric("Screen reader score",  50, Direction.HIGHER_IS_BETTER, 0, 100, "%", 86));

        Dimension reliability = new Dimension("Reliability", 20);
        reliability.addMetric(new Metric("Uptime", 50, Direction.HIGHER_IS_BETTER, 95, 100, "%",   99));
        reliability.addMetric(new Metric("MTTR",   50, Direction.LOWER_IS_BETTER,  0,  120, "min", 24));

        Dimension functionality = new Dimension("Functional Suitability", 15);
        functionality.addMetric(new Metric("Feature completion",     50, Direction.HIGHER_IS_BETTER, 0, 100, "%", 91));
        functionality.addMetric(new Metric("Assignment submit rate",  50, Direction.HIGHER_IS_BETTER, 0, 100, "%", 88));

        scenario.addDimension(usability);
        scenario.addDimension(performance);
        scenario.addDimension(accessibility);
        scenario.addDimension(reliability);
        scenario.addDimension(functionality);
        return scenario;
    }

    /**
     * Scenario D — Team Beta (Education / Product Quality)
     * A lighter scenario used to demonstrate a different performance profile.
     */
    private Scenario createEducationTeamBeta() {
        Scenario scenario = new Scenario("Scenario D - Team Beta", "Education", QualityType.PRODUCT_QUALITY);

        Dimension usability = new Dimension("Usability", 30);
        usability.addMetric(new Metric("SUS score",       50, Direction.HIGHER_IS_BETTER, 0,  100, "points", 74));
        usability.addMetric(new Metric("Onboarding time", 50, Direction.LOWER_IS_BETTER,  0,  60,  "min",    18));

        Dimension reliability = new Dimension("Reliability", 25);
        reliability.addMetric(new Metric("Uptime", 50, Direction.HIGHER_IS_BETTER, 95, 100, "%",   97.6));
        reliability.addMetric(new Metric("MTTR",   50, Direction.LOWER_IS_BETTER,  0,  120, "min", 42));

        Dimension performance = new Dimension("Performance Efficiency", 20);
        performance.addMetric(new Metric("Video start time", 50, Direction.LOWER_IS_BETTER,  0, 15,  "sec",   7));
        performance.addMetric(new Metric("Concurrent exams", 50, Direction.HIGHER_IS_BETTER, 0, 600, "users", 300));

        scenario.addDimension(usability);
        scenario.addDimension(reliability);
        scenario.addDimension(performance);
        return scenario;
    }

    // ─────────────────────────────────────────────────────────────────
    // Health scenarios
    // ─────────────────────────────────────────────────────────────────

    /**
     * Scenario A — Hospital System (Health / Process Quality)
     */
    private Scenario createHospitalSystem() {
        Scenario scenario = new Scenario("Scenario A - Hospital System", "Health", QualityType.PROCESS_QUALITY);

        Dimension service = new Dimension("Service Continuity", 35);
        service.addMetric(new Metric("System availability",    50, Direction.HIGHER_IS_BETTER, 90, 100, "%",   98));
        service.addMetric(new Metric("Incident response time", 50, Direction.LOWER_IS_BETTER,  0,  120, "min", 30));

        Dimension safety = new Dimension("Data Safety", 30);
        safety.addMetric(new Metric("Backup success rate", 50, Direction.HIGHER_IS_BETTER, 0,  100, "%",     94));
        safety.addMetric(new Metric("Security incidents",  50, Direction.LOWER_IS_BETTER,  0,  20,  "count", 2));

        Dimension usability = new Dimension("Usability", 20);
        usability.addMetric(new Metric("SUS score",             50, Direction.HIGHER_IS_BETTER, 0, 100, "points", 80));
        usability.addMetric(new Metric("Task completion rate",  50, Direction.HIGHER_IS_BETTER, 0, 100, "%",      91));

        Dimension performance = new Dimension("Performance Efficiency", 15);
        performance.addMetric(new Metric("Response time",    50, Direction.LOWER_IS_BETTER,  0, 5,    "sec",   0.8));
        performance.addMetric(new Metric("Concurrent users", 50, Direction.HIGHER_IS_BETTER, 0, 1000, "users", 750));

        scenario.addDimension(service);
        scenario.addDimension(safety);
        scenario.addDimension(usability);
        scenario.addDimension(performance);
        return scenario;
    }

    /**
     * Scenario B — Clinic System (Health / Process Quality)
     */
    private Scenario createClinicSystem() {
        Scenario scenario = new Scenario("Scenario B - Clinic System", "Health", QualityType.PROCESS_QUALITY);

        Dimension efficiency = new Dimension("Process Efficiency", 40);
        efficiency.addMetric(new Metric("Appointment delay",      50, Direction.LOWER_IS_BETTER,  0, 60,  "min",    12));
        efficiency.addMetric(new Metric("Daily completed visits",  50, Direction.HIGHER_IS_BETTER, 0, 200, "visits", 140));

        Dimension satisfaction = new Dimension("User Satisfaction", 30);
        satisfaction.addMetric(new Metric("Patient satisfaction", 50, Direction.HIGHER_IS_BETTER, 0, 100, "%",     83));
        satisfaction.addMetric(new Metric("Complaint count",      50, Direction.LOWER_IS_BETTER,  0, 30,  "count", 6));

        Dimension reliability = new Dimension("Reliability", 30);
        reliability.addMetric(new Metric("System uptime", 50, Direction.HIGHER_IS_BETTER, 99, 100, "%",   99.2));
        reliability.addMetric(new Metric("MTTR",          50, Direction.LOWER_IS_BETTER,  0,  60,  "min", 20));

        scenario.addDimension(efficiency);
        scenario.addDimension(satisfaction);
        scenario.addDimension(reliability);
        return scenario;
    }
}
