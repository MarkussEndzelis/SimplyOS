package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Chess extends JPanel{
    private ThemeManager themeManager;
    private int[][] board = new int[8][8];
    private int selectedRow = -1, selectedCol = -1;
    private boolean whiteTurn = true;
    private JLabel statusLabel;
    private JPanel boardPanel;

    private static final int PAWN = 1, ROOK = 2, KNIGHT = 3;
    private static final int BISHOP = 4, QUEEN = 5, KING = 6;

    private static final String[] PIECE_CHARS = {"", "♟", "♜", "♞", "♝", "♛", "♚"};

    public Chess(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(themeManager.getWindowColor());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(themeManager.getTitleBarColor());
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        statusLabel = new JLabel("White's turn", SwingConstants.CENTER);
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

        boardPanel = new JPanel(new GridLayout(8, 8)){
            @Override
            public Dimension getPreferredSize(){
              int size = Math.min(getParent().getWidth(), getParent().getHeight());
              return new Dimension(size, size);
            }
        };
        boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(themeManager.getWindowColor());
        centerWrapper.add(boardPanel);

        add(topBar, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        resetGame();
        buildBoard();
    }

    private void resetGame(){
        for(int r = 0; r < 8; r++)
            for(int c = 0; c < 8; c++)
                board[r][c] = 0;


        board[0][0] = -ROOK; board[0][1] = -KNIGHT; board[0][2] = -BISHOP;
        board[0][3] = -QUEEN; board[0][4] = -KING; board[0][5] = -BISHOP;
        board[0][6] = -KNIGHT; board[0][7] = -ROOK;
        for(int c = 0; c < 8; c++){
            board[1][c] = -PAWN;
        }

        board[7][0] = ROOK; board[7][1] = KNIGHT; board[7][2] = BISHOP;
        board[7][3] = QUEEN; board[7][4] = KING; board[7][5] = BISHOP;
        board[7][6] = KNIGHT; board[7][7] = ROOK;
        for(int c = 0; c < 8; c++){
            board[6][c] = PAWN;
        }

        selectedRow = -1;
        selectedCol = -1;
        whiteTurn = true;
        if(statusLabel != null){
            statusLabel.setText("White's turn");
        }
        buildBoard();
    }

    private void buildBoard(){
        boardPanel.removeAll();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                final int row = r, col = c;
                boolean light = (r + c) % 2 == 0;
                boolean isSelected = (r == selectedRow && c == selectedCol);
                boolean isValidMove = selectedRow != -1 && isValidMove(selectedRow, selectedCol, r, c);

                JPanel cell = new JPanel(new BorderLayout());

                if(isSelected){
                    cell.setBackground(new Color(100, 180, 100));
                }else if(isValidMove){
                    cell.setBackground(new Color(180, 220, 100));
                }else{
                    cell.setBackground(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                }

                if(board[r][c] != 0){
                    int piece = Math.abs(board[r][c]);
                    boolean isWhite = board[r][c] > 0;
                    JLabel lbl = new JLabel(PIECE_CHARS[piece], SwingConstants.CENTER);
                    lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                    lbl.setForeground(isWhite ? Color.WHITE : new Color(30, 30, 30));
                    lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    cell.add(lbl, BorderLayout.CENTER);
                }

                cell.addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        handleClick(row, col);
                    }
                    public void mouseEntered(MouseEvent e){
                        if(!isSelected && !isValidMove){
                            cell.setBackground(light ? new Color(220, 200, 160) : new Color(160, 116, 79));
                        }
                    }
                    public void mouseExited(MouseEvent e){
                        if(isSelected){
                            cell.setBackground(new Color(100, 180, 100));
                        }else if(isValidMove){
                            cell.setBackground(new Color(180, 220, 100));
                        }else{
                            cell.setBackground(light ? new Color(240, 217, 181) : new Color(181, 136, 99));
                        }
                    }
                });
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void handleClick(int row, int col){
        if(selectedRow == -1){
            if(board[row][col] != 0){
                boolean pieceIsWhite = board[row][col] > 0;
                if(pieceIsWhite == whiteTurn){
                    selectedRow = row;
                    selectedCol = col;
                    buildBoard();
                }
            }
        }else{
            if(isValidMove(selectedRow, selectedCol, row, col)){
                if(Math.abs(board[row][col]) == KING){
                    board[row][col] = board[selectedRow][selectedCol];
                    board[selectedRow][selectedCol] = 0;
                    selectedRow = -1;
                    selectedCol = -1;
                    buildBoard();
                    statusLabel.setText((whiteTurn ? "White" : "Black") + " wins!");
                    return;
                }

                int piece = board[selectedRow][selectedCol];
                board[row][col] = piece;
                board[selectedRow][selectedCol] = 0;
                if(Math.abs(piece) == PAWN && (row == 0 || row == 7)){
                    board[row][col] = piece > 0 ? QUEEN : -QUEEN;
                }

                whiteTurn = !whiteTurn;
                selectedRow = -1;
                selectedCol = -1;
                statusLabel.setText((whiteTurn ? "White" : "Black") + "'s turn");
            }else if(board[row][col] != 0 && (board[row][col] > 0) == whiteTurn){
                selectedRow = row;
                selectedCol = col;
            }else{
                selectedRow = -1;
                selectedCol = -1;
            }
            buildBoard();
        }
    }
    private boolean isValidMove(int fromR, int fromC, int toR, int toC){
        if(fromR == toR && fromC == toC){
            return false;
        }
        int piece = board[fromR][fromC];
        int target = board[toR][toC];
        if(piece == 0){
            return false;
        }
        if(target != 0 && (target > 0) == (piece > 0)){
            return false;
        }
        int type = Math.abs(piece);
        boolean isWhite = piece > 0;
        int dr = toR - fromR, dc = toC - fromC;

        switch(type){
            case PAWN:
                int dir = isWhite ? -1 : 1;
                int startRow = isWhite ? 6 : 1;
                if(dc == 0 && dr == dir && target == 0){
                    return true;
                }
                if(dc == 0 && dr == 2 * dir && fromR == startRow && target == 0 && board[fromR + dir][fromC] == 0){
                    return true;
                }
                if(Math.abs(dc) == 1 && dr == dir && target != 0){
                    return true;
                }
                return false;
            case ROOK:
                if(dr != 0 && dc != 0){
                    return false;
                }
                return pathClear(fromR, fromC, toR, toC);
            case KNIGHT:
                return(Math.abs(dr) == 2 && Math.abs(dc) == 1) || (Math.abs(dr) == 1 && Math.abs(dc) == 2);
            case BISHOP:
                if(Math.abs(dr) != Math.abs(dc)){
                    return false;
                }
                return pathClear(fromR, fromC, toR, toC);
            case QUEEN:
                if(dr != 0 && dc != 0 && Math.abs(dr) != Math.abs(dc)){
                    return false;
                }
                return pathClear(fromR, fromC, toR, toC);
            case KING:
                return Math.abs(dr) <= 1 && Math.abs(dc) <= 1;
        }
        return false;
    }

    private boolean pathClear(int fromR, int fromC, int toR, int toC){
        int dr = Integer.signum(toR - fromR);
        int dc = Integer.signum(toC - fromC);
        int r = fromR + dr, c = fromC + dc;
        while(r != toR || c != toC){
            if(board[r][c] != 0){
                return false;
            }
            r += dr;
            c += dc;
        }
        return true;
    }
}
