package windows;

import themes.ThemeManager;
import apps.*;
import taskbar.Taskbar;
import javax.swing.*;
import java.awt.*;
import desktop.DesktopIcon;

public class WindowManager{
    private Taskbar taskbar;
    private desktop.Desktop desktopInstance;
    public void openApp(String appName, JPanel desktop, ThemeManager themeManager){
        AppWindow window = null;

        switch(appName){
            case "File Explorer":
                window = new AppWindow("File Explorer", new apps.FileExplorer(themeManager), desktop, themeManager);
                break;
            case "Text Editor":
                window = new AppWindow("Text Editor", new apps.TextEditor(themeManager), desktop, themeManager);
                break;
            case "Calculator":
                window = new AppWindow("Calculator", new apps.Calculator(themeManager), desktop, themeManager);
                break;
            case "Terminal":
                window = new AppWindow("Terminal", new apps.Terminal(themeManager), desktop, themeManager);
                break;
            case "Paint":
                window = new AppWindow("Paint", new apps.Paint(themeManager), desktop, themeManager);
                break;
            case "Settings":
                window = new AppWindow("Settings", new apps.Settings(themeManager, desktop, desktopInstance), desktop, themeManager);
                break;
            case "Minesweeper":
                window = new AppWindow("Minesweeper", new apps.Minesweeper(themeManager), desktop, themeManager);
                break;
            case "Music Player":
                apps.MusicPlayer player = new apps.MusicPlayer(themeManager);
                window = new AppWindow("Music Player", new apps.MusicPlayer(themeManager), desktop, themeManager);
                window.setOnClose(() -> player.cleanup());
                break;
        }

        if(window != null){
            int x = 50 + (int)(Math.random() * 200);
            int y = 50 + (int)(Math.random() * 100);
            window.setBounds(x, y, window.getAppWidth(), window.getAppHeight());
            desktop.add(window);
            desktop.setComponentZOrder(window, 0);
            for(int i = 0; i < desktop.getComponentCount(); i++){
                if(desktop.getComponent(i) instanceof desktop.DesktopIcon){
                    desktop.setComponentZOrder(desktop.getComponent(i), desktop.getComponentCount() - 1);
                    break;
                }
            }
            desktop.revalidate();
            desktop.repaint();

            if(taskbar != null){
                taskbar.addTaskbarButton(appName, window);
            }
        }
    }
    public void setTaskbar(Taskbar taskbar){
        this.taskbar = taskbar;
    }
    public void setDesktop(desktop.Desktop d){
        this.desktopInstance = d;
    }
}