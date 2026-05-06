package service;

import model.Direction;
import model.Dimension;
import model.Metric;

/**
 * ScoreCalculator
 * Implements the ISO 15939 metric and dimension scoring formulas.
 * This class contains no GUI code; all calculation logic is isolated here (MVC separation).
 */
public class ScoreCalculator {

    /**
     * Calculates a normalised score (1.0–5.0) for a single metric and stores it in the metric.
     *
     * Formula:
     *   Higher is better: score = 1 + ((value - min) / (max - min)) * 4
     *   Lower  is better: score = 5 - ((value - min) / (max - min)) * 4
     *
     * The result is clamped to [1.0, 5.0] and rounded to the nearest 0.5.
     *
     * @param metric the metric to score (rawValue, min, max, and direction must be set)
     * @return the calculated score
     */
    public double calculateMetricScore(Metric metric) {
        double min = metric.getMinValue();
        double max = metric.getMaxValue();
        double value = metric.getRawValue();
        double score;

        if (metric.getDirection() == Direction.HIGHER_IS_BETTER) {
            score = 1 + ((value - min) / (max - min)) * 4;
        } else {
            score = 5 - ((value - min) / (max - min)) * 4;
        }

        score = clamp(score, 1.0, 5.0);
        score = roundToNearestHalf(score);
        metric.setScore(score); // Persist score back to the metric for use in dimension calculation
        return score;
    }

    /**
     * Calculates the weighted average score for a dimension and stores it in the dimension.
     *
     * Formula: dimensionScore = Σ(metricScore × metricCoefficient) / Σ(metricCoefficient)
     *
     * @param dimension the dimension whose metrics have already been scored
     * @return the calculated dimension score
     */
    public double calculateDimensionScore(Dimension dimension) {
        double weightedTotal = 0;
        double coefficientTotal = 0;

        for (Metric metric : dimension.getMetrics()) {
            double metricScore = calculateMetricScore(metric);
            weightedTotal += metricScore * metric.getCoefficient();
            coefficientTotal += metric.getCoefficient();
        }

        double dimensionScore = coefficientTotal == 0 ? 0 : weightedTotal / coefficientTotal;
        dimension.setDimensionScore(dimensionScore);
        return dimensionScore;
    }

    /** Ensures a value stays within the given [min, max] bounds. */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /** Rounds a value to the nearest 0.5 (e.g. 3.7 → 3.5, 3.8 → 4.0). */
    private double roundToNearestHalf(double value) {
        return Math.round(value * 2.0) / 2.0;
    }
}
