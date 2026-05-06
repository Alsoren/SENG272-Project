package service;

import model.Dimension;
import model.Scenario;

/**
 * GapAnalyzer
 * Identifies the lowest-scoring dimension in a scenario and produces a GapAnalysisResult
 * containing the gap value (5.0 − score) and a quality level label.
 */
public class GapAnalyzer {

    private final ScoreCalculator scoreCalculator;

    public GapAnalyzer() {
        this.scoreCalculator = new ScoreCalculator();
    }

    /**
     * Analyses all dimensions in the given scenario and returns the result for the weakest one.
     *
     * @param scenario the fully defined scenario (must contain at least one dimension)
     * @return a GapAnalysisResult, or null if the scenario is empty
     */
    public GapAnalysisResult analyze(Scenario scenario) {
        if (scenario == null || scenario.getDimensions().isEmpty()) {
            return null;
        }

        Dimension lowestDimension = null;
        double lowestScore = Double.MAX_VALUE;

        // Find the dimension with the minimum weighted average score
        for (Dimension dimension : scenario.getDimensions()) {
            double dimensionScore = scoreCalculator.calculateDimensionScore(dimension);
            if (dimensionScore < lowestScore) {
                lowestScore = dimensionScore;
                lowestDimension = dimension;
            }
        }

        double gap = 5.0 - lowestScore;
        String qualityLevel = getQualityLevel(lowestScore);
        String message = "This dimension has the lowest score and requires the most improvement.";

        return new GapAnalysisResult(lowestDimension, lowestScore, gap, qualityLevel, message);
    }

    /**
     * Maps a dimension score to a quality level label.
     *
     * 4.5 – 5.0  → Excellent
     * 3.5 – 4.49 → Good
     * 2.5 – 3.49 → Needs Improvement
     * 1.0 – 2.49 → Poor
     */
    public String getQualityLevel(double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 3.5) return "Good";
        if (score >= 2.5) return "Needs Improvement";
        return "Poor";
    }
}
