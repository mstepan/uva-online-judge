package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * UVA-10010: Where's Waldorf?
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10010 {

    private Uva_10010() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCases = Integer.parseInt(rd.readLine().trim());

            for (int i = 0; i < testCases; ++i) {
                // read empty line and skip
                rd.readLine();

                String[] gridParams = rd.readLine().trim().split("\\s+");

                int rows = Integer.parseInt(gridParams[0]);
                int cols = Integer.parseInt(gridParams[1]);

                char[][] grid = new char[rows][cols];

                for (int singleRow = 0; singleRow < rows; ++singleRow) {
                    grid[singleRow] = rd.readLine().trim().toLowerCase().toCharArray();
                }

                int searchWords = Integer.parseInt(rd.readLine().trim());

                if (i != 0) {
                    out.println();
                }

                for (int it = 0; it < searchWords; ++it) {
                    char[] word = rd.readLine().trim().toLowerCase().toCharArray();

                    RowColPair pair = find(word, grid);

                    out.printf("%d %d%n", pair.row + 1, pair.col + 1);
                }

            }

            diff();
        }
    }

    private RowColPair find(char[] word, char[][] gridData) {

        final Grid grid = new Grid(gridData);

        for (int row = 0; row < grid.rows; ++row) {
            for (int col = 0; col < grid.cols; ++col) {

                for (int[] offset : DIRECTIONS) {
                    if (findRec(word, 0, grid, row, col, offset)) {
                        return new RowColPair(row, col);
                    }
                }
            }
        }

        throw new IllegalStateException("Not found");
    }

    private static final int[][] DIRECTIONS = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };

    private boolean findRec(char[] word, int wordIndex, Grid grid, int gridRow, int gridCol, int[] offset) {

        if (grid.outOfGridBounds(gridRow, gridCol) || noMatch(word, wordIndex, grid, gridRow, gridCol)) {
            return false;
        }

        if (isLastCharToMatch(word, wordIndex)) {
            return true;
        }

        final int nextRow = gridRow + offset[0];
        final int nextCol = gridCol + offset[1];

        return findRec(word, wordIndex + 1, grid, nextRow, nextCol, offset);
    }


    private static boolean noMatch(char[] word, int index, Grid grid, int row, int col) {
        return word[index] != grid.charAt(row, col);
    }

    private static boolean isLastCharToMatch(char[] word, int index) {
        return index + 1 == word.length;
    }


    private static final class Grid {
        final char[][] data;
        final int rows;
        final int cols;

        Grid(char[][] data) {
            this.data = data;
            this.rows = data.length;
            this.cols = data[0].length;
        }

        char charAt(int row, int col) {
            return data[row][col];
        }

        boolean outOfGridBounds(int row, int col) {
            return row < 0 || row >= rows || col < 0 || col >= cols;
        }
    }

    private static final class RowColPair {
        final int row;
        final int col;

        RowColPair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return row + ", " + col;
        }
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

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
    }

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            new Uva_10010();
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
