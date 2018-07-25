package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * UVA - 10496: Collecting Beepers.
 * https://vjudge.net/problem/UVA-10496
 */
public class Uva_10496 {

    private Uva_10496() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCases = Integer.parseInt(rd.readLine().trim());

            for (int i = 0; i < testCases; ++i) {

                // read coordinates maxX, maxY here
                rd.readLine();

                String[] startCoordinates = rd.readLine().trim().split("\\s+");

                final Beeper start = new Beeper(0, Integer.parseInt(startCoordinates[0]),
                        Integer.parseInt(startCoordinates[1]));

                final Beeper[] beepers = new Beeper[Integer.parseInt(rd.readLine().trim())];

                for (int j = 0; j < beepers.length; ++j) {
                    String[] coordinates = rd.readLine().trim().split("\\s+");
                    beepers[j] = new Beeper(j+1, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                }

                out.printf("The shortest path has length %d%n", findShortestPath(start, beepers));
            }

            diff();
        }
    }

    private int findShortestPath(Beeper start, Beeper[] beepers) {

        final int[][] cache = new int[beepers.length + 1][1 << (beepers.length + 1)];

        return shortestRec(start, beepers, markUsed(0, start), start, cache);
    }

    /**
     * It's fine to use int for 'used', because we only have up to 10 beepers.
     */
    private int shortestRec(Beeper end, Beeper[] beepers, int used, Beeper startBeeper, int[][] cache) {

        if (allUsed(used, beepers.length + 1)) {
            return distance(end, startBeeper);
        }

        if (cache[end.id][used] != 0) {
            return cache[end.id][used];
        }

        int minDistance = Integer.MAX_VALUE;

        for (Beeper candidate : beepers) {
            if (notUsed(used, candidate)) {
                minDistance = Math.min(minDistance, distance(end, candidate) +
                        shortestRec(candidate, beepers, markUsed(used, candidate), startBeeper, cache));
            }
        }

        cache[end.id][used] = minDistance;

        return minDistance;
    }

    private static boolean allUsed(int used, int beepersCount) {
        return used == ((1 << beepersCount) - 1);
    }

    private static boolean notUsed(int used, Beeper beeper) {
        return (used & (1 << beeper.id)) == 0;
    }

    private static int markUsed(int used, Beeper beeper) {
        return used | (1 << (beeper.id));
    }

    /**
     * Calculate manhattan distance.
     */
    private static int distance(Beeper from, Beeper to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }


    private static final class Beeper {
        final int id;
        final int x;
        final int y;

        Beeper(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return id + ": (" + x + ", " + y + ")";
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
            new Uva_10496();
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
