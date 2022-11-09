package codeforces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * G. Парные забеги
 * https://codeforces.com/problemset/problem/1244/G
 */
public class Codeforces_1244G {

    private Codeforces_1244G() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine().trim();

            String[] parts = line.split("\\s+");

            long n = Long.parseLong(parts[0]);
            long expectedSum = Long.parseLong(parts[1]);

            long minPossibleSum = n * (n + 1) / 2;

            if(expectedSum < minPossibleSum ){
                out.println("-1");
            }
            else {
                int[] arr = createSequentialArray((int) n);

                String arr1Str = toStringWithSpaces(arr);

                long bestLeftSum = findBestArray(arr, (expectedSum - minPossibleSum));

                out.println(expectedSum - bestLeftSum);
                out.println(arr1Str);
                out.println(toStringWithSpaces(arr));

                System.out.println();
            }

            diff();
        }
    }

    private long findBestArray(int[] arr, long baseLeftSum) {

        long leftSum = baseLeftSum;

        int left = 0;
        int right = arr.length - 1;

        while (left < right && leftSum != 0L) {

            int diff = arr[right] - arr[left];

            if (diff <= leftSum) {
                swap(arr, left, right);
                leftSum -= diff;
                ++left;
                --right;
            }
            else {
                --right;
            }

        }

        return leftSum;
    }

    private void swap(int[] arr, int from, int to) {
        int temp = arr[from];

        arr[from] = arr[to];
        arr[to] = temp;
    }

    private static String toStringWithSpaces(int[] arr) {
        StringBuilder buf = new StringBuilder(arr.length);

        for (int val : arr) {
            buf.append(val).append(" ");
        }

        return buf.toString().trim();
    }

    private static int[] createSequentialArray(int n) {
        int[] arr = new int[n];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = i + 1;
        }

        return arr;
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
            new Codeforces_1244G();
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
