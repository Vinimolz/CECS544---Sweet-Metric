package model;

import java.util.ArrayList;
import java.util.List;

public class Project {

    private String projectName;
    private String productName;
    private String creatorName;
    private String comments;
    private SMIData smiData = new SMIData();

    // Will hold FP pane data later
    private List<FunctionPointData> functionPointDataList = new ArrayList<>();
    private List<UseCasePointData> useCasePointDataList = new ArrayList<>();

    public Project(String projectName, String productName,
            String creatorName, String comments) {
        this.projectName = projectName;
        this.productName = productName;
        this.creatorName = creatorName;
        this.comments = comments;
    }

    // Getters
    public String getProjectName() {
        return projectName;
    }

    public String getProductName() {
        return productName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getComments() {
        return comments;
    }

    public SMIData getSmiData() {
        return smiData;
    }

    public void setSmiData(SMIData smiData) {
        this.smiData = smiData;
    }

    // FP data management
    public void addFunctionPointData(FunctionPointData data) {
        functionPointDataList.add(data);
    }

    public List<FunctionPointData> getFunctionPointDataList() {
        return functionPointDataList;
    }

    public void addUseCasePointData(UseCasePointData data) {
        useCasePointDataList.add(data);
    }

    public List<UseCasePointData> getUseCasePointDataList() {
        return useCasePointDataList;
    }
}
