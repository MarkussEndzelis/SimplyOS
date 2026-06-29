package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockApp extends JPanel{
    private ThemeManager themeManager;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JLabel clockLabel;
    private JLabel dateLabel;

    private long swStart = 0;
    private long swElapsed = 0;
    private boolean swRunning = false;
    private JLabel swLabel;
    private Timer swTimer;

    private int cdSeconds = 0;
    private int cdRemaining = 0;
    private boolean cdRunning = false;
    private JLabel cdLabel;
    private JTextField cdInput;
    private Timer cdTimer;

    public ClockApp(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabs.setBackground(themeManager.getTitleBarColor());
        tabs.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        String[] tabNames = {"Clock", "Stopwatch", "Timer"};
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(themeManager.getWindowColor());

        for(String name : tabNames){
            JButton tab = new JButton(name);
            tab.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tab.setBackground(themeManager.getTitleBarColor());
            tab.setForeground(themeManager.getTextColor());
            tab.setBorderPainted(false);
            tab.setFocusPainted(false);
            tab.setPreferredSize(new Dimension(110, 32));
            tab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            tab.addActionListener(e -> cardLayout.show(cardPanel, name));
            tab.addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){
                    tab.setBackground(themeManager.getAccentColor());
                    tab.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e){
                    tab.setBackground(themeManager.getTitleBarColor());
                    tab.setForeground(themeManager.getTextColor());
                }
            });
            tabs.add(tab);
        }
        cardPanel.add(buildClockPanel(), "Clock");
        cardPanel.add(buildStopwatchPanel(), "Stopwatch");
        cardPanel.add(buildTimerPanel(), "Timer");

        add(tabs, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        Timer clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();
        updateClock();
    }

    private JPanel buildClockPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(themeManager.getWindowColor());

        clockLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        clockLabel.setForeground(themeManager.getTextColor());

        dateLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dateLabel.setForeground(themeManager.getTextColor());

        JPanel analogClock = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int cx = getWidth() / 2, cy = getHeight() / 2;
                int r = Math.min(cx, cy) - 10;

                g2.setColor(themeManager.getTitleBarColor());
                g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                g2.setColor(themeManager.getBorderColor());
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);

                g2.setColor(themeManager.getTextColor());
                for(int i = 0; i < 12; i++){
                    double angle = Math.toRadians(i * 30 - 90);
                    int x1 = (int)(cx + Math.cos(angle) * (r - 8));
                    int y1 = (int)(cy + Math.sin(angle) * (r - 8));
                    int x2 = (int)(cx + Math.cos(angle) * (r - 16));
                    int y2 = (int)(cy + Math.sin(angle) * (r - 16));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(x1, y1, x2, y2);
                }
                LocalDateTime now = LocalDateTime.now();
                int hour = now.getHour() % 12;
                int minute = now.getMinute();
                int second = now.getSecond();

                double hourAngle = Math.toRadians((hour * 30 + minute * 0.5) - 90);
                g2.setColor(themeManager.getTextColor());
                g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx, cy, (int)(cx + Math.cos(hourAngle) * r * 0.5), (int)(cy + Math.sin(hourAngle) * r * 0.5));
                
                double minAngle = Math.toRadians(minute * 6 - 90);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx, cy, (int)(cx + Math.cos(minAngle) * r * 0.7), (int)(cy + Math.sin(minAngle) * r * 0.7));

                double secAngle = Math.toRadians(second * 6 - 90);
                g2.setColor(themeManager.getAccentColor());
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx, cy, (int)(cx + Math.cos(secAngle) * r * 0.85), (int)(cy + Math.sin(secAngle) * r * 0.85));

                g2.setColor(themeManager.getAccentColor());
                g2.fillOval(cx - 4, cy - 4, 8, 8);
            }
        };
        analogClock.setBackground(themeManager.getWindowColor());
        analogClock.setPreferredSize(new Dimension(180, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(analogClock,gbc);
        gbc.gridy = 1;
        panel.add(clockLabel, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(4, 0, 0, 0);
        panel.add(dateLabel, gbc);

        return panel;
    }
    private JPanel buildStopwatchPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(themeManager.getWindowColor());

        swLabel = new JLabel("00:00.000", SwingConstants.CENTER);
        swLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        swLabel.setForeground(themeManager.getTextColor());

        JButton startStopBtn = new JButton("Start");
        JButton resetBtn = new JButton("Reset");

        styleBtn(startStopBtn);
        styleBtn(resetBtn);

        swTimer = new Timer(10, e -> {
            swElapsed = System.currentTimeMillis() - swStart;
            swLabel.setText(formatStopwatch(swElapsed));
        });

        startStopBtn.addActionListener(e -> {
            if(!swRunning){
                swStart = System.currentTimeMillis() - swElapsed;
                swTimer.start();
                swRunning = true;
                startStopBtn.setText("Stop");
                startStopBtn.setBackground(new Color(200, 60, 60));
            }else{
                swTimer.stop();
                swRunning = false;
                startStopBtn.setText("Start");
                startStopBtn.setBackground(themeManager.getAccentColor());
            }
        });

        resetBtn.addActionListener(e -> {
            swTimer.stop();
            swRunning = false;
            swElapsed = 0;
            swLabel.setText("00:00.000");
            startStopBtn.setText("Start");
            startStopBtn.setBackground(themeManager.getAccentColor());
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(themeManager.getWindowColor());
        btnRow.add(startStopBtn);
        btnRow.add(resetBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 0, 20, 0);
        panel.add(swLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnRow, gbc);

        return panel;

    }
    private JPanel buildTimerPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(themeManager.getWindowColor());
        
        cdLabel = new JLabel("00:00", SwingConstants.CENTER);
        cdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        cdLabel.setForeground(themeManager.getTextColor());

        cdInput = new JTextField("60", 6);
        cdInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cdInput.setBackground(themeManager.getTitleBarColor());
        cdInput.setForeground(themeManager.getTextColor());
        cdInput.setCaretColor(themeManager.getTextColor());
        cdInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(themeManager.getBorderColor()),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        cdInput.setHorizontalAlignment(JTextField.CENTER);

        JLabel secLabel = new JLabel("seconds");
        secLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        secLabel.setForeground(themeManager.getTextColor());

        JButton startBtn = new JButton("Start");
        JButton resetBtn = new JButton("Reset");
        styleBtn(startBtn);
        styleBtn(resetBtn);

        cdTimer = new Timer(1000, e -> {
            cdRemaining--;
            updateCdLabel();
            if(cdRemaining <= 0){
                cdTimer.stop();
                cdRunning = false;
                cdLabel.setForeground(new Color(220, 60, 60));
                cdLabel.setText("Done!");
                startBtn.setText("Start");
                startBtn.setBackground(themeManager.getAccentColor());
                Toolkit.getDefaultToolkit().beep();
            }
        });

        startBtn.addActionListener(e -> {
            if(!cdRunning){
                try{
                    cdSeconds = Integer.parseInt(cdInput.getText().trim());
                    if(cdSeconds <= 0){
                        return;
                    }
                    cdRemaining = cdSeconds;
                    cdLabel.setForeground(themeManager.getTextColor());
                    updateCdLabel();
                    cdTimer.start();
                    cdRunning = true;
                    startBtn.setText("Stop");
                    startBtn.setBackground(new Color(200, 60, 60));
                }catch(NumberFormatException ex){
                    cdInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
            }else{
                cdTimer.stop();
                cdRunning = false;
                startBtn.setText("Start");
                startBtn.setBackground(themeManager.getAccentColor());
            }
        });

        resetBtn.addActionListener(e -> {
            cdTimer.stop();
            cdRunning = false;
            cdRemaining = 0;
            cdLabel.setForeground(themeManager.getTextColor());
            cdLabel.setText("00:00");
            startBtn.setText("Start");
            startBtn.setBackground(themeManager.getAccentColor());
        });

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        inputRow.setBackground(themeManager.getWindowColor());
        inputRow.add(cdInput);
        inputRow.add(secLabel);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setBackground(themeManager.getWindowColor());
        inputRow.add(startBtn);
        inputRow.add(resetBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 16, 0);
        panel.add(cdLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(inputRow, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(btnRow, gbc);

        return panel;
    }

    private void updateClock(){
        LocalDateTime now = LocalDateTime.now();
        clockLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        if(cardPanel != null){
            cardPanel.repaint();
        }
    }
    private void updateCdLabel(){
        int m = cdRemaining / 60;
        int s = cdRemaining % 60;
        cdLabel.setText(String.format("%02d:%02d", m, s));
    }
    private String formatStopwatch(long ms){
        long minutes = ms / 60000;
        long seconds = (ms % 60000) / 1000;
        long millis = ms % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
    private void styleBtn(JButton btn){
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(themeManager.getAccentColor());
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 34));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

}