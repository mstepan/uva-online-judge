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
 * 146 - ID Codes
 */
public class Uva_146 {


    private Uva_146() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String line = rd.readLine().trim();

                if ("#".equals(line)) {
                    break;
                }

                String next = nextValue(line);

                if (next == null) {
                    out.println("No Successor");
                }
                else {
                    out.println(next);
                }
            }

            diff();
        }
    }


    private static String nextValue(String value) {

        char[] arr = value.toCharArray();

        int i = arr.length - 2;

        while (i >= 0) {

            if (arr[i] < arr[i + 1]) {

                int index = findFirstBiggestIndexFromEnd(arr[i], arr, i + 1);

                swap(arr, i, index);
                reverse(arr, i + 1, arr.length - 1);

                break;
            }

            --i;
        }


        return i < 0 ? null : new String(arr);
    }

    private static int findFirstBiggestIndexFromEnd(char ch, char[] arr, int boundary) {

        for (int i = arr.length - 1; i >= boundary; --i) {
            if (arr[i] > ch) {
                return i;
            }
        }

        return -1;
    }

    private static void swap(char[] arr, int from, int to) {
        char temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private static void reverse(char[] arr, int from, int to) {
        int left = from;
        int right = to;
        while (left < right) {
            swap(arr, left, right);
            ++left;
            --right;
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
            new Uva_146();
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
