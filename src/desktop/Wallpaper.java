package desktop;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;

public class Wallpaper extends JPanel{
    private ThemeManager themeManager;
    private Color wallpaperColor;

    public Wallpaper(ThemeManager themeManager){
        this.themeManager = themeManager;
        this.wallpaperColor = themeManager.getWallpaperColor();
        setLayout(null);
    }

    public void setWallpaperColor(Color color){
        this.wallpaperColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(
            0, 0, wallpaperColor,
            getWidth(), getHeight(), wallpaperColor.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}