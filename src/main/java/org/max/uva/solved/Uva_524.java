package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.function.Consumer;

/**
 * 524 - Prime Ring Problem
 */
public class Uva_524 {

    private Uva_524() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int testIndex = 0;

            while (true) {
                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                int k = Integer.parseInt(line.trim());

                if (testIndex != 0) {
                    out.println();
                }

                out.printf("Case %d:%n", testIndex + 1);

                printRings(out, k);

                ++testIndex;
            }

            diff();
        }
    }

    // primes below of equal to '16' + '15' = 31
    private static final BitSet PRIMES = new BitSet(17);

    static {
        PRIMES.set(2);
        PRIMES.set(3);
        PRIMES.set(5);
        PRIMES.set(7);
        PRIMES.set(11);
        PRIMES.set(13);
        PRIMES.set(17);
        PRIMES.set(19);
        PRIMES.set(23);
        PRIMES.set(29);
        PRIMES.set(31);
    }

    private static void printRings(PrintStream out, int k) {

        int[] numbers = getPossibleNumbers(k);

        int[] res = new int[k];
        res[0] = 1;

        boolean[] used = new boolean[numbers.length];

        printSolutionsRec(out, numbers, used, res, 1);
    }

    private static int[] getPossibleNumbers(int k) {
        int[] numbers = new int[k - 1];

        for (int i = 0; i < numbers.length; ++i) {
            numbers[i] = 2 + i;
        }

        return numbers;
    }

    private static void printSolutionsRec(PrintStream out, int[] numbers, boolean[] used, int[] res, int index) {

        if (index == res.length && PRIMES.get(res[0] + res[res.length - 1])) {
            out.println(clockWiseOrder(res));
            return;
        }

        final int lastRingValue = res[index - 1];

        for (int i = 0; i < numbers.length; ++i) {

            int curNumber = numbers[i];

            if (!used[i] && PRIMES.get(lastRingValue + curNumber)) {

                res[index] = curNumber;
                used[i] = true;

                printSolutionsRec(out, numbers, used, res, index + 1);

                used[i] = false;
                res[index] = 0;
            }
        }
    }

    private static String clockWiseOrder(int[] res) {

        assert res != null && res.length > 0;

        StringBuilder buf = new StringBuilder();
        buf.append(res[0]);

        for (int i = 1; i < res.length; ++i) {
            buf.append(" ").append(res[i]);
        }

        return buf.toString();
    }

    private static String counterClockWiseOrder(int[] res) {
        assert res != null && res.length > 0;

        StringBuilder buf = new StringBuilder();
        buf.append(res[0]);

        for (int i = res.length - 1; i > 0; --i) {
            buf.append(" ").append(res[i]);
        }

        return buf.toString();
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
            new Uva_524();
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
