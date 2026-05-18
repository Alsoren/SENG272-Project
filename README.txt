================================================================================
  ISO 15939 Measurement Process Simulator
  Java Swing GUI Application
================================================================================
  Course      : Software Project II
  Name        : Ahmet Alp Keleş
  Student ID  : 202328038
================================================================================

--------------------------------------------------------------------------------
ABOUT
--------------------------------------------------------------------------------
This application simulates the 5-step ISO/IEC 15939 software measurement process
as a Java Swing desktop wizard. Users enter session information, select a
predefined measurement scenario, view quality dimensions and metrics, collect
data with automatically calculated scores, and analyse the results through
dimension-based weighted averages, a radar chart, and gap analysis.

--------------------------------------------------------------------------------
COMPILATION
--------------------------------------------------------------------------------
From the project root directory (where this README.txt is located):

  javac -d out -sourcepath src src/Main.java

This compiles all source files into the "out/" directory.

--------------------------------------------------------------------------------
RUNNING
--------------------------------------------------------------------------------
  java -cp out Main

No IDE is required. The application runs entirely from the command line.

--------------------------------------------------------------------------------
SCREENSHOTS
--------------------------------------------------------------------------------
Screenshots are located in the screenshots/ folder.

  1.png  - Step 1: Profile    — username, school, session name input
  2.png  - Step 2: Define     — quality type / mode / scenario selection (empty)
  3.png  - Step 2: Define     — Education mode selected, scenario dropdown open
  4.png  - Step 2: Define     — Health mode selected, scenario dropdown open
  5.png  - Step 3: Plan       — read-only measurement plan table (Scenario C)
  6.png  - Step 4: Collect    — raw values and calculated scores with dimension headers
  7.png  - Step 5: Analyse    — progress bars, radar chart, and gap analysis
  8.png  - Step 2: Define     — Process Quality + Health mode

--------------------------------------------------------------------------------
PROJECT STRUCTURE
--------------------------------------------------------------------------------
src/
├── Main.java                        Entry point (EDT launch)
├── app/
│   └── AppState.java                Shared session state between all panels
├── model/
│   ├── UserProfile.java             Step 1 profile data
│   ├── Metric.java                  Single measurable metric
│   ├── Dimension.java               Quality dimension grouping metrics
│   ├── Scenario.java                Complete measurement scenario
│   ├── Direction.java               Enum: HIGHER_IS_BETTER / LOWER_IS_BETTER
│   └── QualityType.java             Enum: PRODUCT_QUALITY / PROCESS_QUALITY
├── service/
│   ├── ScenarioRepository.java      All hard-coded scenario data (centralised)
│   ├── ScoreCalculator.java         ISO 15939 metric & dimension score formulas
│   ├── GapAnalyzer.java             Identifies lowest-scoring dimension
│   └── GapAnalysisResult.java       Immutable result object for gap analysis
└── gui/
    ├── MainFrame.java               Main window + CardLayout controller
    ├── WizardPanel.java             Abstract base class for all wizard steps
    ├── StepIndicatorPanel.java      Top progress bar (done / active / pending)
    ├── ProfilePanel.java            Step 1: profile input & validation
    ├── DefinePanel.java             Step 2: quality type / mode / scenario
    ├── PlanPanel.java               Step 3: read-only measurement plan table
    ├── CollectPanel.java            Step 4: raw values + calculated scores
    ├── AnalysePanel.java            Step 5: dimension bars + gap analysis
    └── RadarChartPanel.java         Bonus: radar chart (Graphics2D)

--------------------------------------------------------------------------------
SCENARIOS
--------------------------------------------------------------------------------
Education mode:
  - Scenario C - Team Alpha   (5 dimensions, 10 metrics)
  - Scenario D - Team Beta    (3 dimensions,  6 metrics)

Health mode:
  - Scenario A - Hospital System  (4 dimensions, 8 metrics)
  - Scenario B - Clinic System    (3 dimensions, 6 metrics)

--------------------------------------------------------------------------------
SCORE FORMULAS
--------------------------------------------------------------------------------
Higher is better:  score = 1 + ((value - min) / (max - min)) * 4
Lower  is better:  score = 5 - ((value - min) / (max - min)) * 4

Result is clamped to [1.0, 5.0] and rounded to the nearest 0.5.

Dimension score:  sum(metricScore * metricCoeff) / sum(metricCoeff)
Gap value      :  5.0 - dimensionScore

Quality levels:
  4.5 - 5.0   ->  Excellent
  3.5 - 4.49  ->  Good
  2.5 - 3.49  ->  Needs Improvement
  1.0 - 2.49  ->  Poor

--------------------------------------------------------------------------------
AI Usage Declaration
--------------------------------------------------------------------------------
AI assistance was used in the following part of this project:
RadarChartPanel.java — The radar chart implementation using Java 2D Graphics (Graphics2D) was developed with AI assistance. Specifically, the polygon path calculation using trigonometric functions (Math.cos / Math.sin), the angle distribution formula for each axis, and the coordinate mapping from score values to screen pixels were areas where AI guidance was utilized.


--------------------------------------------------------------------------------
TECHNICAL NOTES
--------------------------------------------------------------------------------
  - Java SE 17 or higher required
  - No external libraries — only the standard Java SE library + Swing
  - No file I/O — all scenario data is hard-coded in ScenarioRepository.java
  - MVC pattern: model / service / gui packages are fully separated
  - CardLayout wizard: each step is a separate JPanel extending WizardPanel
  - OOP: encapsulation (private fields), inheritance (WizardPanel),
         polymorphism (validateStep / onEnterStep overrides)
  - Collections: ArrayList and HashMap used in ScenarioRepository and panels
  - Bonus: Radar chart implemented with Java 2D Graphics (Graphics2D)

================================================================================

--------------------------------------------------------------------------------
PRESENTATION VIDEO
--------------------------------------------------------------------------------
Video Link: https://drive.google.com/file/d/1ng9Nv_Vl5UfIvV7-LXMDiqR5YHoPiZyp/view?usp=drive_link
