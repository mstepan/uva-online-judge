package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 11849 - CD
 */
public class Uva_11849 {


    private Uva_11849() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String[] data = rd.readLine().trim().split("\\s+");

                int n = Integer.parseInt(data[0]);
                int m = Integer.parseInt(data[1]);

                if (n == 0 && m == 0) {
                    break;
                }

                int[] first = new int[n];
                for (int i = 0; i < n; ++i) {
                    first[i] = Integer.parseInt(rd.readLine().trim());
                }

                int[] second = new int[m];
                for (int i = 0; i < m; ++i) {
                    second[i] = Integer.parseInt(rd.readLine().trim());
                }

                out.println(intersectionCount(first, second));
            }

            diff();
        }
    }

    private static int intersectionCount(int[] first, int[] second) {

        int i = 0;
        int j = 0;

        int cnt = 0;

        while (i < first.length && j < second.length) {

            if (first[i] == second[j]) {
                ++cnt;
                ++i;
                ++j;
            }
            else if (first[i] < second[j]) {
                ++i;
            }
            else {
                ++j;
            }

        }
        return cnt;
    }

    private static String combineResult(Deque<String> ops) {
        StringBuilder buf = new StringBuilder();

        Iterator<String> revIt = ops.descendingIterator();

        while (revIt.hasNext()) {
            buf.append(revIt.next()).append(" ");
        }

        return buf.toString().trim();
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
            new Uva_11849();
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
