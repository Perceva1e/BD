package ui.panels;

import controller.BackUpController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class BackupPanel extends JPanel {
    private final BackUpController controller;
    private final JFileChooser fileChooser;

    public BackupPanel() {
        super(new BorderLayout());
        this.controller = new BackUpController();
        this.fileChooser = new JFileChooser();
        initUI();
    }

    private void initUI() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnCreate = new JButton("Create Backup");
        btnCreate.addActionListener(this::handleCreateBackup);

        JButton btnRestore = new JButton("Restore Backup");
        btnRestore.addActionListener(this::handleRestoreBackup);

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRestore);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void handleCreateBackup(ActionEvent e) {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.createBackup(file.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Backup created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error creating backup: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRestoreBackup(ActionEvent e) {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                controller.restoreBackup(file.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Backup restored successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error restoring backup: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}