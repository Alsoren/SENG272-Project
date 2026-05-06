package model;

/**
 * Metric
 * Represents a single measurable item within a quality dimension.
 * Stores both static metadata (name, coefficient, direction, range, unit)
 * and runtime data (rawValue and the calculated score).
 * Fields are private with controlled access (encapsulation).
 */
public class Metric {

    private final String name;
    private final int coefficient;
    private final Direction direction;
    private final double minValue;
    private final double maxValue;
    private final String unit;
    private final double rawValue;
    private double score; // Calculated by ScoreCalculator; mutable

    public Metric(String name, int coefficient, Direction direction,
                  double minValue, double maxValue, String unit, double rawValue) {
        this.name = name;
        this.coefficient = coefficient;
        this.direction = direction;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unit = unit;
        this.rawValue = rawValue;
        this.score = 0.0;
    }

    public String getName()        { return name; }
    public int getCoefficient()    { return coefficient; }
    public Direction getDirection(){ return direction; }
    public double getMinValue()    { return minValue; }
    public double getMaxValue()    { return maxValue; }
    public String getUnit()        { return unit; }
    public double getRawValue()    { return rawValue; }
    public double getScore()       { return score; }

    /** Called by ScoreCalculator after computing the normalised score. */
    public void setScore(double score) { this.score = score; }
}
