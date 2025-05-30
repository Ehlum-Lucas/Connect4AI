import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Test {
    private File test_file;
    private Scanner file_reader;

    public Test(String file) {
        try {
            test_file = new File(file);
        } catch (Exception e) {
            System.out.println("Error initializing test file: " + file);
            e.printStackTrace();
        }
    }

    private static Map<String, Object> compute_average_time(long time, int total) {
        if (total == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("value", 0.0);
            result.put("unit", "nanoseconds");
            return result;
        }
        long averageTime = time / total;
        Map<String, Object> result = new HashMap<>();
        if (averageTime >= 1_000_000_000) {
            result.put("value", averageTime / 1_000_000_000.0);
            result.put("unit", " seconds");
        } else if (averageTime >= 1_000_000) {
            result.put("value", averageTime / 1_000_000.0);
            result.put("unit", " milliseconds");
        } else if (averageTime >= 1_000) {
            result.put("value", averageTime / 1_000.0);
            result.put("unit", " microseconds");
        } else {
            result.put("value", (double)averageTime);
            result.put("unit", " nanoseconds");
        }
        return result;
    }

    public void run_test_suite_for_ai(ConnectFourAI ai, String ttTypeDescription) {
        if (test_file == null || !test_file.exists()) {
            System.out.println("Test file not found or not initialized. Skipping tests for " + ttTypeDescription);
            return;
        }

        try {
            if (file_reader != null) {
                file_reader.close();
            }
            file_reader = new Scanner(test_file);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reopening the test file for " + ttTypeDescription);
            e.printStackTrace();
            return;
        }

        System.out.println("\n--- Testing with Transposition Table: " + ttTypeDescription + " ---");
        int score = 0;
        long nodes = 0;
        int total = 0;
        long time = 0;

        while(file_reader.hasNextLine()) {
            String data = file_reader.nextLine();
            String[] results = data.split(" ");
            if (results.length < 2) {
                System.out.println("Skipping malformed line: " + data);
                continue;
            }
            Position pos = new Position();
            pos.play(results[0]);

            long start = System.nanoTime();
            int[] ai_results = ai.solve(pos, 7);
            long end = System.nanoTime();
            time += (end - start);

            if (ai_results[1] == Integer.parseInt(results[1])) {
                score++;
            }
            nodes += ai_results[2];
            total++;
        }

        System.out.println("Score: " + score + "/" + total);
        if (total > 0) {
            System.out.println("Average nodes visited: " + (nodes / total));
            Map<String, Object> avgTimeResult = compute_average_time(time, total);
            System.out.printf("Average time: %.3f%s%n", avgTimeResult.get("value"), avgTimeResult.get("unit"));
        } else {
            System.out.println("No test cases processed.");
        }
        file_reader.close();
        file_reader = null;
    }

    public void run_all_tests() {
        ConnectFourAI aiHashMap = new ConnectFourAI(new ConnectFourAI.MapBasedTT(new HashMap<>()));
        run_test_suite_for_ai(aiHashMap, "HashMap");

        ConnectFourAI aiLinkedHashMap = new ConnectFourAI(new ConnectFourAI.MapBasedTT(new LinkedHashMap<>()));
        run_test_suite_for_ai(aiLinkedHashMap, "LinkedHashMap");

        ConnectFourAI aiTreeMap = new ConnectFourAI(new ConnectFourAI.MapBasedTT(new TreeMap<>()));
        run_test_suite_for_ai(aiTreeMap, "TreeMap");

        ConnectFourAI aiArrayList = new ConnectFourAI(new ArrayListTranspositionTable());
        run_test_suite_for_ai(aiArrayList, "ArrayList (Custom)");

        ConnectFourAI aiLinkedList = new ConnectFourAI(new LinkedListTranspositionTable());
        run_test_suite_for_ai(aiLinkedList, "LinkedList (Custom)");
    }

    public static void main(String[] args) {
        String file = "./tests/Test_L3_R1.txt";
        Test test = new Test(file);
        test.run_all_tests();
    }
}