package not_submitted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * UVa 108 - Maximum Sum
 */
public class Uva_108 {


    private Uva_108() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int[][] m = {
                    {1, 2, -1, -4, -20},
                    {-8, -3, 4, 2, 1},
                    {3, 8, 10, 1, 3},
                    {-4, -1, 1, 7, -6}
            };

            out.println(maxRectangleSum(m));

            diff();
        }
    }

    /**
     * time: O(N^4)
     * space: O(N^2)
     */
    static int maxRectangleSum(int[][] m) {

        final int rows = m.length;
        final int cols = m[0].length;

        int[][] cumulative = calculateCumulativeMatrix(m);

        int maxSum = 0;

        for (int endRow = 0; endRow < rows; ++endRow) {
            for (int endCol = 0; endCol < cols; ++endCol) {

                for (int startRow = 0; startRow <= endRow; ++startRow) {
                    for (int startCol = 0; startCol <= endCol; ++startCol) {

                        int curSum = cumulative[endRow][endCol] -
                                (startRow == 0 ? 0 : cumulative[startRow - 1][endCol]) -
                                (startCol == 0 ? 0 : cumulative[endRow][startCol - 1]) +
                                (startRow == 0 || startCol == 0 ? 0 : cumulative[startRow - 1][startCol - 1]);

                        maxSum = Math.max(maxSum, curSum);

                    }
                }
            }
        }

        return maxSum;
    }

    /**
     * time: O(N^2)
     * space: O(N^2)
     */
    private static int[][] calculateCumulativeMatrix(int[][] m) {
        final int rows = m.length;
        final int cols = m[0].length;

        int[][] cumulative = new int[rows][cols];

        // set 0; 0 element
        cumulative[0][0] = m[0][0];

        // calculate 0-row
        for (int col = 1; col < cols; ++col) {
            cumulative[0][col] = cumulative[0][col - 1] + m[0][col];
        }

        // calculate 0-column
        for (int row = 1; row < rows; ++row) {
            cumulative[row][0] = cumulative[row - 1][0] + m[row][0];
        }

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {
                cumulative[row][col] = m[row][col] + cumulative[row - 1][col] + cumulative[row][col - 1] -
                        cumulative[row - 1][col - 1];
            }
        }

        return cumulative;
    }


    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(
                    Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt")));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {

        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
                .exec(String.format("/usr/bin/diff %s %s",
                        "/Users/mstepan/repo/uva-online-judge/src/main/java/out.txt",
                        "/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt"));

        Uva_108.StreamGobbler streamGobbler =
                new Uva_108.StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
    }

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            new Uva_108();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }


}
