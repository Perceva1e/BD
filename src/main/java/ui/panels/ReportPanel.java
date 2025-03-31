package ui.panels;

import controller.ReportController;
import model.SavedQuery;
import ui.components.TablePanel;
import ui.dialogs.QueryEditorDialog;
import util.ExcelExporter;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ReportPanel extends TablePanel {
    private final ReportController controller = new ReportController();
    private List<?> currentData;
    private DefaultListModel<SavedQuery> queryListModel = new DefaultListModel<>();
    private JList<SavedQuery> queryList = new JList<>(queryListModel);

    public ReportPanel() {
        super("Reports Management");
        initializeUI();
        loadSavedQueries();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createSavedQueriesPanel(), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(300, 400));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createReportsButtonPanel(), BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(createExportPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSavedQueriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        queryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        queryList.addMouseListener(new QueryListMouseAdapter());

        JToolBar queryToolbar = new JToolBar();
        queryToolbar.add(createToolButton("New", e -> createNewQuery()));
        queryToolbar.add(createToolButton("Delete", e -> deleteSelectedQuery()));

        panel.add(new JScrollPane(queryList), BorderLayout.CENTER);
        panel.add(queryToolbar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createReportsButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 5, 5));

        String[] reports = {
                "High-Paid Employees", "All Clients",
                "Active Bookings", "Bookings with Clients",
                "Recent Payments", "Credit Card Payments",
                "Expensive Room Types", "Large Capacity Rooms",
                "Rooms with Types", "Long Services",
                "Unused Services", "Manager Bookings",
                "Service Usage", "High Average Paid",
                "Luxury Clients", "Room Costs",
                "Payment Stats", "Large Rooms",
                "Special Amenities", "Service Costs",
                "Expensive Bookings", "Big Spenders",
                "Payment Types", "Special Rooms"
        };

        for (String report : reports) {
            JButton btn = new JButton(report);
            btn.addActionListener(e -> handleStandardReport(report));
            panel.add(btn);
        }

        return panel;
    }

    private void handleStandardReport(String reportName) {
        try {
            int reportId = getReportIdByName(reportName);
            currentData = controller.getReportData(reportId);
            table.setModel(new ReportTableModel(currentData));
            table.setAutoCreateRowSorter(true);
        } catch (Exception ex) {
            showError("Error loading report: " + ex.getMessage());
        }
    }

    private int getReportIdByName(String name) {
        String[] reportNames = {
                "High-Paid Employees", "All Clients", "Active Bookings", "Bookings with Clients",
                "Recent Payments", "Credit Card Payments", "Expensive Room Types", "Large Capacity Rooms",
                "Rooms with Types", "Long Services", "Unused Services", "Manager Bookings",
                "Service Usage", "High Average Paid", "Luxury Clients", "Room Costs",
                "Payment Stats", "Large Rooms", "Special Amenities", "Service Costs",
                "Expensive Bookings", "Big Spenders", "Payment Types", "Special Rooms"
        };

        for (int i = 0; i < reportNames.length; i++) {
            if (reportNames[i].equals(name)) return i + 1;
        }
        return -1;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportBtn = new JButton("Export to Excel");
        exportBtn.addActionListener(this::handleExport);
        panel.add(exportBtn);
        return panel;
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

    private JButton createToolButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.addActionListener(listener);
        btn.setMargin(new Insets(2, 5, 2, 5));
        return btn;
    }

    private void loadSavedQueries() {
        try {
            queryListModel.clear();
            controller.getSavedQueries().forEach(queryListModel::addElement);
        } catch (SQLException e) {
            showError("Error loading queries: " + e.getMessage());
        }
    }

    private void createNewQuery() {
        QueryEditorDialog dialog = new QueryEditorDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        SavedQuery query = dialog.getQuery();
        if (query != null) {
            try {
                controller.saveQuery(query);
                loadSavedQueries();
            } catch (SQLException e) {
                showError("Error saving query: " + e.getMessage());
            }
        }
    }

    private void deleteSelectedQuery() {
        SavedQuery query = queryList.getSelectedValue();
        if (query != null) {
            try {
                controller.deleteQuery(query.getId());
                queryListModel.removeElement(query);
            } catch (SQLException e) {
                showError("Error deleting query: " + e.getMessage());
            }
        }
    }

    private class QueryListMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                SavedQuery query = queryList.getSelectedValue();
                if (query != null) {
                    try {
                        currentData = controller.executeCustomQuery(query.getSql());
                        table.setModel(new ReportTableModel(currentData));

                        table.revalidate();
                        table.repaint();
                    } catch (SQLException ex) {
                        showError("Ошибка выполнения: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private static class ReportTableModel extends AbstractTableModel {
        private final List<?> data;
        private final String[] columns;
        private final Class<?>[] columnTypes;

        public ReportTableModel(List<?> data) {
            this.data = data;

            if (!data.isEmpty() && data.get(0) instanceof Object[]) {
                Object[] firstRow = (Object[]) data.get(0);
                columns = new String[firstRow.length];
                columnTypes = new Class<?>[firstRow.length];

                for (int i = 0; i < firstRow.length; i++) {
                    columns[i] = "Column " + (i + 1);
                    columnTypes[i] = firstRow[i] != null ? firstRow[i].getClass() : Object.class;
                }
            } else if (!data.isEmpty() && data.get(0) != null) {
                java.lang.reflect.Field[] fields = data.get(0).getClass().getDeclaredFields();
                columns = new String[fields.length];
                columnTypes = new Class<?>[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    columns[i] = fields[i].getName();
                    columnTypes[i] = fields[i].getType();
                }
            } else {
                columns = new String[]{"No data"};
                columnTypes = new Class<?>[]{String.class};
            }
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }
        @Override public Class<?> getColumnClass(int col) { return columnTypes[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Object item = data.get(row);
            if (item instanceof Object[]) {
                return ((Object[]) item)[col];
            }

            try {
                java.lang.reflect.Field field = item.getClass().getDeclaredField(columns[col]);
                field.setAccessible(true);
                return field.get(item);
            } catch (Exception e) {
                return "N/A";
            }
        }
    }

    @Override
    protected void onAdd() {} // Не используется

    @Override
    protected void onEdit() {} // Не используется

    @Override
    protected void onDelete() {} // Не используется

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}