import java.util.Scanner;

public class Game {
    private ConnectFourAI ai = new ConnectFourAI();
    private Position pos = new Position();
    private Scanner scanner = new Scanner(System.in);

    public Game(String seq) {
        pos.play(seq);
    }

    public Game(){}

    public void game() {
        while (true) {
            pos.printBoard();
            System.out.println("Your move (1-7): ");
            int playerMove = scanner.nextInt() - 1;

            if (!pos.isValidMove(playerMove)) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            if (pos.isWinningMove(playerMove)) {
                pos.play(playerMove);
                pos.printBoard();
                System.out.println("You win!");
                break;
            }
            pos.play(playerMove);

            int[] aiResult = ai.solve(pos, 9);
            int aiMove = aiResult[1];

            if (pos.isWinningMove(aiMove)) {
                pos.play(aiMove);
                pos.printBoard();
                System.out.println("AI wins!");
                break;
            }
            pos.play(aiMove);
        }
    }
}
