package themes;

import java.awt.*;

public class ThemeManager {
    private String currentTheme = "light";

    private final Color LIGHT_BG = new Color(240, 240, 240);
    private final Color LIGHT_TASKBAR = new Color(220, 220, 220);
    private final Color LIGHT_WINDOW = ne Color(255, 255, 255);
    private final Color LIGHT_TITLEBAR = new Color(200, 200, 200);
    private final Color LIGHT_TEXT = new Color(30, 30, 30);
    private final Color LIGHT_BORDER = new Color(180, 180, 180);
    private final Color LIGHT_ACCENT = new Color(0, 120, 215);
    private final Color LIGHT_WALLPAPER = new Color(70, 130, 180);

    private final Color DARK_BG = new Color(30, 30, 30);
    private final Color DARK_TASKBAR = new Color(20, 20, 20);
    private final Color DARK_WINDOW = new Color(40, 40, 40);
    private final Color DARK_TITLEBAR = new Color(50, 50, 50);
    private final Color DARK_TEXT = new Color(240, 240, 240);
    private final Color DARK_BORDER = new Color(70, 70, 70);
    private final Color DARK_ACCENT = new Color(0, 150, 255);
    private final Color DARK_WALLPAPER = new Color(20, 30, 50);

    public void setTheme(String theme){
        this.currentTheme = theme;
    }
    public String getCurrentTheme(){
        return currentTheme;
    }
    public boolean isDark(){
        return currentTheme.equals("dark");
    }
    public Color getBackgroundColor(){
        return isDark() ? DARK_BG : LIGHT_BG;
    }
    public Color getTaskbarColor(){
        return isDark() ? DARK_TASKBAR : LIGHT_TASKBAR;
    }
    public Color getWindowColor(){
        return isDark() ? DARK_WINDOW : LIGHT_WINDOW;
    }
    public Color getTitleBarColor(){
        return isDark() ? DARK_TITLEBAR : LIGHT_TITLEBAR;
    }
    public Color getTextColor(){
        return isDark() ? DARK_BORDER : LIGHT_BORDER;
    }
    public Color getBorderColor(){
        return isDark() ? DARK_BORDER : LIGHT_BORDER;
    }
    public Color getAccentColor(){
        return isDark() ? DARK_ACCENT : LIGHT_ACCENT;
    }
    public Color getWallpaperColor(){
        return isDark() ? DARK_WALLPAPER : LIGHT_WALLPAPER;
    }
}