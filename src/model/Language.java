package model;

public class Language {

    // LOC per Function Point ratios per language
    // Source: standard Capers Jones language-level table
    public static final String[] LANGUAGES = {
            "Assembler", "Ada 95", "C", "C++", "C#",
            "COBOL", "FORTRAN", "HTML", "Java",
            "JavaScript", "VBScript", "Visual Basic"
    };

    // LOC per FP for each language (used to compute code size)
    // These are standard industry averages
    private static final double[] LOC_PER_FP = {
            320,  // Assembler
            71,   // Ada 95
            128,  // C
            53,   // C++
            54,   // C#
            91,   // COBOL
            106,  // FORTRAN
            34,   // HTML
            53,   // Java
            47,   // JavaScript
            32,   // VBScript
            32    // Visual Basic
    };

    private String selectedLanguage = null;

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    // Returns "None" if nothing selected — ready for the FP pane label
    public String getDisplayName() {
        return selectedLanguage != null ? selectedLanguage : "None";
    }

    public void setSelectedLanguage(String language) {
        this.selectedLanguage = language;
    }

    public boolean hasLanguage() {
        return selectedLanguage != null;
    }

    // Returns LOC estimate for a given FP value
    // Returns -1 if no language selected
    public double computeCodeSize(double functionPoints) {
        if (selectedLanguage == null) return -1;

        for (int i = 0; i < LANGUAGES.length; i++) {
            if (LANGUAGES[i].equals(selectedLanguage)) {
                return functionPoints * LOC_PER_FP[i];
            }
        }
        return -1;
    }
}