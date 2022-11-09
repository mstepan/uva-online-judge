package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <a href="https://vjudge.net/problem/UVA-10252">UVA-10252</a>
 */
public class Uva_10252 {

    private static void mainLogic() throws IOException, InterruptedException {

        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String second = reader.readLine();
                out.println(findCommonChars(line, second));
            }

            diff();
        }
    }

    private static String findCommonChars(String first, String second) {

        int[] freq1 = gatherCharsFreq(first);
        int[] freq2 = gatherCharsFreq(second);

        return combineCommonChars(freq1, freq2);
    }


    private static int[] gatherCharsFreq(String str) {
        int[] freq = new int['z' - 'a' + 1];
        for (int i = 0; i < str.length(); ++i) {
            freq[str.charAt(i) - 'a'] += 1;
        }
        return freq;
    }

    private static String combineCommonChars(int[] freq1, int[] freq2) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < freq1.length && i < freq2.length; ++i) {
            int curCount = Math.min(freq1[i], freq2[i]);

            if (curCount > 0) {
                char curCh = (char) ('a' + i);
                while (curCount > 0) {
                    res.append(curCh);
                    --curCount;
                }
            }
        }

        return res.toString();
    }


    //------------------------------------------------------------------------------------------------------------------
    // UTILS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read array from input stream, each element of an array is read as a separate new line.
     */
    private static int[] readArray(BufferedReader reader, int length) throws IOException {
        final int[] arr = new int[length];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = Integer.parseInt(reader.readLine());
        }

        return arr;
    }

    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get(Objects.requireNonNull(Uva_10252.class.getResource("in.txt")).getPath()));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(
                Paths.get(Objects.requireNonNull(Uva_10252.class.getResource("out-actual.txt")).getPath())));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {
        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
            .exec(String.format("/usr/bin/diff %s %s",
                                Objects.requireNonNull(Uva_10252.class.getResource("out.txt")).getPath(),
                                Objects.requireNonNull(Uva_10252.class.getResource("out-actual.txt")).getPath()));

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
            mainLogic();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private final InputStream inputStream;

        private final Consumer<String> consumer;


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
