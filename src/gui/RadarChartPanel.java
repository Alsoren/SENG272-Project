package gui;

import model.Dimension;
import model.Scenario;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * RadarChartPanel (Bonus)
 * Draws a radar (spider) chart of all dimension scores using Java 2D Graphics.
 * Each axis of the polygon represents one quality dimension, scaled from 0 to 5.
 * The filled polygon shows the actual scores; concentric grid lines show scale levels.
 */
public class RadarChartPanel extends JPanel {

    private Scenario scenario;

    public RadarChartPanel() {
        setPreferredSize(new java.awt.Dimension(360, 300));
        setBackground(Color.WHITE);
    }

    /**
     * Sets the scenario to visualise and triggers a repaint.
     *
     * @param scenario the fully scored scenario, or null to show a placeholder message
     */
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (scenario == null || scenario.getDimensions().isEmpty()) {
            g2.drawString("Radar chart will appear after scenario analysis.", 20, 30);
            g2.dispose();
            return;
        }

        List<Dimension> dimensions = scenario.getDimensions();
        int count = dimensions.size();
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2 + 10;
        int radius = Math.min(width, height) / 3;

        // Draw concentric grid polygons for scale levels 1–5
        g2.setColor(new Color(230, 230, 230));
        for (int level = 1; level <= 5; level++) {
            double levelRadius = radius * (level / 5.0);
            Path2D grid = createPolygonPath(count, centerX, centerY, levelRadius, null);
            g2.draw(grid);
        }

        // Draw axis lines from the centre to each vertex
        g2.setColor(new Color(180, 180, 180));
        for (int i = 0; i < count; i++) {
            double angle = getAngle(i, count);
            int x = centerX + (int) Math.round(Math.cos(angle) * radius);
            int y = centerY + (int) Math.round(Math.sin(angle) * radius);
            g2.drawLine(centerX, centerY, x, y);
        }

        // Collect dimension scores for the filled polygon
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            values[i] = dimensions.get(i).getDimensionScore();
        }

        // Draw the filled score polygon
        Path2D scoreShape = createPolygonPath(count, centerX, centerY, radius, values);
        g2.setColor(new Color(70, 130, 180, 70));
        g2.fill(scoreShape);
        g2.setColor(new Color(30, 90, 150));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(scoreShape);

        // Draw dimension labels outside each vertex
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.DARK_GRAY);
        for (int i = 0; i < count; i++) {
            Dimension dimension = dimensions.get(i);
            double angle = getAngle(i, count);
            int labelX = centerX + (int) Math.round(Math.cos(angle) * (radius + 35));
            int labelY = centerY + (int) Math.round(Math.sin(angle) * (radius + 35));
            String text = shorten(dimension.getName()) + " (" + String.format("%.1f", dimension.getDimensionScore()) + ")";
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, labelX - textWidth / 2, labelY);
        }

        g2.dispose();
    }

    /**
     * Creates a polygon path for either a grid level (values == null) or the score shape.
     *
     * @param count   number of dimensions / axes
     * @param cx      centre x
     * @param cy      centre y
     * @param radius  maximum radius (corresponds to score 5.0)
     * @param values  actual scores; null means draw at full radius (grid polygon)
     */
    private Path2D createPolygonPath(int count, int cx, int cy, double radius, double[] values) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < count; i++) {
            // Scale score to [0, 1]; null values draw the full grid polygon
            double ratio = values == null ? 1.0 : Math.max(0.0, Math.min(5.0, values[i])) / 5.0;
            double angle = getAngle(i, count);
            double x = cx + Math.cos(angle) * radius * ratio;
            double y = cy + Math.sin(angle) * radius * ratio;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }

    /**
     * Returns the angle (in radians) for the i-th axis, starting from the top (−π/2).
     */
    private double getAngle(int index, int count) {
        return -Math.PI / 2 + (2 * Math.PI * index / count);
    }

    /**
     * Truncates a dimension name to 16 characters to prevent label overlap.
     */
    private String shorten(String text) {
        if (text.length() <= 16) {
            return text;
        }
        return text.substring(0, 13) + "...";
    }
}
