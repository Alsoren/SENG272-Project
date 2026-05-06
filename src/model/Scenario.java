package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Scenario
 * Represents a complete predefined measurement scenario.
 * A scenario belongs to one mode and one quality type, and contains
 * one or more Dimension objects (each with its own Metric list).
 */
public class Scenario {

    private final String name;
    private final String mode;
    private final QualityType qualityType;
    private final ArrayList<Dimension> dimensions;

    public Scenario(String name, String mode, QualityType qualityType) {
        this.name = name;
        this.mode = mode;
        this.qualityType = qualityType;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) { dimensions.add(dimension); }

    public String getName()              { return name; }
    public String getMode()              { return mode; }
    public QualityType getQualityType()  { return qualityType; }
    public List<Dimension> getDimensions() { return dimensions; }

    /**
     * Returns the scenario name so JComboBox displays it correctly
     * without needing a custom ListCellRenderer.
     */
    @Override
    public String toString() {
        return name;
    }
}
