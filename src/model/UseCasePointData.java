package model;

public class UseCasePointData {

    private String paneName;

    // --- UUCW: Unadjusted Use Case Weight ---
    // User enters count of Simple, Average, Complex use cases
    private int simpleUseCases   = 0;
    private int averageUseCases  = 0;
    private int complexUseCases  = 0;

    // Fixed weights per complexity
    public static final int SIMPLE_UC_WEIGHT  = 5;
    public static final int AVERAGE_UC_WEIGHT = 10;
    public static final int COMPLEX_UC_WEIGHT = 15;

    // --- UAW: Unadjusted Actor Weight ---
    // User enters count of Simple, Average, Complex actors
    private int simpleActors  = 0;
    private int averageActors = 0;
    private int complexActors = 0;

    // Fixed weights per complexity
    public static final int SIMPLE_ACTOR_WEIGHT  = 1;
    public static final int AVERAGE_ACTOR_WEIGHT = 2;
    public static final int COMPLEX_ACTOR_WEIGHT = 3;

    // --- TCF: Technical Complexity Factor ---
    // 13 factors rated 0-5 by user, default 0
    private double[] tcfRatings = new double[13];

    // Fixed weights for each TCF factor
    public static final double[] TCF_WEIGHTS = {
            2.0,  // T1:  Distributed system
            1.0,  // T2:  Response time / performance
            1.0,  // T3:  End-user efficiency
            1.0,  // T4:  Complex internal processing
            1.0,  // T5:  Reusable code
            0.5,  // T6:  Easy to install
            0.5,  // T7:  Easy to use
            2.0,  // T8:  Portable
            1.0,  // T9:  Easy to change
            1.0,  // T10: Concurrent
            1.0,  // T11: Security
            1.0,  // T12: Third party access
            1.0   // T13: Training required
    };

    public static final String[] TCF_FACTORS = {
            "T1:  Distributed system required",
            "T2:  Response time / performance objectives",
            "T3:  End-user efficiency",
            "T4:  Complex internal processing",
            "T5:  Reusable code",
            "T6:  Easy to install",
            "T7:  Easy to use",
            "T8:  Portable",
            "T9:  Easy to change",
            "T10: Concurrent",
            "T11: Special security features",
            "T12: Direct access for third parties",
            "T13: Special user training required"
    };

    // --- ECF: Environmental Complexity Factor ---
    // 8 factors rated 0-5 by user, default 0
    private double[] ecfRatings = new double[8];

    // Fixed weights for each ECF factor (note negatives for E7 and E8)
    public static final double[] ECF_WEIGHTS = {
            1.5,  // E1: Familiar with process
            0.5,  // E2: Application experience
            1.0,  // E3: OO experience
            0.5,  // E4: Lead analyst capability
            1.0,  // E5: Motivation
            2.0,  // E6: Stable requirements
            -1.0,  // E7: Part-time workers
            -1.0   // E8: Difficult programming language
    };

    public static final String[] ECF_FACTORS = {
            "E1: Familiar with process being used",
            "E2: Application experience",
            "E3: Object-oriented experience",
            "E4: Lead analyst capability",
            "E5: Motivation of the team",
            "E6: Stability of requirements",
            "E7: Part-time workers (negative impact)",
            "E8: Difficult programming language (negative impact)"
    };

    // --- Editable estimation fields with defaults ---
    private double productivityFactor = 20.0;  // hours per UCP
    private double locPerPM           = 700.0; // LOC per person-month
    private double locPerUCP          = 100.0; // LOC per UCP

    // --- Computed results ---
    private double uucw             = 0;
    private double uaw              = 0;
    private double tcf              = 0;
    private double ecf              = 0;
    private double totalUCP         = 0;
    private double estimatedHours   = 0;
    private double estimatedLOC     = 0;
    private double estimatedPM      = 0;

    public UseCasePointData(String paneName) {
        this.paneName = paneName;
    }

    // --- Getters and Setters: UUCW ---
    public int getSimpleUseCases()   { return simpleUseCases; }
    public int getAverageUseCases()  { return averageUseCases; }
    public int getComplexUseCases()  { return complexUseCases; }

    public void setSimpleUseCases(int v)  { simpleUseCases = v; }
    public void setAverageUseCases(int v) { averageUseCases = v; }
    public void setComplexUseCases(int v) { complexUseCases = v; }

    // --- Getters and Setters: UAW ---
    public int getSimpleActors()  { return simpleActors; }
    public int getAverageActors() { return averageActors; }
    public int getComplexActors() { return complexActors; }

    public void setSimpleActors(int v)  { simpleActors = v; }
    public void setAverageActors(int v) { averageActors = v; }
    public void setComplexActors(int v) { complexActors = v; }

    // --- Getters and Setters: TCF ---
    public double[] getTcfRatings()           { return tcfRatings; }
    public void setTcfRating(int i, double v) { tcfRatings[i] = v; }

    // --- Getters and Setters: ECF ---
    public double[] getEcfRatings()           { return ecfRatings; }
    public void setEcfRating(int i, double v) { ecfRatings[i] = v; }

    // --- Getters and Setters: estimation fields ---
    public double getProductivityFactor() { return productivityFactor; }
    public double getLocPerPM()           { return locPerPM; }
    public double getLocPerUCP()          { return locPerUCP; }

    public void setProductivityFactor(double v) { productivityFactor = v; }
    public void setLocPerPM(double v)           { locPerPM = v; }
    public void setLocPerUCP(double v)          { locPerUCP = v; }

    // --- Getters: computed results ---
    public String getPaneName()      { return paneName; }
    public double getUUCW()          { return uucw; }
    public double getUAW()           { return uaw; }
    public double getTCF()           { return tcf; }
    public double getECF()           { return ecf; }
    public double getTotalUCP()      { return totalUCP; }
    public double getEstimatedHours(){ return estimatedHours; }
    public double getEstimatedLOC()  { return estimatedLOC; }
    public double getEstimatedPM()   { return estimatedPM; }

    // --- Computations ---

    public double computeUUCW() {
        uucw = (simpleUseCases  * SIMPLE_UC_WEIGHT)
                + (averageUseCases * AVERAGE_UC_WEIGHT)
                + (complexUseCases * COMPLEX_UC_WEIGHT);
        return uucw;
    }

    public double computeUAW() {
        uaw = (simpleActors  * SIMPLE_ACTOR_WEIGHT)
                + (averageActors * AVERAGE_ACTOR_WEIGHT)
                + (complexActors * COMPLEX_ACTOR_WEIGHT);
        return uaw;
    }

    public double computeTCF() {
        double tf = 0;
        for (int i = 0; i < 13; i++) {
            tf += tcfRatings[i] * TCF_WEIGHTS[i];
        }
        tcf = 0.6 + (tf / 100.0);
        return tcf;
    }

    public double computeECF() {
        double ef = 0;
        for (int i = 0; i < 8; i++) {
            ef += ecfRatings[i] * ECF_WEIGHTS[i];
        }
        ecf = 1.4 + (-0.03 * ef);
        return ecf;
    }

    public void computeAll() {
        computeUUCW();
        computeUAW();
        computeTCF();
        computeECF();
        totalUCP       = (uucw + uaw) * tcf * ecf;
        estimatedHours = totalUCP * productivityFactor;
        estimatedLOC   = totalUCP * locPerUCP;
        estimatedPM    = estimatedLOC / locPerPM;
    }
}
