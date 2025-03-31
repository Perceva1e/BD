package ui.themes;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class ThemeManager {
    public static void configure() {
        FlatLightLaf.setup();
        UIManager.put("Table.rowHeight", 35);
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("ProgressBar.arc", 10);
        UIManager.put("TextComponent.arc", 5);
    }
}