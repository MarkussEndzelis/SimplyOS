package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JPanel{
    private ThemeManager themeManager;
    private JTextField display;
    private String currentInput = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean newInput = true;

    public Calculator(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());
        setPreferredSize(new Dimension(280, 380));

        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(themeManager.getWindowColor());
        display.setForeground(themeManager.getTextColor());
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        display.setPreferredSize(new Dimension(0, 70));

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 4, 4));
        buttonsPanel.setBackground(themeManager.getWindowColor());
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        String[] buttons = {
            "C", "+/2", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "DEL", "="
        };

        for(String btn : buttons){
            buttonsPanel.add(createButton(btn));
        }

        add(display, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if(text.equals("=")){
            btn.setBackground(themeManager.getAccentColor());
            btn.setForeground(Color.WHITE);
        }else if(text.equals("/") || text.equals("*") || text.equals("-") || text.equals("+")){
            btn.setBackground(new Color(255, 149, 0));
            btn.setForeground(Color.WHITE);
        }else if(text.equals("C") || text.equals("+/-") || text.equals("%") || text.equals("DEL")){
            btn.setBackground(themeManager.getTitleBarColor());
            btn.setForeground(themeManager.getTextColor());
        }else{
            btn.setBackground(themeManager.getWindowColor().darker());
            btn.setForeground(themeManager.getTextColor());
        }
        btn.addActionListener(e -> handleButton(text));
        return btn;
    }

    private void handleButton(String text){
        switch(text){
            case "C":
                currentInput = "";
                firstNumber = 0;
                operator = "";
                newInput = true;
                display.setText("0");
                break;
            case "DEL":
                if(currentInput.length() > 0){
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    display.setText(currentInput.isEmpty() ? "0" : currentInput);
                }
                break;
            case "+/-":
                if(!currentInput.isEmpty()){
                    double val = Double.parseDouble(currentInput) * -1;
                    currentInput = String.valueOf(val);
                    display.setText(currentInput);
                }
                break;
            case "%":
                if(!currentInput.isEmpty()){
                    double val = Double.parseDouble(currentInput) / 100;
                    currentInput = String.valueOf(val);
                    display.setText(currentInput);
                }
                break;
            case "/": case "*": case "-": case "+":
                if(!currentInput.isEmpty()){
                    firstNumber = Double.parseDouble(currentInput);
                    operator = text;
                    newInput = true;
                    currentInput = "";
                } 
                break;
            case "=":
                if(!operator.isEmpty() && !currentInput.isEmpty()){
                    double secondNumber = Double.parseDouble(currentInput);
                    double result = 0;
                    switch(operator){
                        case "/": result = firstNumber / secondNumber; break;
                        case "*": result = firstNumber * secondNumber; break;
                        case "-": result = firstNumber - secondNumber; break;
                        case "+": result = firstNumber + secondNumber; break;
                    }
                    String resultStr = result == (long) result ? String.valueOf((long) result) : String.valueOf(result);
                    display.setText(resultStr);
                    currentInput = resultStr;
                    operator = "";
                    newInput = true;
                }
                break;
            case ".":
                if(!currentInput.contains(".")){
                    currentInput += currentInput.isEmpty() ? "0." : ".";
                    display.setText(currentInput);
                }
                break;
            default:
                if(newInput){
                    currentInput = text;
                    newInput = false;
                }else{
                    currentInput += text;
                }
                display.setText(currentInput);
                break;
        }
    }
}