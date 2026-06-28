package taskbar;

import desktop.Desktop;
import windows.WindowManager;
import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JPanel{
    private Desktop desktop;
    private WindowManager windowManager;
    private ThemeManager themeManager;

    public StartMenu(Desktop desktop, WindowManager windowManager, ThemeManager themeManager){
        this.desktop = desktop;
        this.windowManager = windowManager;
        this.themeManager = themeManager;

        setLayout(new BorderLayout());
        setBackground(themeManager.getTaskbarColor());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(themeManager.getBorderColor(), 1),
            BorderFactory.createEmptyBorder(4, 0, 4, 0)
        ));
        setPreferredSize(new Dimension(220, 320));

        JLabel title = new JLabel("  SimplyOS", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(themeManager.getAccentColor());
        title.setPreferredSize(new Dimension(220, 45));
        title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, themeManager.getBorderColor()));

        JPanel appsPanel = new JPanel();
        appsPanel.setLayout(new BoxLayout(appsPanel, BoxLayout.Y_AXIS));
        appsPanel.setBackground(themeManager.getTaskbarColor());

        String[] apps = {"File Explorer", "Text Editor", "Calculator", "Terminal", "Paint", "Settings"};
        String[] icons = {"", "", "", "", "", ""};

        for(int i = 0; i < apps.length; i++){
            final String appName = apps[i];
            JButton btn = new JButton(appName);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setForeground(themeManager.getTextColor());
            btn.setBackground(themeManager.getTaskbarColor());
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(220, 44));
            btn.setPreferredSize(new Dimension(220, 44));
            btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    btn.setBackground(themeManager.getAccentColor());
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e){
                    btn.setBackground(themeManager.getTaskbarColor());
                    btn.setForeground(themeManager.getTextColor());
                }
            });
            btn.addActionListener(e -> {
                windowManager.openApp(appName, desktop.getDesktopPanel(), themeManager);
                setVisible(false);
            });
            appsPanel.add(btn);
        }
        JButton powerBtn = new JButton("Shut Down");
        powerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        powerBtn.setForeground(new Color(220, 60, 60));
        powerBtn.setBackground(themeManager.getTaskbarColor());
        powerBtn.setBorderPainted(false);
        powerBtn.setFocusPainted(false);
        powerBtn.setHorizontalAlignment(SwingConstants.LEFT);
        powerBtn.setPreferredSize(new Dimension(220, 40));
        powerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        powerBtn.addActionListener(e -> System.exit(0));

        add(title, BorderLayout.NORTH);
        add(appsPanel, BorderLayout.CENTER);
        add(powerBtn, BorderLayout.SOUTH);
    }
}