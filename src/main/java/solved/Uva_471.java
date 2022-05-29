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
 * Magic Numbers
 * <p>
 * https://vjudge.net/problem/UVA-471
 */
public class Uva_471 {

    private Uva_471() throws IOException, InterruptedException {


        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            int testCases = Integer.parseInt(reader.readLine().trim());

            for (int i = 0; i < testCases; ++i) {

                reader.readLine();

                if (i > 0) {
                    out.println();
                }

                long value = Long.parseLong(reader.readLine().trim());
                printAllSpecialPairs(out, value);
            }

            diff();
        }
    }

    private void printAllSpecialPairs(PrintStream out, long value) {

        final long boundary = 10_000_000_000L / value;

        for (long s2 = 1; s2 <= boundary; ++s2) {
            if (hasUniqueDigits(s2)) {
                long s1 = s2 * value;
                if (hasUniqueDigits(s1)) {
                    out.printf("%d / %d = %d%n", s1, s2, value);
                }
            }
        }
    }

    private boolean hasUniqueDigits(long value) {

        long temp = value;
        int usedDigits = 0;

        while (temp != 0) {
            int digit = (int) (temp % 10);
            temp /= 10L;
            int mask = 1 << digit;

            if ((usedDigits & mask) != 0) {
                return false;
            }
            usedDigits |= mask;
        }

        return true;
    }


    //------------------------------------------------------------------------------------------------------------------
    // UTILS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read array from input stream, each element of an array is read as a separate new line.
     */
    private int[] readArray(BufferedReader reader, int arrLength) throws IOException {
        final int[] arr = new int[arrLength];

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
            new Uva_471();
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
