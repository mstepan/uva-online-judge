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
 * Mint, https://vjudge.net/problem/UVA-10717
 * <p>
 * N <- coins count (N <= 50)
 * M <- tables count (M <= 10)
 * <p>
 * gcd algorithm complexity: log2(Integer.MAX_VALUE) ~ 31
 * <p>
 * time: O(M * N^4 * 31)   10 * 50^4 * 31 = 1938 M
 * space: O(N)
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10717 {

    private Uva_10717() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String[] coinsAndTables = rd.readLine().trim().split("\\s+");

                int coinsCnt = Integer.parseInt(coinsAndTables[0]);
                int tablesCnt = Integer.parseInt(coinsAndTables[1]);

                if (coinsCnt == 0) {
                    break;
                }

                int[] coins = new int[coinsCnt];

                for (int i = 0; i < coins.length; ++i) {
                    coins[i] = Integer.parseInt(rd.readLine().trim());
                }

                for (int i = 0; i < tablesCnt; ++i) {
                    int tableHeight = Integer.parseInt(rd.readLine().trim());
                    Range optimalRange = findOptimalRange(coins, tableHeight);
                    out.printf("%d %d%n", optimalRange.lo, optimalRange.hi);
                }
            }

            diff();
        }
    }

    private Range findOptimalRange(int[] coins, int tableHeight) {

        int smaller = Integer.MIN_VALUE;
        int bigger = Integer.MAX_VALUE;

        // Iterate over all quadruple(4) values and find Least Common Multiple.
        for (int i = 0; i < coins.length - 3; ++i) {
            for (int j = i + 1; j < coins.length - 2; ++j) {
                for (int k = j + 1; k < coins.length - 1; ++k) {
                    for (int t = k + 1; t < coins.length; ++t) {

                        int curLcm = lcm(coins[i], coins[j], coins[k], coins[t]);

                        if (tableHeight % curLcm == 0) {
                            return new Range(tableHeight, tableHeight);
                        }

                        int multiplier = tableHeight / curLcm;

                        if (multiplier * curLcm > smaller) {
                            smaller = multiplier * curLcm;
                        }

                        if ((multiplier + 1) * curLcm < bigger) {
                            bigger = (multiplier + 1) * curLcm;
                        }
                    }
                }
            }
        }

        return new Range(smaller, bigger);
    }

    private static int lcm(int a, int b, int c, int d) {
        return lcm(d, lcm(c, lcm(a, b)));
    }

    private static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }

        return gcd(b, a % b);
    }

    private static final class Range {
        final int lo;
        final int hi;

        Range(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }
    }

    /*
    10717 Mint
    11512 GATTACA
    10065 Useless Tile Packers
     */

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
            new Uva_10717();
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
