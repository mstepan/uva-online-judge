package org.max.uva.solved;

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
 * 11340 - Newspaper
 */
public class Uva_11340 {

    private Uva_11340() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCount = Integer.parseInt(rd.readLine());

            for (int i = 0; i < testsCount; ++i) {
                final int charsCount = Integer.parseInt(rd.readLine());

                Map<Character, Integer> charPrices = new HashMap<>();

                for (int k = 0; k < charsCount; ++k) {
                    String[] data = rd.readLine().split("\\s+");
                    charPrices.put(data[0].charAt(0), Integer.parseInt(data[1]));
                }

                long totalPriceInCents = 0L;
                final int textLines = Integer.parseInt(rd.readLine());

                for (int lineIndex = 0; lineIndex < textLines; ++lineIndex) {
                    String line = rd.readLine();

                    for (int chIndex = 0; chIndex < line.length(); ++chIndex) {
                        Integer price = charPrices.get(line.charAt(chIndex));

                        if (price != null) {
                            totalPriceInCents += price;
                        }
                    }
                }

                out.printf("%d.%02d$%n", totalPriceInCents / 100, totalPriceInCents % 100);
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
            new Uva_11340();
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
