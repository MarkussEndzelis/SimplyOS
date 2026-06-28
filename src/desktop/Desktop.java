package desktop;

import taskbar.Taskbar;
import windows.WindowManager;
import themes.ThemeManager;
import java.awt.GradientPaint;

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
    desktopPanel = new JPanel(null){
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            Color c1 = themeManager.getWallpaperColor();
            Color c2 = c1.darker();
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        };

        desktopPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isRightMouseButton(e)){
                    showContextMenu(e.getX(), e.getY());
                }
            }
        });

        taskbar = new Taskbar(this, windowManager, themeManager);
        windowManager.setTaskbar(taskbar);

        add(desktopPanel, BorderLayout.CENTER);
        add(taskbar, BorderLayout.SOUTH);

        addDesktopIcons();

        setVisible(true);
    }

    private void addDesktopIcons(){
        String[] apps = {"File Explorer", "Text Editor", "Calculator", "Terminal", "Paint","Minesweeper", "Settings"};
        int x = 20;
        int y = 20;
        for(String app : apps){
            DesktopIcon icon = new DesktopIcon(app, this, windowManager, themeManager);
            icon.setBounds(x, y, 80, 80);
            desktopPanel.add(icon);
            desktopPanel.setComponentZOrder(icon, desktopPanel.getComponentCount() - 1);
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