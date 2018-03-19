package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 11572 - Unique Snowflakes
 */
public class Uva_11572 {


    private Uva_11572() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCnt = Integer.parseInt(rd.readLine().trim());

            for (int testCase = 0; testCase < testsCnt; ++testCase) {

                final int snowflakesCnt = Integer.parseInt(rd.readLine().trim());

                int maxPackSize = 0;

                int leftIndex = 0;

                Map<Integer, Integer> flakeWithIndex = new HashMap<>();

                for (int index = 0; index < snowflakesCnt; ++index) {

                    int flake = Integer.parseInt(rd.readLine().trim());

                    if (flakeWithIndex.containsKey(flake)) {
                        int newLeft = flakeWithIndex.put(flake, index) + 1;
                        leftIndex = Math.max(leftIndex, newLeft);
                    }
                    else {
                        flakeWithIndex.put(flake, index);
                    }

                    maxPackSize = Math.max(maxPackSize, index - leftIndex + 1);
                }

                out.println(maxPackSize);
            }

            diff();
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
            new Uva_11572();
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
