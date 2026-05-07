package io;

import model.FunctionPointData;
import model.Language;
import model.Project;
import model.SMIData;
import model.UseCasePointData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectFileManager {

    // --- SAVE ---
    public static void saveProject(Project project, Language language, File file)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Project info
        writer.write("PROJECT_NAME=" + project.getProjectName());
        writer.newLine();
        writer.write("PRODUCT_NAME=" + project.getProductName());
        writer.newLine();
        writer.write("CREATOR=" + project.getCreatorName());
        writer.newLine();
        writer.write("COMMENTS=" + project.getComments());
        writer.newLine();
        writer.write("LANGUAGE=" + language.getDisplayName());
        writer.newLine();
        writer.newLine();

        // Each FP pane
        for (FunctionPointData data : project.getFunctionPointDataList()) {
            writer.write("PANE=" + data.getPaneName());
            writer.newLine();
            writer.write("EI=" + data.getExternalInputs());
            writer.newLine();
            writer.write("EO=" + data.getExternalOutputs());
            writer.newLine();
            writer.write("EQ=" + data.getExternalInquiries());
            writer.newLine();
            writer.write("ILF=" + data.getInternalLogical());
            writer.newLine();
            writer.write("EIF=" + data.getExternalInterface());
            writer.newLine();

            // Weighting factors
            writer.write("WEIGHT_EI=" + data.getWeight(0));
            writer.newLine();
            writer.write("WEIGHT_EO=" + data.getWeight(1));
            writer.newLine();
            writer.write("WEIGHT_EQ=" + data.getWeight(2));
            writer.newLine();
            writer.write("WEIGHT_ILF=" + data.getWeight(3));
            writer.newLine();
            writer.write("WEIGHT_EIF=" + data.getWeight(4));
            writer.newLine();

            // VAF values as comma separated
            int[] vafs = data.getVafValues();
            StringBuilder vafLine = new StringBuilder("VAF=");
            for (int i = 0; i < vafs.length; i++) {
                vafLine.append(vafs[i]);
                if (i < vafs.length - 1)
                    vafLine.append(",");
            }
            writer.write(vafLine.toString());
            writer.newLine();

            // FP result
            writer.write("FP_RESULT=" + data.getFunctionPoints());
            writer.newLine();
            writer.write("CODE_SIZE=" + data.getCodeSize());
            writer.newLine();
            writer.newLine(); // blank line between panes
        }

        // Save UCP panes
        for (UseCasePointData data : project.getUseCasePointDataList()) {
            writer.write("UCP_PANE=" + data.getPaneName());
            writer.newLine();
            writer.write("SIMPLE_UC=" + data.getSimpleUseCases());
            writer.newLine();
            writer.write("AVERAGE_UC=" + data.getAverageUseCases());
            writer.newLine();
            writer.write("COMPLEX_UC=" + data.getComplexUseCases());
            writer.newLine();
            writer.write("SIMPLE_ACTOR=" + data.getSimpleActors());
            writer.newLine();
            writer.write("AVERAGE_ACTOR=" + data.getAverageActors());
            writer.newLine();
            writer.write("COMPLEX_ACTOR=" + data.getComplexActors());
            writer.newLine();
            writer.write("PROD_FACTOR=" + data.getProductivityFactor());
            writer.newLine();
            writer.write("LOC_PM=" + data.getLocPerPM());
            writer.newLine();
            writer.write("LOC_UCP=" + data.getLocPerUCP());
            writer.newLine();

            // TCF ratings as comma separated
            double[] tcf = data.getTcfRatings();
            StringBuilder tcfLine = new StringBuilder("TCF_RATINGS=");
            for (int i = 0; i < tcf.length; i++) {
                tcfLine.append(tcf[i]);
                if (i < tcf.length - 1)
                    tcfLine.append(",");
            }
            writer.write(tcfLine.toString());
            writer.newLine();

            // ECF ratings as comma separated
            double[] ecf = data.getEcfRatings();
            StringBuilder ecfLine = new StringBuilder("ECF_RATINGS=");
            for (int i = 0; i < ecf.length; i++) {
                ecfLine.append(ecf[i]);
                if (i < ecf.length - 1)
                    ecfLine.append(",");
            }
            writer.write(ecfLine.toString());
            writer.newLine();

            writer.write("TOTAL_UCP=" + data.getTotalUCP());
            writer.newLine();
            writer.write("EST_HOURS=" + data.getEstimatedHours());
            writer.newLine();
            writer.write("EST_LOC=" + data.getEstimatedLOC());
            writer.newLine();
            writer.write("EST_PM=" + data.getEstimatedPM());
            writer.newLine();
            writer.newLine();
        }

        // Save SMI data
        SMIData smiData = project.getSmiData();
        if (smiData.getRowCount() > 0) {
            writer.write("SMI_ROWS=" + smiData.getRowCount());
            writer.newLine();
            for (int i = 0; i < smiData.getRowCount(); i++) {
                SMIData.SMIRow row = smiData.getRow(i);
                writer.write("SMI_ROW=" + i
                        + "," + row.getModulesAdded()
                        + "," + row.getModulesChanged()
                        + "," + row.getModulesDeleted()
                        + "," + row.getTotalModules()
                        + "," + row.getSmi());
                writer.newLine();
            }
            writer.newLine();
        }

        writer.close();
    }

    // --- LOAD ---
    public static Project loadProject(File file, Language language)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        SMIData smiData = new SMIData();
        String projectName = "";
        String productName = "";
        String creator = "";
        String comments = "";

        List<FunctionPointData> fpPanes = new ArrayList<>();
        List<UseCasePointData> ucpPanes = new ArrayList<>();

        FunctionPointData currentFP = null;
        UseCasePointData currentUCP = null;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split("=", 2);
            if (parts.length < 2)
                continue;

            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                // --- Project level ---
                case "PROJECT_NAME":
                    projectName = value;
                    break;
                case "PRODUCT_NAME":
                    productName = value;
                    break;
                case "CREATOR":
                    creator = value;
                    break;
                case "COMMENTS":
                    comments = value;
                    break;
                case "LANGUAGE":
                    if (!value.equals("None")) {
                        language.setSelectedLanguage(value);
                    }
                    break;

                // --- FP pane start ---
                case "PANE":
                    currentFP = new FunctionPointData(value);
                    currentUCP = null;
                    fpPanes.add(currentFP);
                    break;

                // --- FP fields ---
                case "EI":
                    if (currentFP != null)
                        currentFP.setExternalInputs(Integer.parseInt(value));
                    break;
                case "EO":
                    if (currentFP != null)
                        currentFP.setExternalOutputs(Integer.parseInt(value));
                    break;
                case "EQ":
                    if (currentFP != null)
                        currentFP.setExternalInquiries(Integer.parseInt(value));
                    break;
                case "ILF":
                    if (currentFP != null)
                        currentFP.setInternalLogical(Integer.parseInt(value));
                    break;
                case "EIF":
                    if (currentFP != null)
                        currentFP.setExternalInterface(Integer.parseInt(value));
                    break;
                case "WEIGHT_EI":
                    if (currentFP != null)
                        currentFP.setWeight(0, Integer.parseInt(value));
                    break;
                case "WEIGHT_EO":
                    if (currentFP != null)
                        currentFP.setWeight(1, Integer.parseInt(value));
                    break;
                case "WEIGHT_EQ":
                    if (currentFP != null)
                        currentFP.setWeight(2, Integer.parseInt(value));
                    break;
                case "WEIGHT_ILF":
                    if (currentFP != null)
                        currentFP.setWeight(3, Integer.parseInt(value));
                    break;
                case "WEIGHT_EIF":
                    if (currentFP != null)
                        currentFP.setWeight(4, Integer.parseInt(value));
                    break;
                case "VAF":
                    if (currentFP != null) {
                        String[] vafParts = value.split(",");
                        for (int i = 0; i < vafParts.length; i++) {
                            currentFP.setVafValue(i,
                                    Integer.parseInt(vafParts[i].trim()));
                        }
                    }
                    break;
                case "FP_RESULT":
                    if (currentFP != null) {
                        double fp = Double.parseDouble(value);
                        currentFP.computeFunctionPoints();
                        if (fp > 0)
                            currentFP.setFunctionPoints(fp);
                    }
                    break;
                case "CODE_SIZE":
                    if (currentFP != null) {
                        double codeSize = Double.parseDouble(value);
                        currentFP.setCodeSize(codeSize);
                    }
                    break;

                // --- UCP pane start ---
                case "UCP_PANE":
                    currentUCP = new UseCasePointData(value);
                    currentFP = null;
                    ucpPanes.add(currentUCP);
                    break;

                // --- UCP fields ---
                case "SIMPLE_UC":
                    if (currentUCP != null)
                        currentUCP.setSimpleUseCases(Integer.parseInt(value));
                    break;
                case "AVERAGE_UC":
                    if (currentUCP != null)
                        currentUCP.setAverageUseCases(Integer.parseInt(value));
                    break;
                case "COMPLEX_UC":
                    if (currentUCP != null)
                        currentUCP.setComplexUseCases(Integer.parseInt(value));
                    break;
                case "SIMPLE_ACTOR":
                    if (currentUCP != null)
                        currentUCP.setSimpleActors(Integer.parseInt(value));
                    break;
                case "AVERAGE_ACTOR":
                    if (currentUCP != null)
                        currentUCP.setAverageActors(Integer.parseInt(value));
                    break;
                case "COMPLEX_ACTOR":
                    if (currentUCP != null)
                        currentUCP.setComplexActors(Integer.parseInt(value));
                    break;
                case "PROD_FACTOR":
                    if (currentUCP != null)
                        currentUCP.setProductivityFactor(
                                Double.parseDouble(value));
                    break;
                case "LOC_PM":
                    if (currentUCP != null)
                        currentUCP.setLocPerPM(Double.parseDouble(value));
                    break;
                case "LOC_UCP":
                    if (currentUCP != null)
                        currentUCP.setLocPerUCP(Double.parseDouble(value));
                    break;
                case "TCF_RATINGS":
                    if (currentUCP != null) {
                        String[] tcfParts = value.split(",");
                        for (int i = 0; i < tcfParts.length; i++) {
                            currentUCP.setTcfRating(i,
                                    Double.parseDouble(tcfParts[i].trim()));
                        }
                    }
                    break;
                case "ECF_RATINGS":
                    if (currentUCP != null) {
                        String[] ecfParts = value.split(",");
                        for (int i = 0; i < ecfParts.length; i++) {
                            currentUCP.setEcfRating(i,
                                    Double.parseDouble(ecfParts[i].trim()));
                        }
                    }
                    break;
                case "TOTAL_UCP":
                    if (currentUCP != null)
                        currentUCP.computeAll();
                    break;
                case "SMI_ROW":
                    String[] smiParts = value.split(",");
                    if (smiParts.length == 6) {
                        SMIData.SMIRow smiRow = new SMIData.SMIRow(
                                Integer.parseInt(smiParts[1].trim()),
                                Integer.parseInt(smiParts[2].trim()),
                                Integer.parseInt(smiParts[3].trim()));
                        smiRow.setTotalModules(Integer.parseInt(smiParts[4].trim()));
                        smiRow.setSmi(Double.parseDouble(smiParts[5].trim()));
                        smiData.addRow(smiRow); // use local variable, not project
                    }
                    break;
            }
        }

        reader.close();

        Project project = new Project(projectName, productName,
                creator, comments);
        for (FunctionPointData fp : fpPanes)
            project.addFunctionPointData(fp);
        for (UseCasePointData ucp : ucpPanes)
            project.addUseCasePointData(ucp);
        project.setSmiData(smiData);

        return project;
    }
}
