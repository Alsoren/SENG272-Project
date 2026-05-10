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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 * Step 3 - Plan Measurement
 * Displays the selected scenario's dimensions and metrics in a read-only table.
 * Dimension name and coefficient appear only on the first metric row of each dimension;
 * subsequent rows in the same dimension leave those columns blank to avoid repetition.
 */
public class PlanPanel extends WizardPanel {

    private final DefaultTableModel tableModel;
    private final JLabel scenarioLabel;

    public PlanPanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);

        scenarioLabel = new JLabel("Step 3 - Plan Measurement");
        scenarioLabel.setFont(scenarioLabel.getFont().deriveFont(Font.BOLD, 16f));
        scenarioLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scenarioLabel, BorderLayout.NORTH);

        // Read-only table; dimension columns only populated on the first metric row
        String[] columns = {"Dimension", "Dim. Coeff", "Metric", "Metric Coeff", "Direction", "Range", "Unit"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only step
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        // Custom renderer: shade the first metric row of each dimension (where dim name appears)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    // Rows where the Dimension column is non-empty are group-header rows
                    Object dimCell = t.getModel().getValueAt(row, 0);
                    if (dimCell != null && !dimCell.toString().isEmpty()) {
                        setBackground(new Color(220, 230, 245)); // light blue for dimension row
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                }
                return this;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Populates the table with the dimensions and metrics of the selected scenario.
     * Dimension name and coefficient are shown only on the first metric row of each dimension.
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

        for (Dimension dimension : scenario.getDimensions()) {
            boolean firstMetric = true;
            for (Metric metric : dimension.getMetrics()) {
                // Show dimension name and coefficient only on the first row of each dimension
                String dimName  = firstMetric ? dimension.getName() : "";
                Object dimCoeff = firstMetric ? dimension.getCoefficient() : "";
                firstMetric = false;

                tableModel.addRow(new Object[]{
                        dimName,
                        dimCoeff,
                        metric.getName(),
                        metric.getCoefficient(),
                        metric.getDirection().getDisplayName(),
                        formatRange(metric.getMinValue(), metric.getMaxValue()),
                        metric.getUnit()
                });
            }
        }
    }

    @Override
    public boolean validateStep() {
        return true; // Read-only step; always valid
    }

    private String formatRange(double min, double max) {
        return removeTrailingZero(min) + " - " + removeTrailingZero(max);
    }

    private String removeTrailingZero(double value) {
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }
}
