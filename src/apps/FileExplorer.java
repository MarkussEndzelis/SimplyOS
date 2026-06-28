package apps;

import themes.ThemeManager;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FileExplorer extends JPanel{
    private ThemeManager themeManager;
    private JTree fileTree;
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JLabel pathLabel;
    private File currentDir;

    public FileExplorer(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(themeManager.getTitleBarColor());
        topBar.setPreferredSize(new Dimension(0, 35));

        JButton backBtn = new JButton("<-");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(themeManager.getTitleBarColor());
        backBtn.setForeground(themeManager.getTextColor());
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(40, 35));
        backBtn.addActionListener(e -> goBack());

        pathLabel = new JLabel("  " + System.getProperty("user.home"));
        pathLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pathLabel.setForeground(themeManager.getTextColor());

        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(pathLabel, BorderLayout.CENTER);

        JPanel quickAccess = new JPanel();
        quickAccess.setLayout(new BoxLayout(quickAccess, BoxLayout.Y_AXIS));
        quickAccess.setBackground(themeManager.getWindowColor());
        quickAccess.setPreferredSize(new Dimension(130, 0));
        quickAccess.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, themeManager.getBorderColor()));

        String[] quickDirs = {"Home", "Desktop", "Documents", "Downloads", "Pictures"};
        File[] quickPaths = {
            new File(System.getProperty("user.home")),
            new File(System.getProperty("user.home") + "/Desktop"),
            new File(System.getProperty("user.home") + "/Documents"),
            new File(System.getProperty("user.home") + "/Downloads"),
            new File(System.getProperty("user.home") + "/Pictures")
        };

        for (int i = 0; i < quickDirs.length; i++){
            final File path = quickPaths[i];
            JButton btn = new JButton(quickDirs[i]);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btn.setForeground(themeManager.getTextColor());
            btn.setBackground(themeManager.getWindowColor());
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(130, 32));
            btn.addActionListener(e -> loadDirectory(path));
            quickAccess.add(btn);
        }

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setBackground(themeManager.getWindowColor());
        fileList.setForeground(themeManager.getTextColor());
        fileList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fileList.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    String selected = fileList.getSelectedValue();
                    if(selected != null){
                        File f = new File(currentDir, selected.replace("[DIR] ", ""));
                        if(f.isDirectory()){
                            loadDirectory(f);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(themeManager.getWindowColor());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, quickAccess, scrollPane);
        splitPane.setDividerSize(1);
        splitPane.setDividerLocation(130);

        add(topBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        currentDir = new File(System.getProperty("user.home"));
        loadDirectory(currentDir);
    }

    private void loadDirectory(File dir){
        if(dir == null || !dir.exists()){
            return;
        }
        currentDir = dir;
        pathLabel.setText("  " + dir.getAbsolutePath());
        listModel.clear();
        File[] files = dir.listFiles();
        if(files == null){
            return;
        }
        java.util.Arrays.sort(files, (a, b) -> {
            if(a.isDirectory() && !b.isDirectory()){
                return -1;
            }
            if(!a.isDirectory() && !b.isDirectory()){
                return 1;
            }
            return a.getName().compareToIgnoreCase(b.getName());
        });
        for (File f : files){
            if(!f.isHidden()){
                listModel.addElement(f.isDirectory() ? "[DIR] " + f.getName() :  f.getName());
            }
        }
    }
    private void goBack(){
        if(currentDir != null && currentDir.getParentFile() != null){
            loadDirectory(currentDir.getParentFile());
        }
    }
}