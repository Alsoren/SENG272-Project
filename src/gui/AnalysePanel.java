package gui;

import app.AppState;
import model.Dimension;
import model.Scenario;
import service.GapAnalysisResult;
import service.GapAnalyzer;
import service.ScenarioRepository;
import service.ScoreCalculator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Step 5 - Analyse
 * Displays the measurement results in three parts:
 *   5a. Dimension-based weighted average scores with JProgressBar visualisation.
 *   5b. Radar chart (bonus) drawn with Graphics2D.
 *   5c. Gap analysis identifying the lowest-scoring dimension.
 */
public class AnalysePanel extends WizardPanel {

    private final JPanel scorePanel;
    private final JTextArea gapTextArea;
    private final RadarChartPanel radarChartPanel;
    private final ScoreCalculator scoreCalculator;
    private final GapAnalyzer gapAnalyzer;

    public AnalysePanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);
        this.scoreCalculator = new ScoreCalculator();
        this.gapAnalyzer = new GapAnalyzer();

        // Title
        JLabel title = new JLabel("Step 5 - Analyse");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Centre area: dimension score bars (left) + radar chart (right)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 5a: Dimension score progress bars
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("5a. Dimension-Based Weighted Averages"));

        // 5b: Radar chart (bonus)
        radarChartPanel = new RadarChartPanel();
        radarChartPanel.setBorder(BorderFactory.createTitledBorder("5b. Radar Chart (Bonus)"));

        contentPanel.add(new JScrollPane(scorePanel));
        contentPanel.add(radarChartPanel);

        // 5c: Gap analysis text area
        gapTextArea = new JTextArea();
        gapTextArea.setEditable(false);
        gapTextArea.setLineWrap(true);
        gapTextArea.setWrapStyleWord(true);
        gapTextArea.setRows(6);
        gapTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane gapScrollPane = new JScrollPane(gapTextArea);
        gapScrollPane.setBorder(BorderFactory.createTitledBorder("5c. Gap Analysis"));

        add(contentPanel, BorderLayout.CENTER);
        add(gapScrollPane, BorderLayout.SOUTH);
    }

    /**
     * Calculates all dimension scores and renders the results when this step is entered.
     */
    @Override
    public void onEnterStep() {
        scorePanel.removeAll();
        Scenario scenario = appState.getSelectedScenario();

        if (scenario == null) {
            gapTextArea.setText("No scenario selected. Please complete the Define step first.");
            radarChartPanel.setScenario(null);
            scorePanel.revalidate();
            scorePanel.repaint();
            return;
        }

        // Calculate and display each dimension score
        for (Dimension dimension : scenario.getDimensions()) {
            double score = scoreCalculator.calculateDimensionScore(dimension);
            addDimensionScoreRow(dimension, score);
        }

        // Run gap analysis and display results
        GapAnalysisResult result = gapAnalyzer.analyze(scenario);
        showGapAnalysis(result);

        // Update the radar chart with the fully scored scenario
        radarChartPanel.setScenario(scenario);

        scorePanel.revalidate();
        scorePanel.repaint();
    }

    /**
     * Adds a labelled JProgressBar row for the given dimension and score.
     *
     * @param dimension the quality dimension
     * @param score     the calculated weighted average score (1.0–5.0)
     */
    private void addDimensionScoreRow(Dimension dimension, double score) {
        JLabel label = new JLabel(dimension.getName() + " - " + String.format("%.2f / 5.00", score));
        label.setFont(label.getFont().deriveFont(Font.BOLD));

        // Progress bar maps the 1–5 score to a 0–100% range
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) Math.round((score / 5.0) * 100));
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.0f%%", (score / 5.0) * 100));

        JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        rowPanel.add(label, BorderLayout.NORTH);
        rowPanel.add(progressBar, BorderLayout.CENTER);

        scorePanel.add(rowPanel);
        scorePanel.add(Box.createVerticalStrut(8));
    }

    /**
     * Fills the gap analysis text area with the lowest-dimension result.
     *
     * @param result the GapAnalysisResult produced by GapAnalyzer
     */
    private void showGapAnalysis(GapAnalysisResult result) {
        if (result == null || result.getLowestDimension() == null) {
            gapTextArea.setText("Gap analysis could not be calculated because no dimension data exists.");
            return;
        }

        gapTextArea.setText(
                "Lowest Dimension : " + result.getLowestDimension().getName() + "\n" +
                "Score            : " + String.format("%.2f", result.getScore()) + " / 5.00\n" +
                "Gap Value        : " + String.format("%.2f", result.getGap()) + "\n" +
                "Quality Level    : " + result.getQualityLevel() + "\n" +
                "Improvement      : " + result.getImprovementMessage()
        );
    }

    /**
     * No validation required for the final read-only analysis step.
     */
    @Override
    public boolean validateStep() {
        return true;
    }
}
