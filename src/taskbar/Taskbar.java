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

        setPreferredSize(new Dimension(0, 48));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0,  0, themeManager.getBorderColor()));
        setBackground(new Color(10, 15, 40, 220));
        setOpaque(true);
        setLayout(new BorderLayout());

        JButton startBtn = new JButton("Start");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        startBtn.setPreferredSize(new Dimension(80, 48));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBackground(new Color(0, 120, 215));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBorderPainted(false);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> toggleStartMenu());

        openAppsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 9));
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
            desktop.getDesktopPanel().setComponentZOrder(startMenu, 0);
            startMenu.setVisible(true);
            startMenu.repaint();
        }
    }

    public void addTaskbarButton(String appName, AppWindow window){
        JButton btn = new JButton(appName);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 100, 180));
        btn.setOpaque(true);
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
                if(window.getParent() != null){
                    window.getParent().setComponentZOrder(window, 0);
                    window.getParent().repaint();
                }
            }
        });
        window.setOnClose(() -> {
            SwingUtilities.invokeLater(() -> {
                openAppsPanel.remove(btn);
                openAppsPanel.revalidate();
                openAppsPanel.repaint();
                Taskbar.this.revalidate();
                Taskbar.this.repaint();
                getParent().revalidate();
                getParent().repaint();
            });
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