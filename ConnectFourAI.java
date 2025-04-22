public class ConnectFourAI {
    private long nodes;

    private int[] negamax(Position pos, int depth) {
        nodes++;

        if (pos.getMoves() == Position.WIDTH * Position.HEIGHT || depth == 0)
            return new int[] {0, -1}; // Return score 0 and invalid move (-1) for draw or depth limit

        for (int x = 0; x < Position.WIDTH; x++) {
            if (pos.isValidMove(x) && pos.isWinningMove(x))
                return new int[] {(Position.WIDTH * Position.HEIGHT + 1 - pos.getMoves()) / 2, x}; // Return winning score and move
        }

        int max = -Position.WIDTH * Position.HEIGHT;
        int bestMove = -1;

        for (int x = 0; x < Position.WIDTH; x++) {
            if (pos.isValidMove(x)) {
                Position p2 = new Position(pos);
                p2.play(x);
                int score = -negamax(p2, depth - 1)[0];
                if (score > max) {
                    max = score;
                    bestMove = x;
                }
            }
        }
        return new int[] {max, bestMove}; // Return best score and corresponding move
    }

    public int[] solve(Position pos, int depth) {
        nodes = 0;
        return negamax(pos, depth); // Return the best move
    }

    public long getNodes() {
        return nodes;
    }
}