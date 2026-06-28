package apps;

import themes.ThemeManager;
import desktop.Desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings extends JPanel{
    private ThemeManager themeManager;
    private JPanel desktop;

    public Settings(ThemeManager themeManager, JPanel desktop){
        this.themeManager = themeManager;
        this.desktop = desktop;

        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(themeManager.getTitleBarColor());
        sidebar.setPreferredSize(new Dimension(160, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, themeManager.getBorderColor()));

        JPanel contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(themeManager.getWindowColor());

        String[] sections = {"Appearance", "About"};
        JPanel[] panels = {buildAppearancePanel(contentArea), buildAboutPanel()};

        for(int i = 0; i < sections.length; i++){
            final String section = sections[i];
            final JPanel panel = panels[i];
            contentArea.add(panel, section);

            JButton btn = new JButton(section);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            btn.setForeground(themeManager.getTextColor());
            btn.setBackground(themeManager.getTitleBarColor());
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setPreferredSize(new Dimension(160, 40));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){
                    btn.setBackground(themeManager.getAccentColor());
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e){
                    btn.setBackground(themeManager.getTitleBarColor());
                    btn.setForeground(themeManager.getTextColor());
                }
            });
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentArea.getLayout();
                cl.show(contentArea, section);
            });
            sidebar.add(btn);
        }
        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel buildAppearancePanel(JPanel contentArea){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWindowColor());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Appearance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(themeManager.getTextColor());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel themeLabel = new JLabel("Theme");
        themeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        themeLabel.setForeground(themeManager.getTextColor());
        themeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel themeButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        themeButtons.setBackground(themeManager.getWindowColor());
        themeButtons.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton lightBtn = new JButton("Light");
        lightBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lightBtn.setFocusPainted(false);
        lightBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lightBtn.setPreferredSize(new Dimension(100, 36));

        JButton darkBtn = new JButton("Dark");
        darkBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        darkBtn.setFocusPainted(false);
        darkBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        darkBtn.setPreferredSize(new Dimension(100, 36));
        
        
        lightBtn.addActionListener(e -> {
            themeManager.setTheme("light");
            refreshDesktop();
        });
        darkBtn.addActionListener(e -> {
            themeManager.setTheme("dark");
            refreshDesktop();
        });

        themeButtons.add(lightBtn);
        themeButtons.add(darkBtn);

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(themeLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(themeButtons);

        return panel;
    }
    private JPanel buildAboutPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(themeManager.getWindowColor());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("About SimplyOS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(themeManager.getTextColor());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel version = new JLabel("Version 1.0.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        version.setForeground(themeManager.getTextColor());
        version.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("A simple desktop OS simulation built in Java.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(themeManager.getTextColor());
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(16));
        panel.add(version);
        panel.add(Box.createVerticalStrut(8));
        panel.add(desc);

        return panel;
    }

    private void refreshDesktop(){
        desktop.setBackground(themeManager.getWallpaperColor());
        desktop.repaint();
    }
}