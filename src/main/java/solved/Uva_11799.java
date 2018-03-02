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
 * 11799 - Horror Dash
 */
public class Uva_11799 {

    private Uva_11799() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (Scanner sc = new Scanner(in)) {

            int testCount = sc.nextInt();

            for (int i = 0; i < testCount; ++i) {

                int maxSpeed = Integer.MIN_VALUE;
                int speedsCnt = sc.nextInt();

                for (int k = 0; k < speedsCnt; ++k) {
                    maxSpeed = Math.max(maxSpeed, sc.nextInt());
                }

                out.printf("Case %d: %d%n", i + 1, maxSpeed);
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
            new Uva_11799();
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
