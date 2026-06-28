package desktop;

import windows.WindowManager;
import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DesktopIcon extends JPanel {
    private String appName;
    private Desktop desktop;
    private WindowManager windowManager;
    private ThemeManager themeManager;

    public DesktopIcon(String appName, Desktop desktop, WindowManager windowManager, ThemeManager themeManager){
        this.appName = appName;
        this.desktop = desktop;
        this.windowManager = windowManager;
        this.themeManager = themeManager;

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(30, 100, 180));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel iconBox = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getIconColor(appName));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
              }
        };
        iconBox.setPreferredSize(new Dimension(60, 50));
        iconBox.setOpaque(false);

        JLabel iconLabel = new JLabel(getIconChar(appName), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        iconLabel.setForeground(Color.WHITE);
        iconBox.add(iconLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(appName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        nameLabel.setForeground(Color.WHITE);

        add(iconBox, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    windowManager.openApp(appName, desktop.getDesktopPanel(), themeManager);
                }
            }

            public void mouseEntered(MouseEvent e){
                setBackground(new Color(60, 130, 210));
                repaint();
            }

            public void mouseExited(MouseEvent e){
                setBackground(new Color(30, 100, 180));
                repaint();
            }
        });
    }
    
    private String getIconChar(String appName){
        switch(appName){
            case "File Explorer": return "F";
            case "Text Editor": return "T";
            case "Calculator": return "C";
            case "Terminal": return ">";
            case "Paint": return "P";
            case "Settings": return "S";
            default: return "?";
        }
    }

    private Color getIconColor(String appName){
        switch(appName){
            case "File Explorer": return new Color(230, 160, 30);
            case "Text Editor": return new Color(0, 120, 215);
            case "Calculator": return new Color(16, 124, 16);
            case "Terminal": return new Color(30, 30, 30);
            case "Paint": return new Color(200, 50, 50);
            case "Settings": return new Color(100, 100, 100);
            default: return new Color(80, 80, 80);
        }
    }
}