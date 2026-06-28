package windows;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JPanel{
    private String title;
    private JPanel content;
    private JPanel desktop;
    private ThemeManager themeManager;
    private int appWidth = 650;
    private int appHeight = 400;
    private Point dragStart;
    private boolean isMaximized = false;
    private Rectangle previousBounds;
    private Runnable onClose;

    public AppWindow(String title, JPanel content, JPanel desktop, ThemeManager themeManager){
        this.title = title;
        this.content = content;
        this.desktop = desktop;
        this.themeManager = themeManager;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(themeManager.getBorderColor(), 1));
        setBackground(themeManager.getWindowColor());

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(themeManager.getTitleBarColor());
        titleBar.setPreferredSize(new Dimension(0, 36));

        JLabel titleLabel = new JLabel("  " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(themeManager.getTextColor());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 7));
        buttons.setOpaque(false);

        JButton minimizeBtn = createWindowButton("_", new Color(255, 189, 68));
        JButton maximizeBtn = createWindowButton("O", new Color(0, 200, 80));
        JButton closeBtn = createWindowButton("X", new Color(220, 60, 60));

        minimizeBtn.addActionListener(e -> setVisible(false));
        maximizeBtn.addActionListener(e -> toggleMaximize());
        closeBtn.addActionListener(e -> {
            desktop.remove(this);
            desktop.revalidate();
            desktop.repaint();
            if(onClose != null){
                onClose.run();
            }
        });

        buttons.add(minimizeBtn);
        buttons.add(maximizeBtn);
        buttons.add(closeBtn);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(buttons, BorderLayout.EAST);

        titleBar.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                dragStart = e.getPoint();
                desktop.setComponentZOrder(AppWindow.this, 0);
                desktop.repaint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                if(!isMaximized){
                    Point current = SwingUtilities.convertPoint(titleBar, e.getPoint(), desktop);
                    setLocation(current.x - dragStart.x, current.y - dragStart.y);
                }
            }
        });

        add(titleBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private JButton createWindowButton(String text, Color color){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(44, 22));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void toggleMaximize(){
        if(isMaximized){
            setBounds(previousBounds);
            isMaximized = false;
        }else{
            previousBounds = getBounds();
            setBounds(0, 0, desktop.getWidth(), desktop.getHeight());
            isMaximized = true;
        }
        revalidate();
        repaint();
    }
    public int getAppWidth(){
        return appWidth;
    }
    public int getAppHeight(){
        return appHeight;
    }

    public void setOnClose(Runnable onClose){
        this.onClose = onClose;
    }
}