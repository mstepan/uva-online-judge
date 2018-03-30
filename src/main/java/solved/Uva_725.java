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
 * 725 - Division
 */
public class Uva_725 {

    private Uva_725() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int cnt = 0;

            while (true) {
                int n = Integer.parseInt(rd.readLine().trim());

                if (n == 0) {
                    break;
                }

                printNumbers(n, out, cnt);

                ++cnt;
            }

            diff();
        }
    }

    private static final int ALL_DIGITS_MASK = (1 << 10) - 1;

    private static void printNumbers(int n, PrintStream out, int cnt) {

        if (cnt != 0) {
            out.println();
        }

        boolean hasSolution = false;

        for (int f = 1234; f <= 98765 / n; ++f) {
            int a = f * n;

            int mask = calculateMask(f) ^ calculateMask(a);

            if (mask == ALL_DIGITS_MASK) {
                hasSolution = true;
                out.printf("%05d / %05d = %d%n", a, f, n);
            }
        }

        if (!hasSolution) {
            out.printf("There are no solutions for %d.%n", n);
        }
    }

    private static int calculateMask(int baseValue) {

        int value = baseValue;

        // check if starts with '0'
        int mask = (value < 10_000 ? 1 : 0);

        while (value != 0) {
            mask |= (1 << value % 10);
            value /= 10;
        }
        return mask;
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
            new Uva_725();
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
