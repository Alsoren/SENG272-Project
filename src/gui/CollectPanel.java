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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 * Step 4 - Collect Data
 * Displays predefined raw metric values alongside automatically calculated scores.
 * Dimension name rows are inserted as section headers to group metrics visually.
 * Scores are computed by ScoreCalculator (ISO 15939 formula), clamped to 1.0–5.0,
 * then rounded to the nearest 0.5.
 */
public class CollectPanel extends WizardPanel {

    private final DefaultTableModel tableModel;
    private final ScoreCalculator scoreCalculator;

    // Sentinel value used to identify dimension header rows in the renderer
    private static final String HEADER_MARKER = "__HEADER__";

    public CollectPanel(AppState appState, ScenarioRepository scenarioRepository) {
        super(appState, scenarioRepository);
        this.scoreCalculator = new ScoreCalculator();

        JLabel title = new JLabel("Step 4 - Collect Data");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Metric", "Direction", "Range", "Value", "Score (1–5)", "Coeff / Unit"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Scores are computed automatically
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        // Renderer: shade dimension header rows differently from metric rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                // Replace the sentinel with the actual display text for column 0
                Object displayValue = value;
                if (col == 0 && value != null && value.toString().startsWith(HEADER_MARKER)) {
                    displayValue = value.toString().replace(HEADER_MARKER, "");
                }
                super.getTableCellRendererComponent(t, displayValue, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    Object col0 = t.getModel().getValueAt(row, 0);
                    if (col0 != null && col0.toString().startsWith(HEADER_MARKER)) {
                        // Dimension header row styling
                        setBackground(new Color(220, 230, 245));
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
     * Inserts a dimension header row before each group of metric rows,
     * then calculates and displays the score for each metric.
     */
    @Override
    public void onEnterStep() {
        tableModel.setRowCount(0);
        Scenario scenario = appState.getSelectedScenario();
        if (scenario == null) return;

        for (Dimension dimension : scenario.getDimensions()) {
            // Dimension header row — sentinel prefix lets the renderer identify it
            tableModel.addRow(new Object[]{
                    HEADER_MARKER + dimension.getName() + "  (Coeff: " + dimension.getCoefficient() + ")",
                    "", "", "", "", ""
            });

            for (Metric metric : dimension.getMetrics()) {
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

    @Override
    public boolean validateStep() {
        return true; // Scores are computed automatically; no user input to validate
    }

    private String removeTrailingZero(double value) {
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }
}
