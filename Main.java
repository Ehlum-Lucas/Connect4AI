import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectFourAI ai = new ConnectFourAI();
        Position pos = new Position();
        Scanner scanner = new Scanner(System.in);
        String seq = "7422341735647741166133573473242566";
        pos.play(seq);

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

        scanner.close();
    }
}
