package model;

import java.util.ArrayList;
import java.util.List;

public class SMIData {

    // Represents a single row in the SMI table
    public static class SMIRow {
        private int modulesAdded;
        private int modulesChanged;
        private int modulesDeleted;
        private int totalModules; // computed
        private double smi; // computed

        public SMIRow(int added, int changed, int deleted) {
            this.modulesAdded = added;
            this.modulesChanged = changed;
            this.modulesDeleted = deleted;
            this.totalModules = 0;
            this.smi = 0.0;
        }

        // Getters
        public int getModulesAdded() {
            return modulesAdded;
        }

        public int getModulesChanged() {
            return modulesChanged;
        }

        public int getModulesDeleted() {
            return modulesDeleted;
        }

        public int getTotalModules() {
            return totalModules;
        }

        public double getSmi() {
            return smi;
        }

        // Setters for user-editable fields
        public void setModulesAdded(int v) {
            modulesAdded = v;
        }

        public void setModulesChanged(int v) {
            modulesChanged = v;
        }

        public void setModulesDeleted(int v) {
            modulesDeleted = v;
        }

        // Setters for computed fields
        public void setTotalModules(int v) {
            totalModules = v;
        }

        public void setSmi(double v) {
            smi = v;
        }
    }

    // List of rows in the table
    private List<SMIRow> rows = new ArrayList<>();

    public SMIData() {
    }

    // --- Row management ---

    public void addRow(SMIRow row) {
        rows.add(row);
    }

    public SMIRow getRow(int index) {
        return rows.get(index);
    }

    public int getRowCount() {
        return rows.size();
    }

    public List<SMIRow> getRows() {
        return rows;
    }

    public void clearRows() {
        rows.clear();
    }

    // --- Computation ---
    // Computes SMI and Total Modules for every row
    // Row 0: Total = Added (no previous row)
    // Row N: Total = previous Total + Added - Deleted
    // SMI = (Total - (Added + Changed + Deleted)) / Total
    public void computeAll() {
        for (int i = 0; i < rows.size(); i++) {
            SMIRow row = rows.get(i);

            // Compute Total Modules
            if (i == 0) {
                row.setTotalModules(row.getModulesAdded());
            } else {
                SMIRow prev = rows.get(i - 1);
                int total = prev.getTotalModules()
                        + row.getModulesAdded()
                        - row.getModulesDeleted();
                row.setTotalModules(total);
            }

            // Compute SMI
            int total = row.getTotalModules();
            if (total > 0) {
                int changed = row.getModulesAdded()
                        + row.getModulesChanged()
                        + row.getModulesDeleted();
                row.setSmi((double) (total - changed) / total);
            } else {
                row.setSmi(0.0);
            }
        }
    }
}
