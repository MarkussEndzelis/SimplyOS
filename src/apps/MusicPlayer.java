package apps;

import themes.ThemeManager;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MusicPlayer extends JPanel {
    private ThemeManager themeManager;
    private Clip clip;
    private boolean playing = false;
    private File currentFile;
    private JLabel songLabel;
    private JLabel statusLabel;
    private JSlider volumeSlider;
    private JButton playBtn;
    private DefaultListModel<String> playlistModel;
    private JList<String> playlist;
    private java.util.List<File> files = new java.util.ArrayList<>();

    public MusicPlayer(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(themeManager.getTitleBarColor());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        songLabel = new JLabel("No song selected", SwingConstants.CENTER);
        songLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        songLabel.setForeground(themeManager.getTextColor());

        statusLabel = new JLabel("Stopped", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(themeManager.getTextColor());

        infoPanel.add(songLabel, BorderLayout.CENTER);
        infoPanel.add(statusLabel, BorderLayout.SOUTH);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        controls.setBackground(themeManager.getTitleBarColor());

        JButton prevBtn = createBtn("|<");
        playBtn = createBtn("Play");
        JButton stopBtn = createBtn("Stop");
        JButton nextBtn = createBtn(">|");
        JButton addBtn = createBtn("+ Add");

        controls.add(prevBtn);
        controls.add(playBtn);
        controls.add(stopBtn);
        controls.add(nextBtn);
        controls.add(addBtn);

        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        volumePanel.setBackground(themeManager.getTitleBarColor());
        JLabel volLabel = new JLabel("Volume:");
        volLabel.setForeground(themeManager.getTextColor());
        volLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        volumeSlider = new JSlider(0, 100, 80);
        volumeSlider.setPreferredSize(new Dimension(120, 24));
        volumeSlider.setBackground(themeManager.getTitleBarColor());
        volumeSlider.addChangeListener(r -> setVolume(volumeSlider.getValue()));
        volumePanel.add(volLabel);
        volumePanel.add(volumeSlider);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(infoPanel, BorderLayout.NORTH);
        topSection.add(controls, BorderLayout.CENTER);
        topSection.add(volumePanel, BorderLayout.SOUTH);

        playlistModel = new DefaultListModel<>();
        playlist = new JList<>(playlistModel);
        playlist.setBackground(themeManager.getWindowColor());
        playlist.setForeground(themeManager.getTextColor());
        playlist.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        playlist.setSelectionBackground(themeManager.getAccentColor());
        playlist.setSelectionForeground(Color.WHITE);
        playlist.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int idx = playlist.getSelectedIndex();
                    if(idx >= 0){
                        playSong(files.get(idx));
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(playlist);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(themeManager.getBorderColor()),
            "Playlist",
            0, 0,
            new Font("Segoe UI", Font.PLAIN, 11),
            themeManager.getTextColor()
        ));

        playBtn.addActionListener(e -> togglePlay());
        stopBtn.addActionListener(e -> stopSong());
        prevBtn.addActionListener(e -> playAdjacentSong(-1));
        nextBtn.addActionListener(e -> playAdjacentSong(1));
        addBtn.addActionListener(e -> addFiles());

        add(topSection, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    private JButton createBtn(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(themeManager.getAccentColor());
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(70, 30));
        return btn;
    }

    private void addFiles(){
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("WAV files", "wav"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            for(File f : chooser.getSelectedFiles()){
                files.add(f);
                playlistModel.addElement(f.getName());
            }
        }
    }

    private void playSong(File file){
        stopSong();
        currentFile = file;
        try{
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audio);
            setVolume(volumeSlider.getValue());
            clip.start();
            playing = true;
            playBtn.setText("Pause");
            songLabel.setText(file.getName());
            statusLabel.setText("Playing");
            clip.addLineListener(e -> {
                if(e.getType() == LineEvent.Type.STOP && playing){
                    SwingUtilities.invokeLater(() -> {
                        playing = false;
                        playBtn.setText("Play");
                        statusLabel.setText("Stopped");
                        playAdjacentSong(1);
                    });
                }
            });
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error playing file: " + ex.getMessage());
        }
    }

    private void togglePlay(){
        if(clip == null){
            if(!files.isEmpty()){
                playSong(files.get(0));
                return;
            }
        }
        if(playing){
            clip.stop();
            playing = false;
            playBtn.setText("Play");
            statusLabel.setText("Paused");
        }else{
            clip.setMicrosecondPosition(clip.getMicrosecondPosition());
            clip.start();
            playing = true;
            playBtn.setText("Pause");
            statusLabel.setText("Playing");
        }
    }
    private void stopSong(){
        if(clip != null){
            clip.stop();
            clip.close();
            clip = null;
        }
        playing = false;
        playBtn.setText("Play");
        statusLabel.setText("Stopped");
    }
    
    private void playAdjacentSong(int direction){
        if(files.isEmpty()){
            return;
        }
        int idx = currentFile != null ? files.indexOf(currentFile) : -1;
        idx = (idx + direction + files.size()) % files.size();
        playSong(files.get(idx));
        playlist.setSelectedIndex(idx);
    }
    private void setVolume(int value){
        if(clip == null){
            return;
        }
        FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float)(Math.log(value / 100.0) / Math.log(10.0) * 20.0);
        vol.setValue(Math.max(vol.getMinimum(), Math.min(dB, vol.getMaximum())));
    }

    public void cleanup(){
        stopSong();
    }
}
