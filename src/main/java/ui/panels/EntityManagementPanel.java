package ui.panels;

import controller.EntityController;
import model.DynamicEntity;
import ui.components.TablePanel;
import ui.dialogs.EntityFormDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EntityManagementPanel extends JPanel {
    private final EntityController controller;
    private JList<DynamicEntity> entityList;
    private DefaultListModel<DynamicEntity> entityListModel;

    public EntityManagementPanel(EntityController controller) throws SQLException {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        initializeUI();
        loadEntities();
    }

    private void initializeUI() {
        entityListModel = new DefaultListModel<>();
        entityList = new JList<>(entityListModel);
        entityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entityList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openEntityManagement();
                }
            }
        });

        JToolBar toolbar = new JToolBar();
        toolbar.add(createToolButton("Добавить сущность", e -> addNewEntity()));
        toolbar.add(createToolButton("Удалить сущность", e -> deleteEntity()));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(entityList), BorderLayout.CENTER);
        leftPanel.add(toolbar, BorderLayout.SOUTH);
        leftPanel.setPreferredSize(new Dimension(300, 400));

        add(leftPanel, BorderLayout.WEST);
        add(new JLabel("Выберите сущность для управления (двойной клик)", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    private JButton createToolButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.addActionListener(listener);
        btn.setMargin(new Insets(2, 5, 2, 5));
        return btn;
    }

    private void loadEntities() {
        try {
            entityListModel.clear();
            List<DynamicEntity> entities = controller.getEntities();
            for (DynamicEntity entity : entities) {
                entityListModel.addElement(entity);
            }
        } catch (SQLException e) {
            showError("Ошибка загрузки сущностей: " + e.getMessage());
        }
    }

    private void addNewEntity() {
        EntityFormDialog dialog = new EntityFormDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        DynamicEntity entity = dialog.getEntity();
        if (entity != null) {
            try {
                controller.createEntity(entity);
                loadEntities();
                showSuccess("Сущность '" + entity.getName() + "' создана успешно.");
            } catch (SQLException e) {
                showError("Ошибка создания сущности: " + e.getMessage());
            }
        }
    }

    private void deleteEntity() {
        DynamicEntity selectedEntity = entityList.getSelectedValue();
        if (selectedEntity == null) {
            showError("Выберите сущность для удаления.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this, "Удалить сущность '" + selectedEntity.getName() + "' и все её данные?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteEntity(selectedEntity.getName());
                loadEntities();
                showSuccess("Сущность '" + selectedEntity.getName() + "' удалена успешно.");
            } catch (SQLException e) {
                showError("Ошибка удаления сущности: " + e.getMessage());
            }
        }
    }

    private void openEntityManagement() {
        DynamicEntity selectedEntity = entityList.getSelectedValue();
        if (selectedEntity != null) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(frame, "Управление '" + selectedEntity.getName() + "'", true);
            dialog.setSize(800, 600);
            dialog.add(new DynamicEntityPanel(selectedEntity, controller));
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        }
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}