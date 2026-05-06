package gui;

import app.AppState;
import model.Dimension;
import model.Metric;
import model.Scenario;
import service.ScenarioRepository;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Step 3 - Plan Measurement
 * Displays the selected scenario's dimensions and metrics in a read-only table.
 * Each row shows dimension name, dimension coefficient, metric name,
 * metric coefficient, direction, range, and unit.
 */
public class PlanPanel extends WizardPanel {

    private final DefaultTableModel tableModel;
    private final JLabel scenarioLabel;

    public PlanPanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);

        // Header label showing the current scenario name
        scenarioLabel = new JLabel("Step 3 - Plan Measurement");
        scenarioLabel.setFont(scenarioLabel.getFont().deriveFont(Font.BOLD, 16f));
        scenarioLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scenarioLabel, BorderLayout.NORTH);

        // Read-only table with all dimension and metric details
        String[] columns = {"Dimension", "Dim. Coeff", "Metric", "Metric Coeff", "Direction", "Range", "Unit"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This step is read-only; no cell may be edited
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Populates the table with the dimensions and metrics of the selected scenario.
     * Called every time the user navigates to this step.
     */
    @Override
    public void onEnterStep() {
        tableModel.setRowCount(0);
        Scenario scenario = appState.getSelectedScenario();
        if (scenario == null) {
            scenarioLabel.setText("Step 3 - Plan Measurement");
            return;
        }

        scenarioLabel.setText("Step 3 - Plan Measurement  |  " + scenario.getName());

        // Add one row per metric; repeat dimension name and coefficient for each metric row
        for (Dimension dimension : scenario.getDimensions()) {
            for (Metric metric : dimension.getMetrics()) {
                tableModel.addRow(new Object[]{
                        dimension.getName(),
                        dimension.getCoefficient(),
                        metric.getName(),
                        metric.getCoefficient(),
                        metric.getDirection().getDisplayName(),
                        formatRange(metric.getMinValue(), metric.getMaxValue()),
                        metric.getUnit()
                });
            }
        }
    }

    /**
     * No validation needed for the read-only plan step.
     */
    @Override
    public boolean validateStep() {
        return true;
    }

    /** Formats a min–max range as a readable string, e.g. "0 - 100". */
    private String formatRange(double min, double max) {
        return removeTrailingZero(min) + " - " + removeTrailingZero(max);
    }

    /** Removes unnecessary ".0" suffix from whole-number doubles. */
    private String removeTrailingZero(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
