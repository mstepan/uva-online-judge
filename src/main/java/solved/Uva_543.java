package solved;

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
 * UVA-543: Goldbach's Conjecture
 */
public class Uva_543 {

    private Uva_543() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        int[] allPrimes = calculateAllPrimes(1_000_000);

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine().trim();

            while (true) {
                int value = Integer.parseInt(line);

                if (value == 0) {
                    break;
                }

                int[] primeValues = findGoldbachValues(allPrimes, value);

                if (primeValues.length == 0) {
                    out.println("Goldbach's conjecture is wrong.");
                }
                else {
                    out.println(value + " = " + primeValues[0] + " + " + primeValues[1]);
                }

                line = rd.readLine().trim();
            }

            diff();
        }
    }

    private int[] findGoldbachValues(int[] primes, int value) {

        int from = 0;
        int to = value >= primes[primes.length - 1] ? primes.length - 1 : findFirstBiggerValue(primes, value) - 1;

        while (from <= to) {
            int curSum = primes[from] + primes[to];

            if (curSum == value) {
                return new int[]{primes[from], primes[to]};
            }

            if (curSum < value) {
                ++from;
            }
            else {
                --to;
            }
        }

        return new int[0];
    }

    private int findFirstBiggerValue(int[] arr, int value) {

        int from = 0;
        int to = arr.length - 1;

        while (from <= to) {
            int mid = from + (to - from) / 2;

            if (arr[mid] <= value) {
                from = mid + 1;
            }
            else {
                to = mid - 1;
            }

        }

        return from;
    }

    private static int[] calculateAllPrimes(int maxValue) {

        BitSet sieve = new BitSet(maxValue + 1);
        sieve.set(2, maxValue, true);

        for (int p = sieve.nextSetBit(0); p != -1 && p * p <= maxValue; p = sieve.nextSetBit(p + 1)) {

            for (int val = p * p; val <= maxValue; val += p) {
                sieve.clear(val);
            }
        }

        int[] primes = new int[sieve.cardinality()];

        for (int i = 0, index = sieve.nextSetBit(2); i < primes.length; ++i, index = sieve.nextSetBit(index + 1)) {
            primes[i] = index;
        }

        return primes;
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
            new Uva_543();
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
