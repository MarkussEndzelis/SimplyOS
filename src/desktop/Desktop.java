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
    private Image wallpaperImage = null;
    private DesktopPet pet;

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
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if(wallpaperImage != null){
                g2d.drawImage(wallpaperImage, 0, 0, getWidth(), getHeight(), null);
            }else{
                Color top = new Color(5, 10, 35);
                Color bottom = new Color(20, 50, 100);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, 180));
                java.util.Random rand = new java.util.Random(42);
                for(int i = 0; i < 150; i++){
                    int x = rand.nextInt(getWidth());
                    int y = rand.nextInt(getHeight());
                    int size = rand.nextInt(2) + 1;
                    g2d.fillOval(x, y, size, size);
                }
                
                java.awt.RadialGradientPaint glow = new java.awt.RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f,
                    getWidth() / 2f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(30, 80, 160, 60), new Color(0, 0, 0, 0)}
                );
                g2d.setPaint(glow);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
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
        windowManager.setDesktop(this);

        add(desktopPanel, BorderLayout.CENTER);
        add(taskbar, BorderLayout.SOUTH);

        addDesktopIcons();

        pet = new DesktopPet(desktopPanel);
        pet.setBounds(200, 400, 120, 110);
        desktopPanel.add(pet);
        desktopPanel.setComponentZOrder(pet, 0);

        setVisible(true);
    }

    private void addDesktopIcons(){
        String[] apps = {"File Explorer", "Text Editor", "Calculator", "Terminal", "Paint","Minesweeper", "Music Player", "Pong", "Settings"};
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
    
    public void setWallpaperImage(Image img){
        this.wallpaperImage = img;
        desktopPanel.repaint();
    }

    public JPanel getDesktopPanel(){
        return desktopPanel;
    }
}