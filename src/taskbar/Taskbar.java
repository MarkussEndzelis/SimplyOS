package taskbar;

import desktop.Desktop;
import windows.WindowManager;
import themes.ThemeManager;
import windows.AppWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Taskbar extends JPanel{
    private Desktop desktop;
    private WindowManager windowManager;
    private ThemeManager themeManager;
    private JPanel openAppsPanel;
    private Clock clock;
    private StartMenu startMenu;

    public Taskbar(Desktop desktop, WindowManager windowManager, ThemeManager themeManager){
        this.desktop = desktop;
        this.windowManager = windowManager;
        this.themeManager = themeManager;

        setPreferredSize(new Dimension(0, 45));
        setBackground(themeManager.getTaskbarColor());
        setLayout(new BorderLayout());

        JButton startBtn = new JButton("Start");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startBtn.setForeground(themeManager.getTextColor());
        startBtn.setBackground(themeManager.getTaskbarColor());
        startBtn.setBorderPainted(false);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> toggleStartMenu());

        openAppsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 6));
        openAppsPanel.setOpaque(false);

        clock = new Clock(themeManager);

        add(startBtn, BorderLayout.WEST);
        add(openAppsPanel, BorderLayout.CENTER);
        add(clock, BorderLayout.EAST);
    }

    private void toggleStartMenu(){
        if(startMenu == null){
            startMenu = new StartMenu(desktop, windowManager, themeManager);
        }
        if(startMenu.isVisible()){
            startMenu.setVisible(false);
        }else{
            int x = 0;
            int y = desktop.getHeight() - 45 - startMenu.getPreferredSize().height;
            startMenu.setBounds(x, y, 220, 320);
            desktop.getDesktopPanel().add(startMenu);
            startMenu.setVisible(true);
            startMenu.repaint();
        }
    }

    public void addTaskbarButton(String appName, AppWindow window){
        JButton btn = new JButton(appName);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(Color.WHITE);
        btn.setBackground(themeManager.getAccentColor());
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 30));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            if(window.getParent() == null){
                openAppsPanel.remove(btn);
                openAppsPanel.revalidate();
                openAppsPanel.repaint();
                return;
            }
            if(window.isVisible()){
                window.setVisible(false);
            }else{
                window.setVisible(true);
                window.getParent().setComponentZOrder(window, 0);
                window.getParent().repaint();
            }
        });
        window.setOnClose(() -> {
            openAppsPanel.remove(btn);
            openAppsPanel.revalidate();
            openAppsPanel.repaint();
        });
        openAppsPanel.add(btn);
        openAppsPanel.revalidate();
        openAppsPanel.repaint();
    }

    public void applyTheme(){
        setBackground(themeManager.getTaskbarColor());
        repaint();
    }
}