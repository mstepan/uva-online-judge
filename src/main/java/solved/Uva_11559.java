package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 11559 - Event Planning
 */
public class Uva_11559 {

    private Uva_11559() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (Scanner sc = new Scanner(in)) {

            while (sc.hasNext()) {

                int participants = sc.nextInt();
                int budget = sc.nextInt();
                int hotelsCount = sc.nextInt();
                int weeksCount = sc.nextInt();

                int minSoFar = Integer.MAX_VALUE;

                for (int i = 0; i < hotelsCount; ++i) {

                    int price = sc.nextInt();

                    for (int k = 0; k < weeksCount; ++k) {
                        int bedsCount = sc.nextInt();

                        if (bedsCount >= participants && participants * price <= budget) {
                            minSoFar = Math.min(minSoFar, participants * price);
                        }
                    }
                }

                out.println(minSoFar == Integer.MAX_VALUE ? "stay home" : minSoFar);
            }

        }

        diff();
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

        int returnCode = process.waitFor();

        assert returnCode == 0;
    }

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            new Uva_11559();
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
