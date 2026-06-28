package desktop;

import taskbar.Taskbar;
import windows.WindowManager;
import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Desktop extends JFrame {
    private JPanel desktopPanel;
    private Taskbar taskbar;
    private WindowManager windowManager;
    private ThemeManager themeManager;

    public Desktop(){
        themeManager = new ThemeManager();
        windowManager = new WindowManager();
        setTitle("SimplyOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    public void launch(){
        desktopPanel = new JPanel(null);
        desktopPanel.setBackground(themeManager.getWallpaperColor());

        desktopPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isRightMouseButton(e)){
                    showContextMenu(e.getX(), e.getY());
                }
            }
        });

        taskbar = new Taskbar(this, windowManager, themeManager);

        add(desktopPanel, BorderLayout.CENTER);
        add(taskbar, BorderLayout.SOUTH);

        addDesktopIcons();

        setVisible(true);
    }

    private void addDesktopIcons(){
        String[] apps = {"File Explorer", "Text Editor", "Calculator", "Terminal", "Paint", "Settings"};
        int x = 20;
        int y = 20;
        for(String app : apps){
            DesktopIcon icon = new DesktopIcon(app, this, windowManager, themeManager);
            icon.setBounds(x, y, 80, 80);
            desktopPanel.add(icon);
            y += 110;
        }
    }

    private void showContextMenu(int x, int y){
        JPopupMenu menu = new JPopupMenu();
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem settings = new JMenuItem("Settings");
        settings.addActionListener(e -> windowManager.openApp("Settings", desktopPanel, themeManager));
        menu.add(refresh);
        menu.add(settings);
        menu.show(desktopPanel, x, y);
    }

    public JPanel getDesktopPanel(){
        return desktopPanel;
    }
}