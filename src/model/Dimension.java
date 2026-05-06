package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Dimension
 * Groups a set of related metrics under one quality dimension.
 * Has a coefficient used to weight the dimension's contribution to the overall profile.
 * dimensionScore is calculated by ScoreCalculator and stored here for use in the radar chart.
 */
public class Dimension {

    private final String name;
    private final int coefficient;
    private final ArrayList<Metric> metrics;
    private double dimensionScore; // Set by ScoreCalculator

    public Dimension(String name, int coefficient) {
        this.name = name;
        this.coefficient = coefficient;
        this.metrics = new ArrayList<>();
        this.dimensionScore = 0.0;
    }

    public void addMetric(Metric metric) { metrics.add(metric); }

    public String getName()              { return name; }
    public int getCoefficient()          { return coefficient; }
    public List<Metric> getMetrics()     { return metrics; }
    public double getDimensionScore()    { return dimensionScore; }
    public void setDimensionScore(double score) { this.dimensionScore = score; }
}
