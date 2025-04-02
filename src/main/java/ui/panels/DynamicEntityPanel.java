package ui.panels;

import controller.EntityController;
import model.DynamicEntity;
import ui.components.TablePanel;
import ui.dialogs.DynamicRecordFormDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DynamicEntityPanel extends TablePanel {
    private final EntityController controller;
    private final DynamicEntity entity;

    public DynamicEntityPanel(DynamicEntity entity, EntityController controller) {
        super("Управление '" + entity.getName() + "'");
        this.entity = entity;
        this.controller = controller;
        initTableModel();
    }

    private void initTableModel() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError("Ошибка инициализации данных: " + ex.getMessage());
            table.setModel(new DynamicTableModel(List.of(), entity));
        }
    }

    @Override
    protected void onAdd() {
        DynamicRecordFormDialog dialog = new DynamicRecordFormDialog(null, entity, controller, this::safeRefresh);
        dialog.setVisible(true);
    }

    @Override
    protected void onEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Выберите запись для редактирования.");
            return;
        }
        try {
            List<Map<String, Object>> records = controller.getAllRecords(entity.getName());
            System.out.println("Records: " + records);
            if (row >= records.size()) {
                showError("Запись больше не существует. Обновите таблицу.");
                safeRefresh();
                return;
            }
            Map<String, Object> record = records.get(row);
            Object idObj = record.get("ID");
            if (idObj == null) {
                showError("Не удалось определить ID записи. Проверьте структуру таблицы.");
                return;
            }
            int id;
            if (idObj instanceof Integer) {
                id = (int) idObj;
            } else if (idObj instanceof String) {
                id = Integer.parseInt((String) idObj);
            } else {
                showError("Неверный тип данных для ID: " + idObj.getClass().getName());
                return;
            }
            Map<String, Object> fullRecord = controller.getRecordById(entity.getName(), id);
            System.out.println("Full Record: " + fullRecord);
            DynamicRecordFormDialog dialog = new DynamicRecordFormDialog(fullRecord, entity, controller, this::safeRefresh);
            dialog.setVisible(true);
        } catch (SQLException ex) {
            showError("Ошибка загрузки записи: " + ex.getMessage());
        }
    }

    @Override
    protected void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Выберите запись для удаления.");
            return;
        }
        try {
            List<Map<String, Object>> records = controller.getAllRecords(entity.getName());
            Map<String, Object> record = records.get(row);
            Object idObj = record.get("ID");
            if (idObj == null) {
                showError("Не удалось определить ID записи.");
                return;
            }
            int id;
            if (idObj instanceof Integer) {
                id = (int) idObj;
            } else if (idObj instanceof String) {
                id = Integer.parseInt((String) idObj);
            } else {
                showError("Неверный тип данных для ID: " + idObj.getClass().getName());
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Удалить эту запись?",
                    "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteRecord(entity.getName(), id);
                safeRefresh();
                showSuccess("Запись удалена успешно.");
            }
        } catch (SQLException ex) {
            showError("Ошибка удаления записи: " + ex.getMessage());
        }
    }

    private void refreshData() throws SQLException {
        List<Map<String, Object>> records = controller.getAllRecords(entity.getName());
        System.out.println("Records for " + entity.getName() + ": " + records);
        table.setModel(new DynamicTableModel(records, entity));
    }

    private void safeRefresh() {
        try {
            refreshData();
        } catch (SQLException ex) {
            showError("Ошибка обновления данных: " + ex.getMessage());
        }
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private static class DynamicTableModel extends AbstractTableModel {
        private final List<Map<String, Object>> records;
        private final String[] columns;

        public DynamicTableModel(List<Map<String, Object>> records, DynamicEntity entity) {
            this.records = records;
            this.columns = new String[entity.getFields().size()];
            int i = 0;
            for (String field : entity.getFields().keySet()) {
                columns[i++] = field.toUpperCase();
            }
            System.out.println("Columns: " + Arrays.toString(columns));
            System.out.println("Records: " + records);
        }

        @Override
        public int getRowCount() {
            return records.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Map<String, Object> record = records.get(row);
            return record.get(columns[col]);
        }
    }
}