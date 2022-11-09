package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.Math.E;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

/**
 * https://vjudge.net/problem/UVA-10341
 *
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10341 {

    private Uva_10341() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                String[] data = line.trim().split("\\s+");

                int p = Integer.parseInt(data[0]);
                int q = Integer.parseInt(data[1]);
                int r = Integer.parseInt(data[2]);
                int s = Integer.parseInt(data[3]);
                int t = Integer.parseInt(data[4]);
                int u = Integer.parseInt(data[5]);

                Optional<Double> sol = findSolution(p, q, r, s, t, u);

                if (sol.isPresent()) {
                    out.printf("%.4f%n", sol.get());
                }
                else {
                    out.println("No solution");
                }
            }

            diff();
        }
    }

    private static final double EPSILON = 1E-7;
    private static final int MAX_ITERATIONS_COUNT = 100_000;

    private Optional<Double> findSolution(int p, int q, int r, int s, int t, int u) {

        Parameters params = new Parameters(p, q, r, s, t, u);

        double lo = 0.0;
        double hi = 1.0;

        double loFunc = solveForX(params, 0.0);
        double hiFunc = solveForX(params, 1.0);

        if (isSameSign(loFunc, hiFunc)) {
            return Optional.empty();
        }

        FuncType type = FuncType.GROW;

        if (Double.compare(loFunc, hiFunc) > 0) {
            type = FuncType.DOWN;
        }

        for (int i = 0; i < MAX_ITERATIONS_COUNT; ++i) {
            double mid = lo + ((hi - lo) / 2.0);

            double curFunc = solveForX(params, mid);

            if (Double.compare( Math.abs(curFunc), EPSILON) < 0) {
                return Optional.of(mid);
            }

            if (type == FuncType.GROW) {
                if (Double.compare(curFunc, 0.0) < 0) {
                    lo = mid;
                }
                else {
                    hi = mid;
                }
            }
            else {
                if (Double.compare(curFunc, 0.0) < 0) {
                    hi = mid;
                }
                else {
                    lo = mid;
                }
            }
        }

        return Optional.empty();
    }

    private boolean isSameSign(double first, double second) {
        return Double.compare(Math.signum(first), Math.signum(second)) == 0;
    }

    enum FuncType {
        GROW, DOWN
    }

    private static double solveForX(Parameters params, double x) {

        int p = params.p;
        int q = params.q;
        int r = params.r;
        int s = params.s;
        int t = params.t;
        int u = params.u;

        return p * pow(E, -x) + q * sin(x) + r * cos(x) + s * tan(x) + t * x * x + u;
    }

    private static final class Parameters {
        final int p;
        final int q;
        final int r;
        final int s;
        final int t;
        final int u;

        Parameters(int p, int q, int r, int s, int t, int u) {
            this.p = p;
            this.q = q;
            this.r = r;
            this.s = s;
            this.t = t;
            this.u = u;
        }
    }

    /*

    10360 Rat Attack - complete search
    10341 Solve It - bisection method (divide-and-conquer)
    11292 Dragon of Loowater
    11450 Wedding Shopping
    10911 Forming Quiz Teams
    11635 Hotel Booking
    11506 Angry Programmer
    10243 Fire! Fire!! Fire!!!
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
            new Uva_10341();
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
