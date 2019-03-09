package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;


/**
 * Generating Fast
 * <p>
 * https://vjudge.net/problem/UVA-10098
 *
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10098 {

    private Uva_10098() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCount = Integer.parseInt(rd.readLine().trim());

            for (int i = 0; i < testsCount; ++i) {
                String str = rd.readLine().trim();
                printAllPermutations(str, out);
                out.println();
            }

            diff();
        }
    }

    private void printAllPermutations(String str, PrintStream out) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);

        while (arr != null) {
            out.println(join(arr));
            arr = next(arr);
        }

    }

    private char[] next(char[] arr) {

        for (int i = arr.length - 1; i > 0; --i) {
            char prev = arr[i - 1];
            char cur = arr[i];

            // found
            if ((int) cur > (int) prev) {

                int biggerIndex = findBiggerFromRight(arr, i - 1);

                swap(arr, biggerIndex, i - 1);
                reverse(arr, i);

                return arr;
            }

        }


        return null;
    }

    private void reverse(char[] arr, int fromIndex) {

        int from = fromIndex;
        int to = arr.length - 1;

        while (from < to) {
            swap(arr, from, to);
            ++from;
            --to;
        }

    }

    private void swap(char[] arr, int from, int to) {
        char temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private int findBiggerFromRight(char[] arr, int index) {

        char ch = arr[index];

        for (int i = arr.length - 1; i > index; --i) {
            if (arr[i] > ch) {
                return i;
            }
        }

        return -1;
    }

    private String join(char[] arr) {

        StringBuilder buf = new StringBuilder(arr.length);

        for (int i = 0; i < arr.length; ++i) {
            buf.append(arr[i]);
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
            new Uva_10098();
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
