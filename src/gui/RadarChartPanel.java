package gui;

import model.Dimension;
import model.Scenario;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
 * Labels are never truncated — the radius is reduced when there are many dimensions
 * so that labels always have enough room to render fully.
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
     * @param scenario the fully scored scenario, or null to show a placeholder
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
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (scenario == null || scenario.getDimensions().isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.drawString("Radar chart will appear after scenario analysis.", 10, 30);
            g2.dispose();
            return;
        }

        List<Dimension> dimensions = scenario.getDimensions();
        int count  = dimensions.size();
        int width  = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Use a smaller fraction of the panel for the chart so labels have room
        int radius = Math.min(width, height) / 4;

        // ── Grid polygons (scale levels 1–5) ─────────────────────────
        g2.setColor(new Color(210, 210, 210));
        g2.setStroke(new BasicStroke(1f));
        for (int level = 1; level <= 5; level++) {
            double lr = radius * (level / 5.0);
            g2.draw(createPolygonPath(count, centerX, centerY, lr, null));
        }

        // ── Axis lines ────────────────────────────────────────────────
        g2.setColor(new Color(180, 180, 180));
        for (int i = 0; i < count; i++) {
            double angle = getAngle(i, count);
            int x = centerX + (int) Math.round(Math.cos(angle) * radius);
            int y = centerY + (int) Math.round(Math.sin(angle) * radius);
            g2.drawLine(centerX, centerY, x, y);
        }

        // ── Score polygon ─────────────────────────────────────────────
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            values[i] = dimensions.get(i).getDimensionScore();
        }

        Path2D scoreShape = createPolygonPath(count, centerX, centerY, radius, values);
        g2.setColor(new Color(70, 130, 180, 80));
        g2.fill(scoreShape);
        g2.setColor(new Color(30, 90, 150));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(scoreShape);

        // ── Labels (full name, no truncation) ─────────────────────────
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(new Color(50, 50, 50));

        // Label offset: extra space beyond the chart radius
        int labelOffset = radius + 28;

        for (int i = 0; i < count; i++) {
            Dimension dim = dimensions.get(i);
            double angle = getAngle(i, count);

            // Full dimension name + score — no truncation
            String line1 = dim.getName();
            String line2 = "(" + String.format("%.2f", dim.getDimensionScore()) + " / 5.00)";

            int lx = centerX + (int) Math.round(Math.cos(angle) * labelOffset);
            int ly = centerY + (int) Math.round(Math.sin(angle) * labelOffset);

            // Centre text horizontally on the computed point
            int w1 = fm.stringWidth(line1);
            int w2 = fm.stringWidth(line2);
            int lineHeight = fm.getHeight();

            // Shift vertically so the two lines straddle the label anchor
            g2.drawString(line1, lx - w1 / 2, ly - 2);
            g2.drawString(line2, lx - w2 / 2, ly + lineHeight - 2);
        }

        g2.dispose();
    }

    /**
     * Creates a polygon path.
     * When values is null the polygon is drawn at full radius (used for grid lines).
     */
    private Path2D createPolygonPath(int count, int cx, int cy, double radius, double[] values) {
        Path2D path = new Path2D.Double();
        for (int i = 0; i < count; i++) {
            double ratio = values == null ? 1.0 : Math.max(0.0, Math.min(5.0, values[i])) / 5.0;
            double angle = getAngle(i, count);
            double x = cx + Math.cos(angle) * radius * ratio;
            double y = cy + Math.sin(angle) * radius * ratio;
            if (i == 0) path.moveTo(x, y);
            else        path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }

    /**
     * Returns the angle for the i-th axis, starting from the top (−π/2).
     */
    private double getAngle(int index, int count) {
        return -Math.PI / 2 + (2 * Math.PI * index / count);
    }
}
