package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Terminal extends JPanel{
    private ThemeManager themeManager;
    private JTextArea outputArea;
    private JTextField inputField;
    private File currentDir;

    public Terminal(ThemeManager themeManager){
        this.themeManager = themeManager;
        currentDir = new File(System.getProperty("user.home"));
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        outputArea = new JTextArea();
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(new Color(0, 255, 0));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.BLACK);

        JLabel prompt = new JLabel(" > ");
        prompt.setFont(new Font("Consolas", Font.BOLD, 13));
        prompt.setForeground(new Color(0, 255, 0));

        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0, 255, 0));
        inputField.setCaretColor(new Color(0, 255, 0));
        inputField.setFont(new Font("Consolas", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        inputField.addActionListener(e -> handleCommand(inputField.getText()));

        inputPanel.add(prompt, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        print("SimplyOS Terminal v1.0");
        print("Type 'help' for available commands.");
        print("");
    }

    private void handleCommand(String cmd){
        cmd = cmd.trim();
        inputField.setText("");
        print("> " + cmd);

        if(cmd.isEmpty()){
            return;
        }
        
        switch(cmd.toLowerCase()){
            case "help":
                print("Available commands:");
                print("  help        - show this help");
                print("  clear       - clear terminal");
                print("  ls          - list files");
                print("  pwd         - print working directory");
                print("  cd <dir>    - change directory");
                print("  echo <msg>  - print message");
                print("  date        - show current date");
                print("  sysinfo     - show system info");
                break;
            case "clear":
                outputArea.setText("");
                break;
            case "ls":
                File[] files = currentDir.listFiles();
                if(files != null){
                    for(File f : files){
                        if(!f.isHidden()){
                            print((f.isDirectory() ? "[DIR] " : "[FILE] ") + f.getName());
                        }
                    }
                }
                break;
            case "pwd":
                print(currentDir.getAbsolutePath());
                break;
            case "date":
                print(new java.util.Date().toString());
                break;
            case "sysinfo":
                print("OS: " + System.getProperty("os.name"));
                print("User: " + System.getProperty("user.name"));
                print("Java: " + System.getProperty("java.version"));
                print("Memory: " + Runtime.getRunTime().totalMemory() / 1024 / 1024 + "MB");
                break;
            default:
                if(cmd.startsWith("cd ")){
                    String dirName = cmd.substring(3).trim();
                    File newDir = new File(currentDir, dirName);
                    if(newDir.exists() && newDir.isDirectory()){
                        currentDir = newDir;
                        print("Changed to: " + currentDir.getAbsolutePath());
                    }else{
                        print("Directory not found: " + dirName);
                    }
                }else if(cmd.startsWith("echo ")){
                    print(cmd.substring(5));
                }else{
                    print("Unknown command: " + cmd + ". Type 'help' for commands.");
                }
                break;
        }
        print("");
    }
    private void print(String text){
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

}