package model;

/**
 * QualityType
 * Enum representing the two quality measurement categories.
 * Product Quality covers software product characteristics (performance, usability, etc.).
 * Process Quality covers development process characteristics (sprint efficiency, etc.).
 */
public enum QualityType {
    PRODUCT_QUALITY("Product Quality"),
    PROCESS_QUALITY("Process Quality");

    private final String displayName;

    QualityType(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}
