package gui;

import model.SMIData;
import model.SMIData.SMIRow;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SMIPanel extends JPanel {

    private SMIData data;
    private DefaultTableModel tableModel;
    private JTable table;

    // Column indices for clarity
    private static final int COL_SMI = 0;
    private static final int COL_ADDED = 1;
    private static final int COL_CHANGED = 2;
    private static final int COL_DELETED = 3;
    private static final int COL_TOTAL = 4;

    private static final String[] COLUMN_NAMES = {
            "SMI", "Modules Added", "Modules Changed",
            "Modules Deleted", "Total Modules"
    };

    public SMIPanel(SMIData data) {
        this.data = data;
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        // --- Title ---
        JLabel titleLabel = new JLabel("Software Maturity Index");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Table ---
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Added, Changed, Deleted are editable
                // SMI and Total are computed
                return column == COL_ADDED
                        || column == COL_CHANGED
                        || column == COL_DELETED;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(22);

        // Set column widths to match the document
        table.getColumnModel().getColumn(COL_SMI).setPreferredWidth(120);
        table.getColumnModel().getColumn(COL_ADDED).setPreferredWidth(100);
        table.getColumnModel().getColumn(COL_CHANGED).setPreferredWidth(110);
        table.getColumnModel().getColumn(COL_DELETED).setPreferredWidth(110);
        table.getColumnModel().getColumn(COL_TOTAL).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        add(scrollPane, BorderLayout.CENTER);

        // --- Buttons ---
        JButton addRowButton = new JButton("Add Row");
        JButton computeButton = new JButton("Compute Index");

        addRowButton.addActionListener(e -> addRow());
        computeButton.addActionListener(e -> computeIndex());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 40, 10, 0));
        buttonPanel.add(addRowButton);
        buttonPanel.add(computeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- Add a new empty row ---
    private void addRow() {
        // Stop any current cell editing before adding
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        // Add empty row to table model
        tableModel.addRow(new Object[] { "", "0", "0", "0", "" });

        // Add corresponding row to data model
        data.addRow(new SMIRow(0, 0, 0));
    }

    // --- Compute SMI for all rows ---
    private void computeIndex() {
        // Stop any current cell editing first
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one row first.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Read editable values from table into data model
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int added = Integer.parseInt(
                        tableModel.getValueAt(i, COL_ADDED).toString().trim());
                int changed = Integer.parseInt(
                        tableModel.getValueAt(i, COL_CHANGED).toString().trim());
                int deleted = Integer.parseInt(
                        tableModel.getValueAt(i, COL_DELETED).toString().trim());

                if (added < 0 || changed < 0 || deleted < 0)
                    throw new NumberFormatException();

                data.getRow(i).setModulesAdded(added);
                data.getRow(i).setModulesChanged(changed);
                data.getRow(i).setModulesDeleted(deleted);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Row " + (i + 1) + ": values must be " +
                                "non-negative integers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Run computation on model
        data.computeAll();

        // Push computed results back to table
        for (int i = 0; i < data.getRowCount(); i++) {
            SMIRow row = data.getRow(i);
            tableModel.setValueAt(row.getSmi(), i, COL_SMI);
            tableModel.setValueAt(row.getTotalModules(), i, COL_TOTAL);
        }
    }

    // --- Populate table from loaded data (called after File -> Open) ---
    public void populateFromData() {
        tableModel.setRowCount(0); // clear existing rows first

        for (int i = 0; i < data.getRowCount(); i++) {
            SMIRow row = data.getRow(i);
            tableModel.addRow(new Object[] {
                    row.getSmi(),
                    row.getModulesAdded(),
                    row.getModulesChanged(),
                    row.getModulesDeleted(),
                    row.getTotalModules()
            });
        }
    }
}