package gui;

import model.Language;
import model.FunctionPointData;
import model.Project;
import model.UseCasePointData;
import io.ProjectFileManager;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import gui.SMIPanel;
import model.SMIData;

public class MainFrame extends JFrame {

    private Project currentProject = null;
    private Language language = new Language();
    private JTabbedPane tabbedPane;
    private JPanel leftPane;
    private JPanel rightPane;
    private JSplitPane splitPane;
    private SMIPanel smiPanel = null;

    public MainFrame() {
        setTitle("CECS 544 Metrics Suite");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centers the window on screen
        buildMenuBar();
        buildMainLayout();
    }

    private void buildMainLayout() {
        leftPane = new JPanel();
        leftPane.setPreferredSize(new Dimension(150, 0));

        tabbedPane = new JTabbedPane();
        rightPane = new JPanel(new BorderLayout());
        rightPane.add(tabbedPane, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPane, rightPane);
        splitPane.setDividerLocation(150);
        splitPane.setDividerSize(3);
        add(splitPane);
    }

    private void addFunctionPointPane() {
        System.out.println("addFunctionPointPane called");
        System.out.println("currentProject = " + currentProject);

        if (currentProject == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create a project first via File -> New.",
                    "No Project Open",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Past null check");

        String paneName = "FP " + this.language.getSelectedLanguage();
        System.out.println("paneName = " + paneName);

        FunctionPointData data = new FunctionPointData(paneName);
        System.out.println("data created");

        currentProject.addFunctionPointData(data);
        System.out.println("data added to project");

        FunctionPointPanel panel = new FunctionPointPanel(this, data, language);
        System.out.println("panel created");

        tabbedPane.addTab(paneName, panel);
        System.out.println("tab added");

        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        System.out.println("tab selected");
    }

    private void addUseCasePointPane() {
        if (currentProject == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create a project first via File -> New.",
                    "No Project Open",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String paneName = "Test " + (tabbedPane.getTabCount() + 1);
        UseCasePointData data = new UseCasePointData(paneName);
        currentProject.addUseCasePointData(data);
        UseCasePointPanel panel = new UseCasePointPanel(this, data);
        tabbedPane.addTab(paneName, panel);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private void addSMIPane() {
        if (currentProject == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create a project first via File -> New.",
                    "No Project Open",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Only one SMI panel per project
        if (smiPanel != null) {
            JOptionPane.showMessageDialog(this,
                    "An SMI panel already exists for this project.",
                    "SMI Already Open",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        smiPanel = new SMIPanel(currentProject.getSmiData());
        tabbedPane.addTab("SMI", smiPanel);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> createNewProject());

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> openProject());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveProject());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit menu (empty for now)
        JMenu editMenu = new JMenu("Edit");

        // Preferences menu
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem languageItem = new JMenuItem("Language");
        languageItem.addActionListener(e -> openLanguageDialog());
        preferencesMenu.add(languageItem);

        // Metrics menu
        JMenu metricsMenu = new JMenu("Metrics");
        JMenu fpMenu = new JMenu("Function Points");
        JMenuItem enterFPItem = new JMenuItem("Enter FP Data");
        enterFPItem.addActionListener(e -> addFunctionPointPane());
        fpMenu.add(enterFPItem);

        JMenu ucpMenu = new JMenu("Use Case Points");
        JMenuItem enterUCPItem = new JMenuItem("Enter UCP Data");
        enterUCPItem.addActionListener(e -> addUseCasePointPane());
        ucpMenu.add(enterUCPItem);

        JMenu smiMenu = new JMenu("SMI");
        JMenuItem enterSMIItem = new JMenuItem("Software Maturity Index");
        enterSMIItem.addActionListener(e -> addSMIPane());
        smiMenu.add(enterSMIItem);
        metricsMenu.add(smiMenu);

        metricsMenu.add(fpMenu);
        metricsMenu.add(ucpMenu);

        // Help menu (empty for now)
        JMenu helpMenu = new JMenu("Help");

        // Add all menus to the bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(metricsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createNewProject() {
        NewProjectDialog dialog = new NewProjectDialog(this);
        dialog.setVisible(true);

        Project result = dialog.getProject();
        if (result != null) {
            currentProject = result;
            // Clear tabs from tabbedPane instead of removing everything
            tabbedPane.removeAll();
            // Make sure tabbedPane is still in rightPane
            rightPane.removeAll();
            rightPane.add(tabbedPane, BorderLayout.CENTER);
            rightPane.revalidate();
            rightPane.repaint();
            smiPanel = null;
            // Update title bar (requirement 4.37)
            setTitle("CECS 544 Metrics Suite - " + currentProject.getProjectName());
        }
    }

    private void openLanguageDialog() {
        LanguageDialog dialog = new LanguageDialog(this, language);
        dialog.setVisible(true);
        // After dialog closes, language.getSelectedLanguage() has the saved value
        System.out.println("Selected: " + language.getSelectedLanguage()); // test it
    }

    private void saveProject() {
        if (currentProject == null) {
            JOptionPane.showMessageDialog(this,
                    "No project open to save.",
                    "Nothing to Save",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // File chooser defaulting to .ms files (requirement 4.32)
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Metrics Suite Files (*.ms)", "ms"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Auto append .ms extension if not present (requirement 4.31)
            if (!file.getName().endsWith(".ms")) {
                file = new File(file.getAbsolutePath() + ".ms");
            }

            try {
                ProjectFileManager.saveProject(currentProject, language, file);
                JOptionPane.showMessageDialog(this,
                        "Project saved successfully!",
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving project: " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openProject() {
        // File chooser defaulting to .ms files (requirement 4.32)
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Metrics Suite Files (*.ms)", "ms"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Load project and restore language (requirement 4.33)
                Project loaded = ProjectFileManager.loadProject(file, language);
                currentProject = loaded;

                // Clear UI and rebuild tabs from loaded data
                tabbedPane.removeAll();
                rightPane.removeAll();
                rightPane.add(tabbedPane, BorderLayout.CENTER);

                // Recreate FP panes from loaded data (requirement 4.33)
                for (FunctionPointData data : currentProject.getFunctionPointDataList()) {
                    FunctionPointPanel panel = new FunctionPointPanel(this, data, language);
                    panel.populateFromData();
                    tabbedPane.addTab(data.getPaneName(), panel);
                }

                // Restore UCP panes
                for (UseCasePointData data : currentProject.getUseCasePointDataList()) {
                    UseCasePointPanel panel = new UseCasePointPanel(this, data);
                    panel.populateFromData();
                    tabbedPane.addTab(data.getPaneName(), panel);
                }

                if (currentProject.getSmiData().getRowCount() > 0) {
                    smiPanel = new SMIPanel(currentProject.getSmiData());
                    smiPanel.populateFromData();
                    tabbedPane.addTab("SMI", smiPanel);
                }

                rightPane.revalidate();
                rightPane.repaint();

                // Update title bar
                setTitle("CECS 544 Metrics Suite - " +
                        currentProject.getProjectName());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error opening project: " + e.getMessage(),
                        "Open Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
