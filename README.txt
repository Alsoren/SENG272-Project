================================================================================
ISO 15939 Measurement Process Simulator
================================================================================
Course        : Software Project II
Assignment    : Individual Java Swing GUI Application
Name          : Ahmet Alp Keleş
Student ID    : 202328038

--------------------------------------------------------------------------------
RUNNING
--------------------------------------------------------------------------------
  java -cp out Main

--------------------------------------------------------------------------------
PROJECT STRUCTURE
--------------------------------------------------------------------------------
src/
├── Main.java                          Entry point (EDT launch)
├── app/
│   └── AppState.java                  Shared session state between panels
├── model/
│   ├── UserProfile.java               Step 1 profile data
│   ├── Metric.java                    Single measurable metric
│   ├── Dimension.java                 Quality dimension grouping metrics
│   ├── Scenario.java                  Complete measurement scenario
│   ├── Direction.java                 Enum: HIGHER_IS_BETTER / LOWER_IS_BETTER
│   └── QualityType.java               Enum: PRODUCT_QUALITY / PROCESS_QUALITY
├── service/
│   ├── ScenarioRepository.java        All hard-coded scenario data (centralised)
│   ├── ScoreCalculator.java           ISO 15939 metric & dimension score formulas
│   ├── GapAnalyzer.java               Identifies lowest-scoring dimension
│   └── GapAnalysisResult.java         Immutable result object for gap analysis
└── gui/
    ├── MainFrame.java                 Main window + CardLayout controller
    ├── WizardPanel.java               Abstract base class for all wizard steps
    ├── StepIndicatorPanel.java        Top progress bar (✓ done / active / pending)
    ├── ProfilePanel.java              Step 1: profile input & validation
    ├── DefinePanel.java               Step 2: quality type / mode / scenario
    ├── PlanPanel.java                 Step 3: read-only measurement plan table
    ├── CollectPanel.java              Step 4: raw values + calculated scores
    ├── AnalysePanel.java              Step 5: dimension bars + gap analysis
    └── RadarChartPanel.java           Bonus: radar chart (Graphics2D)

--------------------------------------------------------------------------------
SCENARIOS
--------------------------------------------------------------------------------
Education mode:
  • Scenario C — Team Alpha  (5 dimensions, 10 metrics)
  • Scenario D — Team Beta   (3 dimensions,  6 metrics)

Health mode:
  • Scenario A — Hospital System  (4 dimensions, 8 metrics)
  • Scenario B — Clinic System    (3 dimensions, 6 metrics)

--------------------------------------------------------------------------------
SCORE FORMULAS
--------------------------------------------------------------------------------
Higher is better: score = 1 + ((value - min) / (max - min)) * 4
Lower  is better: score = 5 - ((value - min) / (max - min)) * 4
Result clamped to [1.0, 5.0] and rounded to nearest 0.5.

Dimension score: Σ(metricScore × metricCoeff) / Σ(metricCoeff)
Gap value      : 5.0 - dimensionScore

Quality levels:
  4.5 – 5.0  → Excellent
  3.5 – 4.49 → Good
  2.5 – 3.49 → Needs Improvement
  1.0 – 2.49 → Poor

--------------------------------------------------------------------------------
NOTES
--------------------------------------------------------------------------------
• No external libraries — only Java SE standard library + Swing.
• No file I/O — all scenario data is hard-coded in ScenarioRepository.java.
• Compilable and runnable from the command line with no IDE dependency.
================================================================================
