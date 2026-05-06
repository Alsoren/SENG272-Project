package model;

/**
 * Direction
 * Enum representing whether a higher or lower metric value is more favourable.
 * Used by ScoreCalculator to select the correct scoring formula.
 */
public enum Direction {
    HIGHER_IS_BETTER("Higher is better"),
    LOWER_IS_BETTER("Lower is better");

    private final String displayName;

    Direction(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}
