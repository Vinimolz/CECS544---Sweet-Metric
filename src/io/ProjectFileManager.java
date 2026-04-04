package io;

import model.FunctionPointData;
import model.Language;
import model.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectFileManager {

    // --- SAVE ---
    public static void saveProject(Project project, Language language, File file)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Project info
        writer.write("PROJECT_NAME=" + project.getProjectName()); writer.newLine();
        writer.write("PRODUCT_NAME=" + project.getProductName()); writer.newLine();
        writer.write("CREATOR="      + project.getCreatorName()); writer.newLine();
        writer.write("COMMENTS="     + project.getComments());    writer.newLine();
        writer.write("LANGUAGE="     + language.getDisplayName()); writer.newLine();
        writer.newLine();

        // Each FP pane
        for (FunctionPointData data : project.getFunctionPointDataList()) {
            writer.write("PANE="    + data.getPaneName());         writer.newLine();
            writer.write("EI="      + data.getExternalInputs());   writer.newLine();
            writer.write("EO="      + data.getExternalOutputs());  writer.newLine();
            writer.write("EQ="      + data.getExternalInquiries());writer.newLine();
            writer.write("ILF="     + data.getInternalLogical());  writer.newLine();
            writer.write("EIF="     + data.getExternalInterface()); writer.newLine();

            // Weighting factors
            writer.write("WEIGHT_EI="  + data.getWeight(0)); writer.newLine();
            writer.write("WEIGHT_EO="  + data.getWeight(1)); writer.newLine();
            writer.write("WEIGHT_EQ="  + data.getWeight(2)); writer.newLine();
            writer.write("WEIGHT_ILF=" + data.getWeight(3)); writer.newLine();
            writer.write("WEIGHT_EIF=" + data.getWeight(4)); writer.newLine();

            // VAF values as comma separated
            int[] vafs = data.getVafValues();
            StringBuilder vafLine = new StringBuilder("VAF=");
            for (int i = 0; i < vafs.length; i++) {
                vafLine.append(vafs[i]);
                if (i < vafs.length - 1) vafLine.append(",");
            }
            writer.write(vafLine.toString()); writer.newLine();

            // FP result
            writer.write("FP_RESULT=" + data.getFunctionPoints()); writer.newLine();
            writer.newLine(); // blank line between panes
        }

        writer.close();
    }

    // --- LOAD ---
    public static Project loadProject(File file, Language language)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String projectName = "";
        String productName = "";
        String creator     = "";
        String comments    = "";

        List<FunctionPointData> panes = new ArrayList<>();
        FunctionPointData currentPane = null;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("=", 2); // split on first = only
            if (parts.length < 2) continue;

            String key   = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                // Project level
                case "PROJECT_NAME": projectName = value; break;
                case "PRODUCT_NAME": productName = value; break;
                case "CREATOR":      creator     = value; break;
                case "COMMENTS":     comments    = value; break;
                case "LANGUAGE":
                    // Restore language - "None" means no language selected
                    if (!value.equals("None")) {
                        language.setSelectedLanguage(value);
                    }
                    break;

                // Pane level - new pane starts here
                case "PANE":
                    currentPane = new FunctionPointData(value);
                    panes.add(currentPane);
                    break;

                // FP counts
                case "EI":
                    if (currentPane != null)
                        currentPane.setExternalInputs(Integer.parseInt(value));
                    break;
                case "EO":
                    if (currentPane != null)
                        currentPane.setExternalOutputs(Integer.parseInt(value));
                    break;
                case "EQ":
                    if (currentPane != null)
                        currentPane.setExternalInquiries(Integer.parseInt(value));
                    break;
                case "ILF":
                    if (currentPane != null)
                        currentPane.setInternalLogical(Integer.parseInt(value));
                    break;
                case "EIF":
                    if (currentPane != null)
                        currentPane.setExternalInterface(Integer.parseInt(value));
                    break;

                // Weighting factors
                case "WEIGHT_EI":
                    if (currentPane != null)
                        currentPane.setWeight(0, Integer.parseInt(value));
                    break;
                case "WEIGHT_EO":
                    if (currentPane != null)
                        currentPane.setWeight(1, Integer.parseInt(value));
                    break;
                case "WEIGHT_EQ":
                    if (currentPane != null)
                        currentPane.setWeight(2, Integer.parseInt(value));
                    break;
                case "WEIGHT_ILF":
                    if (currentPane != null)
                        currentPane.setWeight(3, Integer.parseInt(value));
                    break;
                case "WEIGHT_EIF":
                    if (currentPane != null)
                        currentPane.setWeight(4, Integer.parseInt(value));
                    break;

                // VAF values
                case "VAF":
                    if (currentPane != null) {
                        String[] vafParts = value.split(",");
                        for (int i = 0; i < vafParts.length; i++) {
                            currentPane.setVafValue(i,
                                    Integer.parseInt(vafParts[i].trim()));
                        }
                    }
                    break;

                // FP result
                case "FP_RESULT":
                    if (currentPane != null) {
                        double fp = Double.parseDouble(value);
                        currentPane.computeFunctionPoints();
                        // Override with saved value in case of rounding
                        if (fp > 0) currentPane.setFunctionPoints(fp);
                    }
                    break;
            }
        }

        reader.close();

        // Build and return the project
        Project project = new Project(projectName, productName,
                creator, comments);
        for (FunctionPointData pane : panes) {
            project.addFunctionPointData(pane);
        }
        return project;
    }
}
