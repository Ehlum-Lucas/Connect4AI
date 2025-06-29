import java.util.Random;

public class Position {
    static final int WIDTH = 7;
    static final int HEIGHT = 6;
    private static final int NUM_PLAYERS = 2;
    private int[][] board;
    private int[] height;
    private int moves;
    private long currentZobristHash;

    // Zobrist keys: [column][row][player_index]
    // player_index 0 for player 1, player_index 1 for player 2
    private static final long[][][] zobristKeys = new long[WIDTH][HEIGHT][NUM_PLAYERS];
    private static final Random random = new Random();

    static {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                for (int k = 0; k < NUM_PLAYERS; k++) {
                    zobristKeys[i][j][k] = random.nextLong();
                }
            }
        }
    }

    public Position() {
        board = new int[WIDTH][HEIGHT];
        height = new int[WIDTH];
        moves = 0;
        currentZobristHash = 0L;
    }

    public Position(Position pos) {
        board = new int[WIDTH][HEIGHT];
        height = new int[WIDTH];
        moves = pos.moves;
        currentZobristHash = pos.currentZobristHash;
        for (int x = 0; x < WIDTH; x++) {
            height[x] = pos.height[x];
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = pos.board[x][y];
            }
        }
    }

    public boolean isValidMove(int col) {
        return height[col] < HEIGHT;
    }

    public boolean isWinningMove(int col) {
        int player = moves % 2 + 1;

        if (height[col] >= 3 && board[col][height[col] - 1] == player && board[col][height[col] - 2] == player && board[col][height[col] - 3] == player) return true; //check vertical

        // check horizontal and two diagonals
        for (int dy = -1; dy <= 1; dy++) {
            int nb = 0;
            for (int dx = -1; dx <= 1; dx += 2) {
                for (int x = col + dx, y = height[col] + dx * dy; x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && board[x][y] == player; x += dx, y += dx * dy) nb++;
                if (nb >= 3) return true;
            }
        }
        return false;
    }

    public void play(int col) {
        board[col][height[col]] = moves % 2 + 1;
        currentZobristHash ^= zobristKeys[col][height[col]][moves % 2];
        height[col]++;
        moves++;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= WIDTH || !isValidMove(col) || isWinningMove(col)) return i;
            play(col);
        }
        return seq.length();
    }

    public void printBoard() {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(board[x][y]);
            }
            System.out.println();
        }
    }

    public int getMoves() {
        return moves;
    }

    public long getZobristHash() {
        return currentZobristHash;
    }
}
