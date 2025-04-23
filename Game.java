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
            int playerMove = -1;

            while (true) {
                System.out.println("Your move (1-7): ");
                if (scanner.hasNextInt()) {
                    playerMove = scanner.nextInt() - 1;
                    if (playerMove >= 0 && playerMove < 7) {
                        if (pos.isValidMove(playerMove)) {
                            break;
                        } else {
                            System.out.println("Invalid move. Column is full. Try again.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    scanner.next();
                }
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
