package desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class DesktopPet extends JPanel{
    private int x, y;
    private int dx = 2, dy = 0;
    private int frame = 0;
    private int frameCounter = 0;
    private String mood = "happy";
    private String speech = null;
    private int speechTimer = 0;
    private JPanel desktopPanel;
    private Random rand = new Random();
    private int actionTimer = 0;
    private String currentAction = "walk";
    private int width = 60;
    private int height = 80;

    private static final String[] HAPPY_PHRASES = {
        "Hello!", "Nice day!", "Lets code!", ":)", "Wheee!", "Hi there!"
    };
    private static final String[] IDLE_PHRASES = {
        "...", "Hmm...", "Bored...", "zZz", "What to do?"
    };
    private static final String[] SCARED_PHRASES = {
        "Eek!", "Too fast!", "Watch out!", "Whoa!"
    };

    public DesktopPet(JPanel desktopPanel){
        this.desktopPanel = desktopPanel;
        this.x = 200;
        this.y = 400;
        setOpaque(false);
        setSize(width + 60, height + 30);

        addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                mood = "happy";
                say(HAPPY_PHRASES[rand.nextInt(HAPPY_PHRASES.length)]);
            }
        });

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                if(SwingUtilities.isRightMouseButton(e)){
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem dance = new JMenuItem("Dance!");
                    JMenuItem sleep = new JMenuItem("Sleep");
                    JMenuItem scatter = new JMenuItem("Scatter Icons");
                    dance.addActionListener(ev -> {
                        currentAction = "dance";
                        actionTimer = 60;
                        say("Wheee!");
                    });
                    sleep.addActionListener(ev -> {
                        currentAction = "sleep";
                        actionTimer = 100;
                        say("zZz...");
                        mood = "sleepy";
                    });
                    scatter.addActionListener(ev -> scatterIcons());
                    menu.add(dance);
                    menu.add(sleep);
                    menu.add(scatter);
                    menu.show(DesktopPet.this, e.getX(), e.getY());
                }
            }
        });
        
        Timer timer = new Timer(50, e -> update());
        timer.start();
    }

    private void update(){
        frameCounter++;
        if(frameCounter % 8 == 0){
            frame = (frame + 1) % 4;
        }
        actionTimer = Math.max(0, actionTimer - 1);
        if(actionTimer == 0 && !currentAction.equals("walk")){
            currentAction = "walk";
            mood = "happy";
        }

        if(rand.nextInt(200) == 0){
            int r = rand.nextInt(4);
            if(r == 0){
                currentAction = "idle";
                actionTimer = 60;
                say(IDLE_PHRASES[rand.nextInt(IDLE_PHRASES.length)]);
            }else if(r == 1){
                dx = -dx;
            }else if(r == 2){
                currentAction = "dance";
                actionTimer = 40;
                say("La la la!");
            }else{
                say(HAPPY_PHRASES[rand.nextInt(HAPPY_PHRASES.length)]);
            }
        }

        if(currentAction.equals("walk")){
            x += dx;
            if(x < 0){
                x = 0;
                dx = Math.abs(dx);
                say(SCARED_PHRASES[rand.nextInt(SCARED_PHRASES.length)]);
            }
            if(x > desktopPanel.getWidth() - width - 60){
                x = desktopPanel.getWidth() - width - 60;
                dx = -Math.abs(dx);
                say(SCARED_PHRASES[rand.nextInt(SCARED_PHRASES.length)]);
            }
        }
        if(speechTimer > 0){
            speechTimer--;
            if(speechTimer == 0){
                speech = null;
            }
        }
        setBounds(x, y, width + 60, height + 30);
        repaint();
    }

    private void say(String text){
        speech = text;
        speechTimer = 60;
    }

    private void scatterIcons(){
        say("Scatter!");
        for(Component c : desktopPanel.getComponents()){
            if(c instanceof DesktopIcon){
                int nx = rand.nextInt(Math.max(1, desktopPanel.getWidth() - 90));
                int ny = rand.nextInt(Math.max(1, desktopPanel.getHeight() - 100));
                c.setLocation(nx, ny);
            }
        }
        desktopPanel.repaint();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        boolean facingLeft = dx < 0;
        int bx = facingLeft ? width + 50 : 10;
        int scaleX = facingLeft ? - 1 : 1;

        g2.translate(bx, 0);
        g2.scale(scaleX, 1);

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(5, height - 5, 50, 10);

        Color bodyColor = mood.equals("sleepy") ? new Color(100, 120, 180) : currentAction.equals("dance") ? new Color(255, 100, 150) : new Color(80, 140, 255);
        g2.setColor(bodyColor);
        g2.fillRoundRect(10, 30, 40, 40, 15, 15);

        g2.setColor(new Color(200, 220, 255));
        g2.fillOval(18, 38, 24, 24);

        g2.setColor(bodyColor);
        g2.fillOval(8, 5, 44, 40);

        if(currentAction.equals("sleep") || mood.equals("sleepy")){
            g2.setColor(new Color(40, 40, 80));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(16, 22, 22, 22);
            g2.drawLine(34, 22, 40, 22);
        }else{
            g2.setColor(Color.WHITE);
            g2.fillOval(15, 16, 14, 14);
            g2.fillOval(31, 16, 14, 14);
            g2.setColor(new Color(30, 30, 80));
            g2.fillOval(18, 19, 8, 8);
            g2.fillOval(34, 19, 8, 8);

            g2.setColor(Color.WHITE);
            g2.fillOval(20, 20, 3, 3);
            g2.fillOval(36, 20, 3, 3);
        }
        
        g2.setColor(new Color(40, 40, 80));
        g2.setStroke(new BasicStroke(2));
        if(mood.equals("happy") || currentAction.equals("dance")){
            g2.drawArc(20, 28, 20, 10, 200, 140);
        }else if(mood.equals("sleepy")){
            g2.drawLine(22, 32, 38, 32);
        }else{
            g2.drawArc(20, 32, 20, 8, 20, 140);
        }

        g2.setColor(bodyColor);
        g2.fillOval(5, 0, 16, 20);
        g2.fillOval(39, 0, 16, 20);
        g2.setColor(new Color(255, 180, 200));
        g2.fillOval(8, 3, 10, 14);
        g2.fillOval(42, 3, 10, 14);

        int armY = currentAction.equals("dance") ? 35 + (int)(Math.sin(frameCounter * 0.3) * 8) : 38;
        g2.setColor(bodyColor);
        g2.fillRoundRect(-2, armY, 14, 20, 8, 8);
        g2.fillRoundRect(48, armY, 14, 20, 8, 8);

        int legOffset = currentAction.equals("walk") ? (int)(Math.sin(frameCounter * 0.4) * 6) : 0;
        g2.fillRoundRect(14, 65, 14, 20, 8, 8);
        g2.fillRoundRect(32, 65, 14, 20, 8, 8);

        g2.setColor(new Color(50, 50, 120));
        g2.fillRoundRect(12, 78 - legOffset, 16, 8, 6, 6);
        g2.fillRoundRect(30, 78 + legOffset, 16, 8, 6, 6);

        g2.setColor(bodyColor);
        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int tailWag = (int)(Math.sin(frameCounter * 0.3) * 12);
        g2.drawArc(-15, 45 + tailWag, 25, 20, 0, 180);

        g2.translate(-bx, 0);
        g2.scale(scaleX, 1);

        if(speech != null){
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(speech) + 14;
            int th = 22;
            int bx2 = facingLeft ? 0 : 10;
            int by = 0;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(bx2, by, tw, th, 10, 10);
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(bx2, by, tw, th, 10, 10);
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(speech, bx2 + 7, by + 15);
        }
    }
}
