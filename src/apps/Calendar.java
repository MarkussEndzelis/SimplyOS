package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class Calendar extends JPanel{
    private ThemeManager themeManager;
    private LocalDate today = LocalDate.now();
    private YearMonth currentMonth;
    private JLabel monthLabel;
    private JPanel gridPanel;

    public Calendar(ThemeManager themeManager){
        this.themeManager = themeManager;
        this.currentMonth = YearMonth.now();
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(themeManager.getTitleBarColor());
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JButton prevBtn = new JButton("<");
        JButton nextBtn = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        monthLabel.setForeground(themeManager.getTextColor());

        for(JButton btn : new JButton[]{prevBtn, nextBtn}){
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(themeManager.getAccentColor());
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(44, 30));
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        prevBtn.addActionListener(e -> {currentMonth = currentMonth.minusMonths(1); buildCalendar(); });
        nextBtn.addActionListener(e -> {currentMonth = currentMonth.plusMonths(1); buildCalendar(); });

        topBar.add(prevBtn, BorderLayout.WEST);
        topBar.add(monthLabel, BorderLayout.CENTER);
        topBar.add(nextBtn, BorderLayout.EAST);

        JPanel headerRow = new JPanel(new GridLayout(1, 7, 2, 0));
        headerRow.setBackground(themeManager.getWindowColor());
        headerRow.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for(String d : days){
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(themeManager.getTextColor());
            headerRow.add(lbl);
        }

        gridPanel = new JPanel(new GridLayout(6, 7, 2, 2));
        gridPanel.setBackground(themeManager.getWindowColor());
        gridPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(themeManager.getWindowColor());
        center.add(headerRow, BorderLayout.NORTH);
        center.add(gridPanel, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        buildCalendar();
    }

    private void buildCalendar(){
        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        + " " + currentMonth.getYear());

        gridPanel.removeAll();

        int firstDay = currentMonth.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        for(int i = 0; i < firstDay; i++){
            gridPanel.add(new JLabel(""));
        }
        for(int d = 1; d <= daysInMonth; d++){
            final int day = d;
            boolean isToday = today.getYear() == currentMonth.getYear() && today.getMonth() == currentMonth.getMonth() && today.getDayOfMonth() == d;

            JButton btn = new JButton(String.valueOf(d));
            btn.setFont(new Font("Segoe UI", isToday ? Font.BOLD : Font.PLAIN, 13));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if(isToday){
                btn.setBackground(themeManager.getAccentColor());
                btn.setForeground(Color.WHITE);
            }else{
                btn.setBackground(themeManager.getTitleBarColor());
                btn.setForeground(themeManager.getTextColor());
            }

            gridPanel.add(btn);
        }

        int total = firstDay + daysInMonth;
        int remaining = (total % 7 == 0) ? 0 : 7 - (total % 7);
        for(int i = 0; i < remaining; i++){
            gridPanel.add(new JLabel(""));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
