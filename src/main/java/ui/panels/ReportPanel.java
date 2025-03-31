package ui.panels;

import controller.ReportController;
import ui.components.TablePanel;
import util.ExcelExporter;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ReportPanel extends TablePanel {
    private final ReportController controller = new ReportController();
    private List<?> currentData;

    public ReportPanel() {
        super("Reports Management");
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel reportsPanel = createReportsButtonPanel();

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportBtn = new JButton("Export to Excel");
        exportBtn.addActionListener(this::handleExport);
        controlPanel.add(exportBtn);

        add(reportsPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createReportsButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 5, 5));

        String[] reports = {
                "1. High-Paid Employees", "2. All Clients",
                "3. Active Bookings", "4. Bookings with Clients",
                "5. Recent Payments", "6. Credit Card Payments",
                "7. Expensive Room Types", "8. Large Capacity Rooms",
                "9. Rooms with Types", "10. Long Services",
                "11. Unused Services", "12. Manager Bookings",
                "13. Service Usage", "14. High Average Paid",
                "15. Luxury Clients", "16. Room Costs",
                "17. Payment Stats", "18. Large Rooms",
                "19. Special Amenities", "20. Service Costs",
                "21. Expensive Bookings", "22. Big Spenders",
                "23. Payment Types", "24. Special Rooms"
        };

        for (String report : reports) {
            JButton btn = new JButton(report.split("\\. ")[1]);
            btn.putClientProperty("reportId", Integer.parseInt(report.split("\\.")[0]));
            btn.addActionListener(this::handleReport);
            panel.add(btn);
        }

        return panel;
    }

    private void handleReport(ActionEvent e) {
        int reportId = (int) ((JButton) e.getSource()).getClientProperty("reportId");
        try {
            currentData = controller.getReportData(reportId);
            table.setModel(new ReportTableModel(currentData));
            table.setAutoCreateRowSorter(true);
        } catch (Exception ex) {
            showError("Error loading report: " + ex.getMessage());
        }
    }

    private void handleExport(ActionEvent e) {
        if (currentData == null || currentData.isEmpty()) {
            showError("No data to export!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".xlsx")) path += ".xlsx";

            try {
                ExcelExporter.export(table.getModel(), path);
                JOptionPane.showMessageDialog(this,
                        "Exported successfully to:\n" + path,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Export failed: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class ReportTableModel extends AbstractTableModel {
        private final List<?> data;
        private final String[] columns;
        private final Class<?>[] columnTypes;

        public ReportTableModel(List<?> data) {
            this.data = data;
            if (!data.isEmpty()) {
                Object first = data.get(0);
                java.lang.reflect.Field[] fields = first.getClass().getDeclaredFields();
                columns = new String[fields.length];
                columnTypes = new Class<?>[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    columns[i] = fields[i].getName();
                    columnTypes[i] = fields[i].getType();
                }
            } else {
                columns = new String[0];
                columnTypes = new Class<?>[0];
            }
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }
        @Override public Class<?> getColumnClass(int col) { return columnTypes[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            try {
                java.lang.reflect.Field field = data.get(row).getClass().getDeclaredField(columns[col]);
                field.setAccessible(true);
                return field.get(data.get(row));
            } catch (Exception e) {
                return "N/A";
            }
        }
    }
    @Override
    protected void onAdd() {
        // Not applicable for reports
    }

    @Override
    protected void onEdit() {
        // Not applicable for reports
    }

    @Override
    protected void onDelete() {
        // Not applicable for reports
    }
}