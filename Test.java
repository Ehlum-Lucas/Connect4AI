import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
    private File test_file;
    private Scanner file_reader;
    private ConnectFourAI ai = new ConnectFourAI();

    public Test(String file) {
        try {
            test_file = new File(file);
            file_reader = new Scanner(test_file);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void test_ai() {
        int score = 0;
        int nodes = 0;
        int total = 0;
        long time = 0;
        while(file_reader.hasNextLine()) {
            String data = file_reader.nextLine();
            String[] results = data.split(" ");
            Position pos = new Position();
            pos.play(results[0]);
            long start = System.nanoTime();
            int[] ai_results = ai.solve(pos, 13 );
            long end = System.nanoTime();
            time += end - start;
            if (ai_results[1] == Integer.parseInt(results[1])) {
                score++;
            }
            nodes += ai_results[2];
            total++;
        }
        System.out.println("Score: " + score + "/" + total);
        System.out.println("Average node visited: " + nodes / total);
        long averageTime = time / total;
        if (averageTime >= 1_000_000_000) {
            System.out.println("Average time: " + (averageTime / 1_000_000_000.0) + " seconds");
        } else if (averageTime >= 1_000_000) {
            System.out.println("Average time: " + (averageTime / 1_000_000.0) + " milliseconds");
        } else if (averageTime >= 1_000) {
            System.out.println("Average time: " + (averageTime / 1_000.0) + " microseconds");
        } else {
            System.out.println("Average time: " + averageTime + " nanoseconds");
        }
    }

    public static void main(String[] args) {
        String file = "./tests/Test_L3_R1.txt";
        Test test = new Test(file);
        test.test_ai();
    }
}
