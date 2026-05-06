package service;

import model.Dimension;

/**
 * GapAnalysisResult
 * Immutable value object that holds the output of GapAnalyzer.analyze().
 * Separating the result into its own class keeps GapAnalyzer and AnalysePanel decoupled.
 */
public class GapAnalysisResult {

    private final Dimension lowestDimension;
    private final double score;
    private final double gap;
    private final String qualityLevel;
    private final String improvementMessage;

    public GapAnalysisResult(Dimension lowestDimension, double score, double gap,
                              String qualityLevel, String improvementMessage) {
        this.lowestDimension = lowestDimension;
        this.score = score;
        this.gap = gap;
        this.qualityLevel = qualityLevel;
        this.improvementMessage = improvementMessage;
    }

    public Dimension getLowestDimension()  { return lowestDimension; }
    public double getScore()               { return score; }
    public double getGap()                 { return gap; }
    public String getQualityLevel()        { return qualityLevel; }
    public String getImprovementMessage()  { return improvementMessage; }
}
