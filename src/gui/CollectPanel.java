package gui;

import app.AppState;
import model.Dimension;
import model.Metric;
import model.Scenario;
import service.ScenarioRepository;
import service.ScoreCalculator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Step 4 - Collect Data
 * Displays predefined raw metric values alongside automatically calculated scores.
 * Scores are computed by ScoreCalculator using the ISO 15939 formula and
 * clamped to the range 1.0–5.0, then rounded to the nearest 0.5.
 */
public class CollectPanel extends WizardPanel {

    private final DefaultTableModel tableModel;
    private final ScoreCalculator scoreCalculator;

    public CollectPanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);
        this.scoreCalculator = new ScoreCalculator();

        JLabel title = new JLabel("Step 4 - Collect Data");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Table columns follow the assignment specification
        String[] columns = {"Metric", "Direction", "Range", "Value", "Score (1–5)", "Coeff / Unit"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Scores are calculated automatically; no manual editing allowed
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Calculates metric scores and populates the table when the step becomes visible.
     */
    @Override
    public void onEnterStep() {
        tableModel.setRowCount(0);
        Scenario scenario = appState.getSelectedScenario();
        if (scenario == null) {
            return;
        }

        for (Dimension dimension : scenario.getDimensions()) {
            for (Metric metric : dimension.getMetrics()) {
                // calculateMetricScore also sets metric.score via metric.setScore()
                double score = scoreCalculator.calculateMetricScore(metric);
                tableModel.addRow(new Object[]{
                        metric.getName(),
                        metric.getDirection().getDisplayName(),
                        removeTrailingZero(metric.getMinValue()) + " - " + removeTrailingZero(metric.getMaxValue()),
                        removeTrailingZero(metric.getRawValue()),
                        String.format("%.1f", score),
                        metric.getCoefficient() + " / " + metric.getUnit()
                });
            }
        }
    }

    /**
     * No validation required; scores are computed automatically.
     */
    @Override
    public boolean validateStep() {
        return true;
    }

    /** Removes unnecessary ".0" suffix from whole-number doubles. */
    private String removeTrailingZero(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
