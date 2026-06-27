package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Paint extends JPanel{

    private ThemeManager themeManager;
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Color currentColor = Color.BLACK;
    private int brushSize = 5;
    private boolean erasing = false;
    private int lastX, lastY;

    public Paint(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.setBackground(new Color(30, 30, 40));

        Color[] palette = {
            Color.BLACK, Color.WHITE, Color.RED, Color.GREEN,
            Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PINK,
            Color.CYAN, Color.MAGENTA
        };

        for(Color c : palette){
            JButton btn = new JButton();
            btn.setBackground(c);
            btn.setPreferredSize(new Dimension(24, 24));
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            btn.addActionListener(e -> { currentColor = c; erasing = false; });
            toolbar.add(btn);
        }
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setForeground(Color.WHITE);
        toolbar.add(sizeLabel);

        JSlider sizeSlider = new JSlider(1, 40, 5);
        sizeSlider.setPreferredSize(new Dimension(80, 24));
        sizeSlider.setBackground(new Color(30, 30, 40));
        sizeSlider.addChangeListener(e -> bushSize = sizeSlider.getValue());
        toolbar.add(sizeSlider);

        JButton eraser = new JButton("Eraser");
        eraser.addActionListener(e -> erasing = true);
        toolbar.add(eraser);

        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            repaint();
        });
        toolbar.add(clear);

        JPanel canvasPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                if(canvas != null){
                    ng.drawImage(canvas, 0, 0, null);
                }
            }
        };

        canvasPanel.addComponentListener(new ComponentListener(){
            @Override
            public void componentResized(ComponentEvent e){
                int w = canvasPanel.getWidth();
                int h = canvasPanel.getHeight();
                if(w > 0 && h > 0){
                    BufferedImage newCanvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D ng = newCanvas.createGraphics();
                    ng.setColor(Color.WHITE);
                    ng.fillRect(0, 0, w, h);
                    if(canvas != null){
                        ng.drawImage(canvas, 0, 0, null);
                    }
                    ng.dispose();
                    canvas = newCanvas;
                    g2d = canvas.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                }
            }
        });
        canvasPanel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                lastX = e.getX(); lastY = e.getY();
                drawAt(e.getX(), e.getY());
            }
        });

        canvasPanel.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                if(g2d == null){
                    return;
                }
                g2d.setColor(erasing ? Color.WHITE : currentColor);
                g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(lastX, lastY, e.getX(), e.getY());
                lastX = e.getX();
                lastY = e.getY();
                canvasPanel.repaint();
            }
        });

        add(toolbar, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
    }
    private void drawAt(int x, int y){
        if(g2d == null){
            return;
        }
        g2d.setColor(erasing ? Color.WHITE : currentColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
        repaint();
    }

}
