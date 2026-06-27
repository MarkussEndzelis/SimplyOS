package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JPanel{
    private ThemeManager themeManager;
    private JTextArea textArea;
    private File currentFile;
    private JLabel statusLabel;

    public TextEditor(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel menuBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        menuBar.setBackground(themeManager.getTitleBarColor());
        menuBar.setPreferredSize(new Dimension(0, 30));

        JButton newBtn = createMenuButton("New");
        JButton openBtn = createMenuButton("Open");
        JButton saveBtn = createMenuButton("Save");
        JButton saveAsBtn = createMenuButton("Save As");

        newBtn.addActionListener(e -> newFile());
        openBtn.addActionListener(e -> openFile());
        saveBtn.addActionListener(e -> saveFile());
        saveAsBtn.addActionListener(e -> saveFileAs());

        menuBar.add(newBtn);
        menuBar.add(openBtn);
        menuBar.add(saveBtn);
        menuBar.add(saveAsBtn);

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setBackground(themeManager.getWindowColor());
        textArea.setForeground(themeManager.getTextColor());
        textArea.setCaretColor(themeManager.getTextColor());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        statusLabel = new JLabel("  New File");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(themeManager.getTextColor());
        statusLabel.setBackground(themeManager.getTitleBarColor());
        statusLabel.setOpaque(true);
        statusLabel.setPreferredSize(new Dimension(0, 22));

        add(menuBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(themeManager.getTextColor());
        btn.setBackground(themeManager.getTitleBarColor());
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void newFile(){
        textArea.setText("");
        currentFile = null;
        statusLabel.setText("  New File");
    }

    private void openFile(){
        JFileChooser chooser = new JFileChooser();
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            try{
                BufferedReader reader = new BufferedReader(new FileReader(currentFile));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                }
                reader.close();
                textArea.setText(sb.toString());
                statusLabel.setText("  " + currentFile.getName());
            }catch(IOException ex){
                JOptionPane.showMessageDialog(this, "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void saveFile(){
        if(currentFile == null){
            saveFileAs();
        }else{
            writeFile(currentFile);
        }
    }

    private void saveFileAs(){
        JFileChooser chooser = new JFileChooser();
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            writeFile(currentFile);
        }
    }

    private void writeFile(File file){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(textArea.getText());
            writer.close();
            statusLabel.setText("  Saved: " + file.getName());
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}