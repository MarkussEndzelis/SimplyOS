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
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(getIconChar(appName), SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setPreferredSize(new Dimension(80, 50));

        JLabel nameLabel = new JLabel(appName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        nameLabel.setForeground(themeManager.getTextColor());

        add(iconLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    windowManager.openApp(appName, desktop.getDesktopPanel(), themeManager);
                }
            }

            public void mouseEntered(MouseEvent e){
                setOpaque(true);
                setBackground(new Color(255, 255, 255, 60));
                repaint();
            }

            public void mouseExited(MouseEvent e){
                setOpaque(false);
                repaint();
            }
        });
    }
    
    private String getIconChar(String appName){
        switch(appName){
            case "File Explorer": return "📁";
            case "Text Editor": return "📝";
            case "Calculator": return "🧮";
            case "Terminal": return "💻";
            case "Paint": return "🎨";
            case "Settings": return "⚙️";
            default: return "📄";
        }
    }
}