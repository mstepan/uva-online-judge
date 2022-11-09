package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * UVA-10140: Prime Distance
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10140 {

    private Uva_10140() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

//        long startTime = System.currentTimeMillis();

        final BitSet basePrimes = sievePrimesOfSquareOptimized(((int) Math.sqrt(2_147_483_647)) + 1);

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine();

            while (line != null) {

                String[] data = line.trim().split("\\s+");

                int lo = Integer.parseInt(data[0]);
                int hi = Integer.parseInt(data[1]);

                Optional<MinAndMax> minMax = calculateMinAndMaxPairOfPrimes(lo, hi, basePrimes);

                if (minMax.isPresent()) {
                    out.printf("%d,%d are closest, %d,%d are most distant.%n",
                            minMax.get().min.first, minMax.get().min.second,
                            minMax.get().max.first, minMax.get().max.second);
                }
                else {
                    out.println("There are no adjacent primes.");
                }


                line = rd.readLine();
            }

//            long endTime = System.currentTimeMillis();

//            System.out.println("time: " + (endTime - startTime) + " ms");

            diff();
        }
    }

    private static Optional<MinAndMax> calculateMinAndMaxPairOfPrimes(int lo, int hi, BitSet basePrimes) {

        Sieve primesInRange = findPrimesInRange(lo, hi, basePrimes);

        if (primesInRange.elems < 2) {
            return Optional.empty();
        }

        PrimesPair minPair = null;
        int minDiff = Integer.MAX_VALUE;

        PrimesPair maxPair = null;
        int maxDiff = Integer.MIN_VALUE;

        final int index = primesInRange.nextSetBit(0);
        int prev = index + lo;

        for (int p = primesInRange.nextSetBit(index + 1); p > 0; p = primesInRange.nextSetBit(p + 1)) {

            int cur = p + lo;

            int curDiff = cur - prev;

            if (curDiff < minDiff) {
                minPair = new PrimesPair(prev, cur);
                minDiff = curDiff;
            }

            if (curDiff > maxDiff) {
                maxPair = new PrimesPair(prev, cur);
                maxDiff = curDiff;
            }

            prev = cur;
        }

        assert minPair != null : "null minPair detected";
        assert maxPair != null : "null maxPair detected";

        return Optional.of(new MinAndMax(minPair, maxPair));
    }

    private static Sieve findPrimesInRange(int lo, int hi, BitSet primes) {

        if (lo == Integer.MAX_VALUE) {
            return new Sieve(new boolean[]{true}, 1);
        }

        final int rangeLength = hi - lo + 1;
        boolean[] sieveForRange = new boolean[rangeLength];
        Arrays.fill(sieveForRange, true);

        int elemsCnt = sieveForRange.length;

        if (lo == 1) {
            sieveForRange[0] = false;
        }

        for (int p = primes.nextSetBit(2); p > 0 && p * p <= hi; p = primes.nextSetBit(p + 1)) {

            long from = (long) (Math.ceil((double) lo / p));

            if (from == 1L) {
                from = 2L;
            }

            for (long val = from * p; val <= hi && val >= 0; val += p) {

                int composite = (int) (val - lo);

                if (composite < 0) {
                    break;
                }

                sieveForRange[composite] = false;
            }
        }

        return new Sieve(sieveForRange);

    }

    private static BitSet sievePrimesOfSquareOptimized(int value) {

        final int squareOfUpper = ((int) Math.sqrt(value)) + 1;

        BitSet primes = new BitSet(value + 1);
        primes.set(2, value + 1);

        for (int p = primes.nextSetBit(2); p >= 0 && p <= squareOfUpper; p = primes.nextSetBit(p + 1)) {
            for (int composite = p * p; composite <= value; composite += p) {
                primes.clear(composite);
            }
        }

        return primes;
    }

    private static final class Sieve {
        final boolean[] arr;
        final int elems;

        Sieve(boolean[] arr, int elems) {
            this.arr = arr;
            this.elems = elems;
        }

        Sieve(boolean[] arr) {
            this(arr, cardinality(arr));
        }

        private static int cardinality(boolean[] arr) {

            int count = 0;
            for (int i = 0; i < arr.length; ++i) {
                if (arr[i]) {
                    ++count;
                }
            }
            return count;
        }

        int nextSetBit(int from) {
            for (int i = from; i < arr.length; ++i) {
                if (arr[i]) {
                    return i;
                }
            }

            return -1;
        }
    }

    private static final class PrimesPair {
        final int first;
        final int second;

        PrimesPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    private static final class MinAndMax {
        final PrimesPair min;
        final PrimesPair max;

        MinAndMax(PrimesPair min, PrimesPair max) {
            this.min = min;
            this.max = max;
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
            new Uva_10140();
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
