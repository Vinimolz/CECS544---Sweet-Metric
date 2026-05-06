package gui;

import model.FunctionPointData;
import model.Language;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.NumberFormat;

public class FunctionPointPanel extends JPanel {

    private FunctionPointData data;
    private Language language;
    private JFrame parentFrame;

    // Row labels matching the document
    private static final String[] ROW_LABELS = {
            "External Inputs",
            "External Outputs",
            "External Inquiries",
            "Internal Logical Files",
            "External Interface Files"
    };

    // Input fields for counts (one per row)
    private JTextField[] countFields = new JTextField[5];

    // Radio buttons [row][0=Simple, 1=Average, 2=Complex]
    private JRadioButton[][] radioButtons = new JRadioButton[5][3];

    // Read-only row total fields
    private JTextField[] rowTotalFields = new JTextField[5];

    // Summary fields
    private JTextField totalCountField;
    private JTextField fpResultField;
    private JTextField vafSumField;
    private JTextField codeSizeField;
    private JLabel     currentLanguageLabel;

    public FunctionPointPanel(JFrame parentFrame,
                              FunctionPointData data,
                              Language language) {
        this.parentFrame = parentFrame;
        this.data        = data;
        this.language    = language;

        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        // --- Main content panel ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        // --- Weighting Factors header ---
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(new JLabel("Weighting Factors"), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 2;
        contentPanel.add(new JLabel("Simple"), gbc);
        gbc.gridx = 3;
        contentPanel.add(new JLabel("Average"), gbc);
        gbc.gridx = 4;
        contentPanel.add(new JLabel("Complex"), gbc);

        // --- Five input rows ---
        for (int row = 0; row < 5; row++) {
            gbc.gridy = row + 2;
            gbc.anchor = GridBagConstraints.WEST;

            // Column 0: row label
            gbc.gridx = 0;
            contentPanel.add(new JLabel(ROW_LABELS[row]), gbc);

            // Column 1: count input field
            gbc.gridx = 1;
            countFields[row] = new JTextField("0", 5);
            contentPanel.add(countFields[row], gbc);

            // Columns 2-4: radio buttons
            ButtonGroup group = new ButtonGroup();
            for (int col = 0; col < 3; col++) {
                gbc.gridx = col + 2;
                gbc.anchor = GridBagConstraints.CENTER;
                radioButtons[row][col] = new JRadioButton();
                // Default to Average (col=1) per requirement 4.11
                if (col == 1) radioButtons[row][col].setSelected(true);
                group.add(radioButtons[row][col]);
                contentPanel.add(radioButtons[row][col], gbc);

                // Save weight index when radio changes
                final int r = row, c = col;
                radioButtons[row][col].addActionListener(e -> {
                    data.setWeight(r, c);
                    updateRowTotal(r);
                    updateTotalCount();
                });
            }

            // Column 5: read-only row total
            gbc.gridx = 5;
            gbc.anchor = GridBagConstraints.WEST;
            rowTotalFields[row] = new JTextField("0", 6);
            rowTotalFields[row].setEditable(false);
            contentPanel.add(rowTotalFields[row], gbc);

            // Listen for count field changes
            final int r = row;
            countFields[row].addActionListener(e -> {
                updateRowTotal(r);
                updateTotalCount();
            });
            countFields[row].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent e) {
                    updateRowTotal(r);
                    updateTotalCount();
                }
            });
        }

        // --- Total Count row ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(new JLabel("Total Count"), gbc);
        gbc.gridx = 5;
        totalCountField = new JTextField("0", 6);
        totalCountField.setEditable(false);
        contentPanel.add(totalCountField, gbc);

        // --- Compute FP button + result ---
        gbc.gridy = 8; gbc.gridx = 0;
        JButton computeFPButton = new JButton("Compute FP");
        computeFPButton.addActionListener(e -> computeFP());
        contentPanel.add(computeFPButton, gbc);
        gbc.gridx = 5;
        fpResultField = new JTextField("0", 6);
        fpResultField.setEditable(false);
        contentPanel.add(fpResultField, gbc);

        // --- Value Adjustments button + VAF sum ---
        gbc.gridy = 9; gbc.gridx = 0;
        JButton vafButton = new JButton("Value Adjustments");
        vafButton.addActionListener(e -> openVAFDialog());
        contentPanel.add(vafButton, gbc);
        gbc.gridx = 5;
        vafSumField = new JTextField("0", 6);
        vafSumField.setEditable(false);
        contentPanel.add(vafSumField, gbc);

        // --- Compute Code Size button + language + result ---
        gbc.gridy = 10; gbc.gridx = 0;
        JButton codeSizeButton = new JButton("Compute Code Size");
        codeSizeButton.addActionListener(e -> computeCodeSize());
        contentPanel.add(codeSizeButton, gbc);

        gbc.gridx = 1;
        contentPanel.add(new JLabel("Current Language"), gbc);

        gbc.gridx = 2;
        currentLanguageLabel = new JLabel(language.getDisplayName());
        contentPanel.add(currentLanguageLabel, gbc);

        gbc.gridx = 5;
        codeSizeField = new JTextField("", 10);
        codeSizeField.setEditable(false);
        contentPanel.add(codeSizeField, gbc);

        // --- Change Language button ---
        gbc.gridy = 11; gbc.gridx = 0;
        JButton changeLangButton = new JButton("Change Language");
        changeLangButton.addActionListener(e -> {
            LanguageDialog dialog = new LanguageDialog(parentFrame, language);
            dialog.setVisible(true);
            currentLanguageLabel.setText(language.getDisplayName());
        });
        contentPanel.add(changeLangButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    // --- Update helpers ---

    private void updateRowTotal(int row) {
        // Validate input is non-negative integer (requirement 4.24)
        try {
            int count = Integer.parseInt(countFields[row].getText().trim());
            if (count < 0) throw new NumberFormatException();
            // Update model
            setCountInModel(row, count);
            rowTotalFields[row].setText(String.valueOf(data.getRowTotal(row)));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a non-negative integer for " + ROW_LABELS[row],
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            countFields[row].setText("0");
            setCountInModel(row, 0);
            rowTotalFields[row].setText("0");
        }
    }

    private void updateTotalCount() {
        totalCountField.setText(String.valueOf(data.getTotalCount()));
    }

    private void setCountInModel(int row, int value) {
        switch (row) {
            case 0: data.setExternalInputs(value);    break;
            case 1: data.setExternalOutputs(value);   break;
            case 2: data.setExternalInquiries(value); break;
            case 3: data.setInternalLogical(value);   break;
            case 4: data.setExternalInterface(value); break;
        }
    }

    private void computeFP() {
        double fp = data.computeFunctionPoints();
        // Format with commas and decimal per requirement 4.20
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(1);
        fmt.setMaximumFractionDigits(1);
        fpResultField.setText(fmt.format(fp));
    }

    private void openVAFDialog() {
        VAFDialog dialog = new VAFDialog(parentFrame, data);
        dialog.setVisible(true);
        // Update VAF sum display after dialog closes (requirement 4.17)
        vafSumField.setText(String.valueOf(data.getVafSum()));
    }

    private void computeCodeSize() {
        if (!language.hasLanguage()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a language first.",
                    "No Language Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        double fp = data.getFunctionPoints();
        if (fp == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please compute FP first.",
                    "No FP Value",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        double size = language.computeCodeSize(fp);
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMaximumFractionDigits(0);
        codeSizeField.setText(fmt.format(size));
    }

    // Called by MainFrame to refresh language label
    // if language changes from Preferences menu
    public void refreshLanguageLabel() {
        currentLanguageLabel.setText(language.getDisplayName());
    }

    public void populateFromData() {
        // --- Counts ---
        countFields[0].setText(String.valueOf(data.getExternalInputs()));
        countFields[1].setText(String.valueOf(data.getExternalOutputs()));
        countFields[2].setText(String.valueOf(data.getExternalInquiries()));
        countFields[3].setText(String.valueOf(data.getInternalLogical()));
        countFields[4].setText(String.valueOf(data.getExternalInterface()));

        // --- Radio buttons ---
        for (int row = 0; row < 5; row++) {
            int weight = data.getWeight(row);
            radioButtons[row][weight].setSelected(true);
        }

        // --- Row totals ---
        for (int row = 0; row < 5; row++) {
            rowTotalFields[row].setText(String.valueOf(data.getRowTotal(row)));
        }

        // --- Total count ---
        totalCountField.setText(String.valueOf(data.getTotalCount()));

        // --- FP result ---
        double fp = data.getFunctionPoints();
        if (fp > 0) {
            java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
            fmt.setMinimumFractionDigits(1);
            fmt.setMaximumFractionDigits(1);
            fpResultField.setText(fmt.format(fp));
        }

        // --- VAF sum ---
        vafSumField.setText(String.valueOf(data.getVafSum()));

        codeSizeField.setText(String.valueOf(data.getCodeSize()));

        // --- Language label ---
        currentLanguageLabel.setText(language.getDisplayName());
    }
}
