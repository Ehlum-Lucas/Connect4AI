import javax.swing.*;
import java.awt.*;

public class ConnectFourGUI extends JFrame {
    private final int ROWS = 6;
    private final int COLS = 7;
    private JButton[] columnButtons;
    private int[][] board;
    private boolean playerTurn = true;
    private boolean singlePlayerMode = true;

    private Position position;
    private ConnectFourAI ai;

    public ConnectFourGUI() {
        setTitle("Connect 4");
        setSize(710, 670);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        board = new int[ROWS][COLS];
        position = new Position();
        ai = new ConnectFourAI();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        columnButtons = new JButton[COLS];
        for (int i = 0; i < COLS; i++) {
            int col = i;
            columnButtons[i] = new JButton("↓");
            columnButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            columnButtons[i].setPreferredSize(new Dimension(100, 40));
            columnButtons[i].setMaximumSize(new Dimension(100, 40));
            columnButtons[i].addActionListener(e -> handleMove(col));
            topPanel.add(columnButtons[i]);
        }
        add(topPanel, BorderLayout.NORTH);

        add(new BoardPanel(), BorderLayout.CENTER);

        String[] options = {"Single Player", "Two Players"};
        int mode = JOptionPane.showOptionDialog(this, "Choose Game Mode", "Mode Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        singlePlayerMode = (mode == 0);
    }

    private void handleMove(int col) {
        if (!position.isValidMove(col)) return;

        int row = getAvailableRow(col);
        if (row == -1) return;

        board[row][col] = playerTurn ? 1 : 2;

        repaint();

        if (position.isWinningMove(col)) {
            String message = singlePlayerMode ? "You win!" : (playerTurn ? "Player 1 wins!" : "Player 2 wins!");
            JOptionPane.showMessageDialog(this, message);
            resetGame();
            return;
        }
        if (position.getMoves() == Position.WIDTH * Position.HEIGHT - 1) {
            String message = "Draw!";
            JOptionPane.showMessageDialog(this, message);
            resetGame();
            return;
        }
        position.play(col);

        playerTurn = !playerTurn;

        if (singlePlayerMode && !playerTurn) {
            aiMove();
        }
    }

    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        int depth = 8;

        for (int col = 0; col < COLS; col++) {
            if (position.isValidMove(col)) {
                Position next = new Position(position);
                next.play(col);
                int score = -ai.solve(next, depth - 1)[0];
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }

        if (bestCol == -1) return;

        int row = getAvailableRow(bestCol);
        if (row == -1) return;

        board[row][bestCol] = 2;


        if (position.isWinningMove(bestCol)) {
            JOptionPane.showMessageDialog(this, "AI wins!");
            resetGame();
            return;
        }

        position.play(bestCol);
        repaint();

        playerTurn = true;
    }

    private int getAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) return row;
        }
        return -1;
    }

    private void resetGame() {
        board = new int[ROWS][COLS];
        position = new Position();
        playerTurn = true;
        repaint();
    }

    private class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    g.setColor(Color.BLUE);
                    g.fillRect(col * 100, row * 100, 100, 100);
                    g.setColor(Color.WHITE);
                    if (board[row][col] == 1) g.setColor(Color.RED);
                    else if (board[row][col] == 2) g.setColor(Color.YELLOW);
                    g.fillOval(col * 100 + 10, row * 100 + 10, 80, 80);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnectFourGUI().setVisible(true));
    }
}
