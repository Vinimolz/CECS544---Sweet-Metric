package model;

public class FunctionPointData {

    private String paneName;

    // The 5 FP input counts
    private int externalInputs    = 0;
    private int externalOutputs   = 0;
    private int externalInquiries = 0;
    private int internalLogical   = 0;
    private int externalInterface = 0;

    // Weighting factors per row (0=Simple, 1=Average, 2=Complex)
    // Default to Average (1) as per requirement 4.11
    private int[] weights = {1, 1, 1, 1, 1};

    // Standard weighting factor table
    // [row][Simple, Average, Complex]
    public static final int[][] WEIGHT_TABLE = {
            {3, 4, 6},   // External Inputs
            {4, 5, 7},   // External Outputs
            {3, 4, 6},   // External Inquiries
            {7, 10, 15}, // Internal Logical Files
            {5, 7, 10}   // External Interface Files
    };

    // VAF fields - 14 questions, default 0 (requirement 4.22)
    private int[] vafValues = new int[14];

    // Computed results
    private double functionPoints = 0;
    private double codeSize       = 0;

    public FunctionPointData(String paneName) {
        this.paneName = paneName;
    }

    // --- Getters and Setters ---

    public String getPaneName() { return paneName; }

    public int getExternalInputs()    { return externalInputs; }
    public int getExternalOutputs()   { return externalOutputs; }
    public int getExternalInquiries() { return externalInquiries; }
    public int getInternalLogical()   { return internalLogical; }
    public int getExternalInterface() { return externalInterface; }

    public void setExternalInputs(int v)    { externalInputs = v; }
    public void setExternalOutputs(int v)   { externalOutputs = v; }
    public void setExternalInquiries(int v) { externalInquiries = v; }
    public void setInternalLogical(int v)   { internalLogical = v; }
    public void setExternalInterface(int v) { externalInterface = v; }

    public int getWeight(int row)          { return weights[row]; }
    public void setWeight(int row, int w)  { weights[row] = w; }

    public int[] getVafValues()            { return vafValues; }
    public void setVafValue(int i, int v)  { vafValues[i] = v; }

    public double getFunctionPoints()      { return functionPoints; }
    public double getCodeSize()            { return codeSize; }
    public void setCodeSize(double v)      { codeSize = v; }

    // --- Computations ---

    // Returns the weighted total for a single row
    public int getRowTotal(int row) {
        int count = getCountForRow(row);
        int weightIndex = weights[row];
        return count * WEIGHT_TABLE[row][weightIndex];
    }

    // Sum of all row totals
    public int getTotalCount() {
        int total = 0;
        for (int i = 0; i < 5; i++) {
            total += getRowTotal(i);
        }
        return total;
    }

    // Sum of all VAF values
    public int getVafSum() {
        int sum = 0;
        for (int v : vafValues) sum += v;
        return sum;
    }

    // FP formula: totalCount * (0.65 + 0.01 * vafSum)
    public double computeFunctionPoints() {
        functionPoints = getTotalCount() * (0.65 + 0.01 * getVafSum());
        return functionPoints;
    }

    // Helper to get count by row index
    private int getCountForRow(int row) {
        switch (row) {
            case 0: return externalInputs;
            case 1: return externalOutputs;
            case 2: return externalInquiries;
            case 3: return internalLogical;
            case 4: return externalInterface;
            default: return 0;
        }
    }

    // Add to FunctionPointData.java
    public void setFunctionPoints(double fp) {
        this.functionPoints = fp;
    }
}