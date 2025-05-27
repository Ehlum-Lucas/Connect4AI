import java.util.HashMap;
import java.util.Map;

public class ConnectFourAI {
    private long nodes;
    private ITranspositionTable transpositionTable;

    public static class TableEntry {
        int score;
        int bestMove;
        int depth;

        TableEntry(int score, int bestMove, int depth) {
            this.score = score;
            this.bestMove = bestMove;
            this.depth = depth;
        }
    }

    public interface ITranspositionTable {
        TableEntry get(long key);
        void put(long key, TableEntry entry);
        void clear();
    }

    public static class MapBasedTT implements ITranspositionTable {
        private final Map<Long, TableEntry> map;

        public MapBasedTT(Map<Long, TableEntry> map) {
            this.map = map;
        }

        @Override
        public TableEntry get(long key) {
            return map.get(key);
        }

        @Override
        public void put(long key, TableEntry entry) {
            map.put(key, entry);
        }

        @Override
        public void clear() {
            map.clear();
        }
    }

    public ConnectFourAI(ITranspositionTable transpositionTable) {
        this.transpositionTable = transpositionTable;
    }

    public ConnectFourAI() {
        this(new MapBasedTT(new HashMap<>()));
    }

    private int[] negamax(Position pos, int depth) {
        nodes++;

        long zobristHash = pos.getZobristHash();
        TableEntry entry = transpositionTable.get(zobristHash);
        if (entry != null && entry.depth >= depth) {
            return new int[]{entry.score, entry.bestMove};
        }

        if (pos.getMoves() == Position.WIDTH * Position.HEIGHT || depth == 0)
            return new int[] {0, -1};

        for (int x = 0; x < Position.WIDTH; x++) {
            if (pos.isValidMove(x) && pos.isWinningMove(x)) {
                int score = (Position.WIDTH * Position.HEIGHT + 1 - pos.getMoves()) / 2;
                transpositionTable.put(zobristHash, new TableEntry(score, x, depth));
                return new int[] {score, x};
            }
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
        transpositionTable.put(zobristHash, new TableEntry(max, bestMove, depth));
        return new int[] {max, bestMove};
    }

    public int[] solve(Position pos, int depth) {
        nodes = 0;
        transpositionTable.clear();
        int[] results = negamax(pos, depth);
        return new int[] {results[0], results[1], (int) nodes};
    }

    public long getNodes() {
        return nodes;
    }
}