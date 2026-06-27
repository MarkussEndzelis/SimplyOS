package main;

import desktop.Desktop;
import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Desktop desktop = new Desktop();
            desktop.launch();
        });
    }
}