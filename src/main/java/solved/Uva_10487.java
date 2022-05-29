package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Closest Sums
 * <p>
 * https://vjudge.net/problem/UVA-10487
 */
public class Uva_10487 {

    private Uva_10487() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            for (int testCase = 1; ; ++testCase) {
                int arrLength = Integer.parseInt(reader.readLine().trim());

                if (arrLength == 0) {
                    break;
                }

                int[] arr = readArray(reader, arrLength);

                int queryLength = Integer.parseInt(reader.readLine().trim());

                int[] queries = readArray(reader, queryLength);

                answerAllQueries(out, testCase, arr, queries);
            }


            diff();
        }
    }

    private int[] readArray(BufferedReader reader, int arrLength) throws IOException {
        int[] arr = new int[arrLength];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = Integer.parseInt(reader.readLine());
        }

        return arr;
    }


    /**
     * https://vjudge.net/problem/UVA-10487
     * <p>
     * N - arr.length
     * M - queries.length
     * <p>
     * time: O(N*lgN + M*N)
     * space: O(1)
     */
    static void answerAllQueries(PrintStream out, int caseNo, int[] arr, int[] queries) {
        Objects.requireNonNull(arr);
        Objects.requireNonNull(queries);

        out.printf("Case %d:%n", caseNo);


        Arrays.sort(arr);
        for (int singleQuery : queries) {
            out.printf("Closest sum to %d is %d.%n", singleQuery, findClosestSum(arr, singleQuery));
        }
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    private static int findClosestSum(int[] arr, int value) {

        int left = 0;
        int right = arr.length - 1;

        int bestSum = 0;
        int bestDiff = Integer.MAX_VALUE;

        while (left < right) {

            long curSumLong = ((long) arr[left]) + arr[right];

            // check for overflow
            if (curSumLong > Integer.MAX_VALUE) {
                --right;
                continue;
            }

            //check for underflow
            if (curSumLong < Integer.MIN_VALUE) {
                ++left;
                continue;
            }

            int curSum = (int) curSumLong;

            if (curSum == value) {
                return curSum;
            }

            int curDiff = Math.abs(value - curSum);
            if (curDiff < bestDiff) {
                bestDiff = curDiff;
                bestSum = curSum;
            }

            if (curSum < value) {
                ++left;
            }
            else {
                --right;
            }
        }

        return bestSum;
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
            new Uva_10487();
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
