package apps;

import themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pong extends JPanel{
    private ThemeManager themeManager;

    private int ballX = 350, ballY = 250;
    private int ballDX = 4, ballDY = 3;
    private int ballSize = 12;

    private int paddle1Y = 200, paddle2Y = 200;
    private int paddleWidth = 12, paddleHeight = 80;
    private int paddleSpeed = 5;

    private int score1 = 0, score2 = 0;
    private boolean gameRunning = false;
    private boolean gameOver = false;

    private boolean wUp = false, wDown = false;
    private boolean upKey = false, downKey = false;

    private Timer gameTimer;
    private JLabel statusLabel;

    public Pong(ThemeManager themeManager){
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(20, 20, 20));
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        statusLabel = new JLabel("Press Space to Start | W/S = Left Paddle | UP/DOWN = Right Paddle", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        resetBtn.setBackground(new Color(60, 60, 60));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBorderPainted(false);
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetBtn.addActionListener(e -> resetGame());

        topBar.add(statusLabel, BorderLayout.CENTER);
        topBar.add(resetBtn, BorderLayout.EAST);

        JPanel gameCanvas = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(60, 60, 60));
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
                g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Consolas", Font.BOLD, 36));
                g2.setStroke(new BasicStroke(1));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(String.valueOf(score1), getWidth() / 2 - 60 - fm.stringWidth(String.valueOf(score1)), 50);
                g2.drawString(String.valueOf(score2), getWidth() / 2 + 60, 50);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(20, paddle1Y, paddleWidth, paddleHeight, 6, 6);
                g2.fillRoundRect(getWidth() - 20 - paddleWidth, paddle2Y, paddleWidth, paddleHeight, 6, 6);

                g2.setColor(Color.WHITE);
                g2.fillOval(ballX - ballSize / 2, ballY - ballSize / 2, ballSize, ballSize);

                if(!gameRunning){
                    g2.setColor(new Color(0, 0, 0, 160));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    String msg = gameOver ? "Game Over! Press Space" : "Press Space to Start";
                    FontMetrics fm2 = g2.getFontMetrics();
                    g2.drawString(msg, (getWidth() - fm2.stringWidth(msg)) / 2, getHeight() / 2);

                }
            }
        };
        gameCanvas.setBackground(Color.BLACK);
        gameCanvas.setFocusable(true);

        gameCanvas.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_W: wUp = true; break;
                    case KeyEvent.VK_S: wDown = true; break;
                    case KeyEvent.VK_UP: upKey = true; break;
                    case KeyEvent.VK_DOWN: downKey = true; break;
                    case KeyEvent.VK_SPACE:
                        if(!gameRunning){
                            if(gameOver){
                                resetGame();
                            }
                            gameRunning = true;
                            statusLabel.setText("W/S = Left Paddle  |  UP/DOWN = Right Paddle");
                        }
                    break;
                }
            }

            public void keyReleased(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_W: wUp = false; break;
                    case KeyEvent.VK_S: wDown = false; break;
                    case KeyEvent.VK_UP: upKey = false; break;
                    case KeyEvent.VK_DOWN: downKey = false; break;
                }
            }
        });

        gameTimer = new Timer(16, e -> {
            if(!gameRunning){
                return;
            }
            if(wUp && paddle1Y > 0){
                paddle1Y -= paddleSpeed;
            }
            if(wDown && paddle1Y < gameCanvas.getHeight() - paddleHeight){
                paddle1Y += paddleSpeed;
            }
            if(upKey && paddle2Y > 0){
                paddle2Y -= paddleSpeed;
            }
            if(downKey && paddle2Y < gameCanvas.getHeight() - paddleHeight){
                paddle2Y += paddleSpeed;
            }

            ballX += ballDX;
            ballY += ballDY;

            if(ballY <= ballSize / 2 || ballY >= gameCanvas.getHeight() - ballSize / 2){
                ballDY = - ballDY;
                ballY = Math.max(ballSize / 2, Math.min(ballY, gameCanvas.getHeight() - ballSize / 2));
            }

            if(ballX - ballSize / 2 <= 20 + paddleWidth && ballY >= paddle1Y && ballY <= paddle1Y + paddleHeight && ballDX < 0){
                ballDX = -ballDX;
                ballDX = (int)(ballDX * 1.05);
                float hitPos = (ballY - paddle1Y) / (float) paddleHeight;
                ballDY = (int)((hitPos - 0.5f) * 10);
            }
            
            if(ballX + ballSize / 2 >= gameCanvas.getWidth() - 20 - paddleWidth && ballY >= paddle2Y && ballY <= paddle2Y + paddleHeight && ballDX > 0){
                ballDX = -ballDX;
                ballDX = (int)(ballDX * 1.05);
                float hitPos = (ballY - paddle2Y) / (float) paddleHeight;
                ballDY = (int)(hitPos - 0.5f) * 10;
            }

            if(ballX < 0){
                score2++;
                checkWin(gameCanvas);
                resetBall(gameCanvas);
            }else if(ballX > gameCanvas.getWidth()){
                score1++;
                checkWin(gameCanvas);
                resetBall(gameCanvas);
            }
            gameCanvas.repaint();
        });
        gameTimer.start();

        gameCanvas.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                gameCanvas.requestFocusInWindow();
            }
        });

        add(topBar, BorderLayout.NORTH);
        add(gameCanvas, BorderLayout.CENTER);

        SwingUtilities.invokeLater(gameCanvas::requestFocusInWindow);
    }

    private void resetBall(JPanel canvas){
        ballX = canvas.getWidth() / 2;
        ballY = canvas.getHeight() / 2;
        ballDX = (Math.random() > 0.5 ? 4 : -4);
        ballDY = (Math.random() > 0.5 ? 3 : -3);
    }

    private void checkWin(JPanel canvas){
        if(score1 >= 7 || score2 >= 7){
            gameRunning = false;
            gameOver = true;
            String winner = score1 >= 7 ? "Left" : "Right";
            statusLabel.setText(winner + " player wins! Press Space to play again.");
        }
    }

    private void resetGame(){
        score1 = 0;
        score2 = 0;
        ballDX = 4;
        ballDY = 3;
        paddle1Y = 200;
        paddle2Y = 200;
        gameOver = false;
        gameRunning = false;
        statusLabel.setText("Press Space to Start  |  W/S = Left Paddle  |  UP/DOWN = Right Paddle");
        repaint();
    }
}
