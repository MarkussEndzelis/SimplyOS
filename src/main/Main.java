package main;

import desktop.Dekstop;
import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Desktop desktop = new Dekstop();
            desktop.launch();
        });
    }
}