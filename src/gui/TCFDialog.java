package gui;

import model.UseCasePointData;
import javax.swing.*;
import java.awt.*;

public class TCFDialog extends JDialog {

    private UseCasePointData data;
    private JComboBox<Integer>[] comboBoxes;

    @SuppressWarnings("unchecked")
    public TCFDialog(JFrame parent, UseCasePointData data) {
        super(parent, "Technical Complexity Factors", true);
        this.data = data;
        this.comboBoxes = new JComboBox[13];
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // --- Title ---
        JLabel titleLabel = new JLabel(
                "Assign a value from 0 to 5 for each Technical Complexity Factor:");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(titleLabel, BorderLayout.NORTH);

        // --- Factors panel ---
        JPanel factorsPanel = new JPanel(new GridBagLayout());
        factorsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        // --- Column headers ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel factorHeader = new JLabel("Factor");
        factorHeader.setFont(factorHeader.getFont().deriveFont(java.awt.Font.BOLD));
        factorsPanel.add(factorHeader, gbc);

        gbc.gridx = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel weightHeader = new JLabel("Weight");
        weightHeader.setFont(weightHeader.getFont().deriveFont(java.awt.Font.BOLD));
        factorsPanel.add(weightHeader, gbc);

        gbc.gridx = 2;
        JLabel ratingHeader = new JLabel("Rating (0-5)");
        ratingHeader.setFont(ratingHeader.getFont().deriveFont(java.awt.Font.BOLD));
        factorsPanel.add(ratingHeader, gbc);

        // --- 13 factor rows ---
        for (int i = 0; i < 13; i++) {
            gbc.gridy = i + 1;

            // Factor name
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            factorsPanel.add(new JLabel(UseCasePointData.TCF_FACTORS[i]), gbc);

            // Weight (read only label)
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            JLabel weightLabel = new JLabel(
                    String.valueOf(UseCasePointData.TCF_WEIGHTS[i]));
            factorsPanel.add(weightLabel, gbc);

            // Rating combo box
            gbc.gridx = 2;
            comboBoxes[i] = new JComboBox<>(
                    new Integer[]{0, 1, 2, 3, 4, 5});
            // Restore previous value
            comboBoxes[i].setSelectedItem((int) data.getTcfRatings()[i]);
            factorsPanel.add(comboBoxes[i], gbc);
        }

        JScrollPane scrollPane = new JScrollPane(factorsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 380));
        add(scrollPane, BorderLayout.CENTER);

        // --- Buttons ---
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            for (int i = 0; i < 13; i++) {
                data.setTcfRating(i,
                        (Integer) comboBoxes[i].getSelectedItem());
            }
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(doneButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}