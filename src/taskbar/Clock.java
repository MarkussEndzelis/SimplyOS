package taskbar;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock extends JLabel{
    private ThemeManager themeManager;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Clock(ThemeManager themeManager){
        this.themeManager = themeManager;

        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setForeground(themeManager.getTextColor());
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(90, 45));

        updateTime();

        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime(){
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(timeFormatter);
        String date = now.format(dateFormatter);
        setText("<html><center>" + time + "<br><small>" + date + "</small></center></html>");
    }
}