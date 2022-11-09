package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * Recursive top-down dynamic solution with memoization.
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
public class Uva_10911 {

    private Uva_10911() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

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

            diff();
        }
    }

    private static double findOptimalSolution(Position[] positions) {

        final int initialMask = (1 << positions.length) - 1;

        return findRec(positions, initialMask, new HashMap<>());
    }

    private static double findRec(Position[] positions, int initialMask, Map<Integer, Double> cache) {

        if (initialMask == 0) {
            return 0.0;
        }

        if (cache.containsKey(initialMask)) {
            return cache.get(initialMask);
        }

        double bestSoFar = Double.POSITIVE_INFINITY;

        int mask = initialMask;


        int i = lsbOne(mask);
        int index1 = log2(i);
        mask = zeroLsb(mask);

        int cur = mask;

        while (cur != 0) {
            int j = lsbOne(cur);
            cur = zeroLsb(cur);

            int index2 = log2(j);

            bestSoFar = Math.min(bestSoFar,
                                 positions[index1].distance(positions[index2]) +
                                         findRec(positions, mask ^ j, cache));
        }

        cache.put(initialMask, bestSoFar);

        return bestSoFar;
    }

    // get LSB set to '1'
    private static int lsbOne(int value) {
        return value & ((~value) + 1);
    }

    // zero LSB set to '1'
    private static int zeroLsb(int value) {
        return value & (value - 1);
    }


    private static int log2(int value) {
        return (int) (Math.log(value) / Math.log(2.0));
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
            new Uva_10911();
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
