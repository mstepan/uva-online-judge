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
 *
 * Recursive backtracking with pruning.
 *
 * https://vjudge.net/problem/UVA-10911
 *
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10911_backtracking {

    private Uva_10911_backtracking() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

//            final long startTime = System.currentTimeMillis();

            for (int caseIndex = 1; ; ++caseIndex) {

                int positionsCnt = Integer.parseInt(rd.readLine().trim());

                if (positionsCnt == 0) {
                    break;
                }

                Position[] positions = new Position[2 * positionsCnt];

                for (int i = 0; i < positions.length; ++i) {

                    String[] singleLineData = rd.readLine().trim().split("\\s+");

                    int x = Integer.parseInt(singleLineData[1]);
                    int y = Integer.parseInt(singleLineData[2]);

                    positions[i] = new Position(x, y);
                }

                double optimalSolution = findOptimalSolution(positions);

                out.printf("Case %d: %.2f%n", caseIndex, optimalSolution);
            }

//            final long endTime = System.currentTimeMillis();
//
//            System.out.printf("time: %d ms%n", (endTime - startTime));

            diff();
        }
    }

    private double findOptimalSolution(Position[] positions) {

        double[] bestSoFar = {Double.POSITIVE_INFINITY};

        findOptimalRec(positions, new boolean[positions.length], positions.length / 2, 0.0, bestSoFar);

        return bestSoFar[0];
    }

    private void findOptimalRec(Position[] positions, boolean[] used, int pairsLeft, double totalCost, double[] bestSoFar) {

        if (Double.compare(totalCost, bestSoFar[0]) > 0) {
            return;
        }

        if (pairsLeft == 0) {

            if (Double.compare(totalCost, bestSoFar[0]) < 0) {
                bestSoFar[0] = totalCost;
            }

            return;
        }

        int i = firstClear(used);
        used[i] = true;

        Position cur = positions[i];

        for (int j = 0; j < positions.length; ++j) {

            if (!used[j]) {

                Position other = positions[j];

                used[j] = true;

                findOptimalRec(positions, used, pairsLeft - 1, totalCost + cur.distance(other), bestSoFar);

                used[j] = false;
            }
        }

        used[i] = false;
    }

    private static int firstClear(boolean[] used) {
        for (int i = 0; i < used.length; ++i) {
            if (!used[i]) {
                return i;
            }
        }

        throw new IllegalStateException("Can't be here");
    }

    private static final class Position {
        final int x;
        final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        double distance(Position other) {
            double dx = ((double) x) - other.x;
            double dy = ((double) y) - other.y;
            return Math.sqrt((dx * dx) + (dy * dy));
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
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
            new Uva_10911_backtracking();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


        StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
