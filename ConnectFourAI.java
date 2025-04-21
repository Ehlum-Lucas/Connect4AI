public class ConnectFourAI {
    private long nodes;

    private int negamax(Position pos, int depth) {
        nodes++;

        if (pos.getMoves() == Position.WIDTH * Position.HEIGHT || depth == 0) return 0; // check for draw
        for (int x = 0; x < Position.WIDTH; x++) {
            if (pos.isValidMove(x) && pos.isWinningMove(x)) return (Position.WIDTH * Position.HEIGHT + 1 - pos.getMoves()) / 2; //check for win move
        }

        int max = -Position.WIDTH * Position.HEIGHT;
        for (int x = 0; x < Position.WIDTH; x++) {
            if (pos.isValidMove(x)) {
                Position p2 = new Position(pos);
                p2.play(x);
                int score = -negamax(p2, depth - 1);
                if (score > max) max = score;
            }
        }
        return max;
    }

    public int solve(Position pos, int depth) {
        nodes = 0;
        return negamax(pos, depth);
    }

    public long getNodes() {
        return nodes;
    }
}
