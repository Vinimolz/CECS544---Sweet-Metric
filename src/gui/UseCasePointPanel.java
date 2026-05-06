package gui;

import model.UseCasePointData;
import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class UseCasePointPanel extends JPanel {

    private UseCasePointData data;
    private JFrame parentFrame;

    // --- UUCW fields ---
    private JTextField simpleUCField;
    private JTextField averageUCField;
    private JTextField complexUCField;
    private JTextField uucwResultField;

    // --- UAW fields ---
    private JTextField simpleActorField;
    private JTextField averageActorField;
    private JTextField complexActorField;
    private JTextField uawResultField;

    // --- UUCP field ---
    private JTextField uucpField;

    // --- TCF / ECF result fields ---
    private JTextField tcfResultField;
    private JTextField ecfResultField;

    // --- Editable estimation fields ---
    private JTextField productivityFactorField;
    private JTextField locPerPMField;
    private JTextField locPerUCPField;

    // --- Final result fields ---
    private JTextField totalUCPField;
    private JTextField estimatedHoursField;
    private JTextField estimatedLOCField;
    private JTextField estimatedPMField;

    public UseCasePointPanel(JFrame parentFrame, UseCasePointData data) {
        this.parentFrame = parentFrame;
        this.data        = data;
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // =============================================
        // UUCW SECTION
        // =============================================
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel uucwHeader = new JLabel("Unadjusted Use Case Weight (UUCW)");
        uucwHeader.setFont(uucwHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(uucwHeader, gbc);
        gbc.gridwidth = 1;

        // Column headers
        gbc.gridy = row;
        gbc.gridx = 1; contentPanel.add(new JLabel("Count"), gbc);
        gbc.gridx = 2; contentPanel.add(new JLabel("Weight"), gbc);
        row++;

        // Simple use cases
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Simple Use Cases (≤3 transactions):"), gbc);
        gbc.gridx = 1;
        simpleUCField = new JTextField("0", 6);
        contentPanel.add(simpleUCField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.SIMPLE_UC_WEIGHT)), gbc);
        row++;

        // Average use cases
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Average Use Cases (4-7 transactions):"), gbc);
        gbc.gridx = 1;
        averageUCField = new JTextField("0", 6);
        contentPanel.add(averageUCField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.AVERAGE_UC_WEIGHT)), gbc);
        row++;

        // Complex use cases
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Complex Use Cases (>7 transactions):"), gbc);
        gbc.gridx = 1;
        complexUCField = new JTextField("0", 6);
        contentPanel.add(complexUCField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.COMPLEX_UC_WEIGHT)), gbc);
        row++;

        // UUCW result
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("UUCW:"), gbc);
        gbc.gridx = 1;
        uucwResultField = new JTextField("0", 6);
        uucwResultField.setEditable(false);
        contentPanel.add(uucwResultField, gbc);
        row++;

        // Separator
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // =============================================
        // UAW SECTION
        // =============================================
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel uawHeader = new JLabel("Unadjusted Actor Weight (UAW)");
        uawHeader.setFont(uawHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(uawHeader, gbc);
        gbc.gridwidth = 1;

        // Column headers
        gbc.gridy = row;
        gbc.gridx = 1; contentPanel.add(new JLabel("Count"), gbc);
        gbc.gridx = 2; contentPanel.add(new JLabel("Weight"), gbc);
        row++;

        // Simple actors
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Simple Actors (API/interface):"), gbc);
        gbc.gridx = 1;
        simpleActorField = new JTextField("0", 6);
        contentPanel.add(simpleActorField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.SIMPLE_ACTOR_WEIGHT)), gbc);
        row++;

        // Average actors
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Average Actors (protocol/interface):"), gbc);
        gbc.gridx = 1;
        averageActorField = new JTextField("0", 6);
        contentPanel.add(averageActorField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.AVERAGE_ACTOR_WEIGHT)), gbc);
        row++;

        // Complex actors
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Complex Actors (human via GUI):"), gbc);
        gbc.gridx = 1;
        complexActorField = new JTextField("0", 6);
        contentPanel.add(complexActorField, gbc);
        gbc.gridx = 2;
        contentPanel.add(new JLabel(
                String.valueOf(UseCasePointData.COMPLEX_ACTOR_WEIGHT)), gbc);
        row++;

        // UAW result
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("UAW:"), gbc);
        gbc.gridx = 1;
        uawResultField = new JTextField("0", 6);
        uawResultField.setEditable(false);
        contentPanel.add(uawResultField, gbc);
        row++;

        // Separator
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // =============================================
        // UUCP SECTION
        // =============================================
        gbc.gridy = row;
        gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel uucpHeader = new JLabel(
                "Unadjusted Use Case Points (UUCP = UUCW + UAW)");
        uucpHeader.setFont(uucpHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(uucpHeader, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("UUCP:"), gbc);
        gbc.gridx = 1;
        uucpField = new JTextField("0", 6);
        uucpField.setEditable(false);
        contentPanel.add(uucpField, gbc);
        row++;

        // Separator
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // =============================================
        // TCF / ECF SECTION
        // =============================================
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel tcfEcfHeader = new JLabel("Complexity Factors");
        tcfEcfHeader.setFont(tcfEcfHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(tcfEcfHeader, gbc);
        gbc.gridwidth = 1;

        // TCF button + result
        gbc.gridy = row;
        gbc.gridx = 0;
        JButton tcfButton = new JButton("Technical Complexity Factor (TCF)");
        tcfButton.addActionListener(e -> openTCFDialog());
        contentPanel.add(tcfButton, gbc);
        gbc.gridx = 1;
        tcfResultField = new JTextField("0.00", 6);
        tcfResultField.setEditable(false);
        contentPanel.add(tcfResultField, gbc);
        row++;

        // ECF button + result
        gbc.gridy = row;
        gbc.gridx = 0;
        JButton ecfButton = new JButton("Environmental Complexity Factor (ECF)");
        ecfButton.addActionListener(e -> openECFDialog());
        contentPanel.add(ecfButton, gbc);
        gbc.gridx = 1;
        ecfResultField = new JTextField("0.00", 6);
        ecfResultField.setEditable(false);
        contentPanel.add(ecfResultField, gbc);
        row++;

        // Separator
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // =============================================
        // EDITABLE ESTIMATION FIELDS
        // =============================================
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel estHeader = new JLabel("Estimation Parameters");
        estHeader.setFont(estHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(estHeader, gbc);
        gbc.gridwidth = 1;

        // Productivity Factor
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Productivity Factor (hours/UCP):"), gbc);
        gbc.gridx = 1;
        productivityFactorField = new JTextField(
                String.valueOf(data.getProductivityFactor()), 6);
        contentPanel.add(productivityFactorField, gbc);
        row++;

        // LOC per PM
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("LOC per Person-Month:"), gbc);
        gbc.gridx = 1;
        locPerPMField = new JTextField(
                String.valueOf(data.getLocPerPM()), 6);
        contentPanel.add(locPerPMField, gbc);
        row++;

        // LOC per UCP
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("LOC per UCP:"), gbc);
        gbc.gridx = 1;
        locPerUCPField = new JTextField(
                String.valueOf(data.getLocPerUCP()), 6);
        contentPanel.add(locPerUCPField, gbc);
        row++;

        // Separator
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // =============================================
        // COMPUTE BUTTON + RESULTS
        // =============================================
        gbc.gridy = row++; gbc.gridx = 0; gbc.gridwidth = 4;
        JLabel resultsHeader = new JLabel("Results");
        resultsHeader.setFont(resultsHeader.getFont().deriveFont(Font.BOLD));
        contentPanel.add(resultsHeader, gbc);
        gbc.gridwidth = 1;

        // Total UCP
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Total UCP:"), gbc);
        gbc.gridx = 1;
        totalUCPField = new JTextField("0", 6);
        totalUCPField.setEditable(false);
        contentPanel.add(totalUCPField, gbc);
        row++;

        // Estimated Hours
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Estimated Hours:"), gbc);
        gbc.gridx = 1;
        estimatedHoursField = new JTextField("0", 6);
        estimatedHoursField.setEditable(false);
        contentPanel.add(estimatedHoursField, gbc);
        row++;

        // Estimated LOC
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Estimated LOC:"), gbc);
        gbc.gridx = 1;
        estimatedLOCField = new JTextField("0", 6);
        estimatedLOCField.setEditable(false);
        contentPanel.add(estimatedLOCField, gbc);
        row++;

        // Estimated PM
        gbc.gridy = row;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Estimated Person-Months:"), gbc);
        gbc.gridx = 1;
        estimatedPMField = new JTextField("0", 6);
        estimatedPMField.setEditable(false);
        contentPanel.add(estimatedPMField, gbc);
        row++;

        // Compute button
        gbc.gridy = row;
        gbc.gridx = 0; gbc.gridwidth = 2;
        JButton computeButton = new JButton("Compute UCP");
        computeButton.addActionListener(e -> computeAll());
        contentPanel.add(computeButton, gbc);

        // Wrap in scroll pane in case window is small
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // =============================================
    // DIALOG OPENERS
    // =============================================

    private void openTCFDialog() {
        TCFDialog dialog = new TCFDialog(parentFrame, data);
        dialog.setVisible(true);
        // Update TCF display after dialog closes
        double tcf = data.computeTCF();
        tcfResultField.setText(String.format("%.2f", tcf));
    }

    private void openECFDialog() {
        ECFDialog dialog = new ECFDialog(parentFrame, data);
        dialog.setVisible(true);
        // Update ECF display after dialog closes
        double ecf = data.computeECF();
        ecfResultField.setText(String.format("%.2f", ecf));
    }

    // =============================================
    // COMPUTE
    // =============================================

    private void computeAll() {
        // Validate and save editable fields first
        if (!validateAndSaveInputs()) return;

        // Run all computations
        data.computeAll();

        // Format results
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);

        uucwResultField.setText(fmt.format(data.getUUCW()));
        uawResultField.setText(fmt.format(data.getUAW()));
        uucpField.setText(fmt.format(data.getUUCW() + data.getUAW()));
        tcfResultField.setText(fmt.format(data.getTCF()));
        ecfResultField.setText(fmt.format(data.getECF()));
        totalUCPField.setText(fmt.format(data.getTotalUCP()));
        estimatedHoursField.setText(fmt.format(data.getEstimatedHours()));
        estimatedLOCField.setText(fmt.format(data.getEstimatedLOC()));
        estimatedPMField.setText(fmt.format(data.getEstimatedPM()));
    }

    private boolean validateAndSaveInputs() {
        // Validate use case counts
        try {
            int suc = Integer.parseInt(simpleUCField.getText().trim());
            int auc = Integer.parseInt(averageUCField.getText().trim());
            int cuc = Integer.parseInt(complexUCField.getText().trim());
            if (suc < 0 || auc < 0 || cuc < 0)
                throw new NumberFormatException();
            data.setSimpleUseCases(suc);
            data.setAverageUseCases(auc);
            data.setComplexUseCases(cuc);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Use case counts must be non-negative integers.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate actor counts
        try {
            int sa = Integer.parseInt(simpleActorField.getText().trim());
            int aa = Integer.parseInt(averageActorField.getText().trim());
            int ca = Integer.parseInt(complexActorField.getText().trim());
            if (sa < 0 || aa < 0 || ca < 0)
                throw new NumberFormatException();
            data.setSimpleActors(sa);
            data.setAverageActors(aa);
            data.setComplexActors(ca);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Actor counts must be non-negative integers.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate editable estimation fields
        try {
            double pf  = Double.parseDouble(
                    productivityFactorField.getText().trim());
            double lpm = Double.parseDouble(locPerPMField.getText().trim());
            double luc = Double.parseDouble(locPerUCPField.getText().trim());
            if (pf <= 0 || lpm <= 0 || luc <= 0)
                throw new NumberFormatException();
            data.setProductivityFactor(pf);
            data.setLocPerPM(lpm);
            data.setLocPerUCP(luc);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Estimation parameters must be positive numbers.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Called by MainFrame after loading from file
    public void populateFromData() {
        simpleUCField.setText(String.valueOf(data.getSimpleUseCases()));
        averageUCField.setText(String.valueOf(data.getAverageUseCases()));
        complexUCField.setText(String.valueOf(data.getComplexUseCases()));

        simpleActorField.setText(String.valueOf(data.getSimpleActors()));
        averageActorField.setText(String.valueOf(data.getAverageActors()));
        complexActorField.setText(String.valueOf(data.getComplexActors()));

        productivityFactorField.setText(
                String.valueOf(data.getProductivityFactor()));
        locPerPMField.setText(String.valueOf(data.getLocPerPM()));
        locPerUCPField.setText(String.valueOf(data.getLocPerUCP()));

        // Only populate results if they were computed
        if (data.getTotalUCP() > 0) {
            NumberFormat fmt = NumberFormat.getNumberInstance();
            fmt.setMinimumFractionDigits(2);
            fmt.setMaximumFractionDigits(2);

            uucwResultField.setText(fmt.format(data.getUUCW()));
            uawResultField.setText(fmt.format(data.getUAW()));
            uucpField.setText(fmt.format(data.getUUCW() + data.getUAW()));
            tcfResultField.setText(fmt.format(data.getTCF()));
            ecfResultField.setText(fmt.format(data.getECF()));
            totalUCPField.setText(fmt.format(data.getTotalUCP()));
            estimatedHoursField.setText(fmt.format(data.getEstimatedHours()));
            estimatedLOCField.setText(fmt.format(data.getEstimatedLOC()));
            estimatedPMField.setText(fmt.format(data.getEstimatedPM()));
        }
    }
}
