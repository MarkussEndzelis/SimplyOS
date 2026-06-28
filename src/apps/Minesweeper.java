package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper extends JPanel {
    private ThemeManager themeManager;
    private static final int ROWS = 16;
    private static final int COLS = 16;
    private static final int MINES = 40;
    private static final int CELL_SIZE = 30;

    private int[][] board;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private boolean gameOver;
    private boolean won;
    private int remainingMines;
    private JLabel statusLabel;
    private JPanel gridPanel;

    public Minesweeper(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(themeManager.getTitleBarColor());
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        statusLabel = new JLabel("Mines: " + MINES, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(themeManager.getTextColor());

        JButton resetBtn = new JButton("New Game");
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resetBtn.setBackground(themeManager.getAccentColor());
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBorderPainted(false);
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(e -> resetGame());

        topBar.add(statusLabel, BorderLayout.CENTER);
        topBar.add(resetBtn, BorderLayout.EAST);

        gridPanel = new JPanel(new GridLayout(ROWS, COLS, 2, 2));
        gridPanel.setBackground(themeManager.getBackgroundColor());
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(topBar, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        resetGame();
    }

    private void resetGame(){
        board = new int[ROWS][COLS];
        revealed = new boolean[ROWS][COLS];
        flagged = new boolean[ROWS][COLS];
        gameOver = false;
        won = false;
        remainingMines = MINES;
        statusLabel.setText("Mines: " + MINES);

        placeMines();
        calculateNumbers();
        buildGrid();
    }
    
    private void placeMines(){
        int placed = 0;
        while(placed < MINES){
            int r = (int)(Math.random() * ROWS);
            int c = (int)(Math.random() * COLS);
            if(board[r][c] != -1){
                board[r][c] = - 1;
                placed++;
            }
        }
    }

    private void calculateNumbers(){
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                if(board[r][c] == -1){
                    continue;
                }
                int count = 0;
                for(int dr = -1; dr <= 1; dr++){
                    for(int dc = -1; dc <= 1; dc++){
                        int nr = r + dr, nc = c + dc;
                        if(nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && board[nr][nc] == -1){
                            count++;
                        }
                    }
                }
                board[r][c] = count;
            }
        }
    }

    private void buildGrid(){
        gridPanel.removeAll();
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                final int row = r, col = c;
                JButton cell = new JButton();
                cell.setFont(new Font("Segoe UI", Font.BOLD, 12));
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBackground(new Color(180, 180, 180));
                cell.setBorderPainted(true);
                cell.setFocusPainted(false);
                cell.setMargin(new Insets(0, 0, 0, 0));

                cell.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if(gameOver){
                            return;
                        }
                        if(SwingUtilities.isRightMouseButton(e)){
                            toggleFlag(row, col);
                        }else if(SwingUtilities.isLeftMouseButton(e)){
                            reveal(row, col);
                        }
                        buildGrid();
                        checkWin();
                    }
                });
                gridPanel.add(cell);
            }
        }
        updateGrid();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateGrid(){
        Component[] cells = gridPanel.getComponents();
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                JButton cell = (JButton) cells[r * COLS + c];
                cell.setText("");
                if(flagged[r][c] && !revealed[r][c]){
                    cell.setText("F");
                    cell.setForeground(Color.RED);
                    cell.setBackground(new Color(180, 180, 180));
                }else if(!revealed[r][c]){
                    cell.setBackground(new Color(180, 180, 180));
                }else if(board[r][c] == -1){
                    cell.setText("*");
                    cell.setBackground(Color.RED);
                    cell.setForeground(Color.WHITE);
                }else{
                    cell.setBackground(new Color(220, 220, 220));
                    if(board[r][c] > 0){
                        cell.setText(String.valueOf(board[r][c]));
                        cell.setForeground(getNumberColor(board[r][c]));
                    }
                }
            }
        }
    }

    private Color getNumberColor(int n){
        switch(n){
            case 1: return new Color(0,0,255);
            case 2: return new Color(0, 128, 0);
            case 3: return new Color(255, 0, 0);
            case 4: return new Color(0, 0, 128);
            case 5: return new Color(128, 0, 0);
            case 6: return new Color(0, 128, 128);
            case 7: return new Color(0, 0, 0);
            case 8: return new Color(128, 128, 128);
            default: return Color.BLACK;
        }
    }

    private void reveal(int r, int c){
        if(r < 0 || r >= ROWS || c < 0 || c >= COLS){
            return;
        }
        if(revealed[r][c] || flagged[r][c]){
            return;
        }
        revealed[r][c] = true;
        if(board[r][c] == -1){
            gameOver = true;
            revealAll();
            statusLabel.setText("Game Over!");
            return;
        }
        if(board[r][c] == 0){
            for(int dr = -1; dr <= 1; dr++){
                for(int dc = -1; dc <= 1; dc++){
                    reveal(r + dr, c + dc);
                }
            }
        }
    }
    private void toggleFlag(int r, int c){
        if(revealed[r][c]){
            return;
        }
        flagged[r][c] = !flagged[r][c];
        remainingMines += flagged[r][c] ? -1 : 1;
        statusLabel.setText("Mines: " + remainingMines);
    }
    private void revealAll(){
        for(int r =0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                revealed[r][c] = true;
            }
        }
    }
    private void checkWin(){
        int unrevealedSafe = 0;
        for(int r = 0; r < ROWS; r++){
            for(int c = 0; c < COLS; c++){
                if(!revealed[r][c] && board[r][c] != -1){
                    unrevealedSafe++;
                }
            }
        }
        if(unrevealedSafe == 0 && !gameOver){
            won = true;
            gameOver = true;
            statusLabel.setText("You Win!");
        }
    }
}
