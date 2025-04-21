public class Main {
    public static void main(String[] args) {
        ConnectFourAI ai = new ConnectFourAI();
        Position pos = new Position();
        String seq = "52753311433677442422121";
        if (pos.play(seq) != seq.length()) {
            System.out.println("Invalid move");
            return;
        } else {
            pos.printBoard();
            System.out.println();
            int score = ai.solve(pos, 9);
            System.out.println("Score: " + score);
        }
    }
}
