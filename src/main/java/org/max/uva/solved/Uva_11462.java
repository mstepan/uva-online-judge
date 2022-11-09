package org.max.uva.solved;

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
 * 11462 - Age Sort
 */
public class Uva_11462 {

    private Uva_11462() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in), 25 * 1024 * 1024)) {

            int[] freq = new int[100];

            while (true) {
                final int count = Integer.parseInt(rd.readLine());

                if (count == 0) {
                    break;
                }

                String[] ages = rd.readLine().split(" ");


                for (String singleAgeStr : ages) {
                    ++freq[toIntAgeOptimized(singleAgeStr)];
                }

                StringBuilder res = new StringBuilder(2 * ages.length);

                for (int i = 0; i < freq.length; ++i) {
                    for (int cnt = freq[i]; cnt != 0; --cnt) {

                        if (res.length() == 0) {
                            res.append(i);
                        }
                        else {
                            res.append(" ").append(i);
                        }
                    }
                }

                out.println(res);

                Arrays.fill(freq, 0);
            }

            diff();
        }
    }

    private static int toIntAgeOptimized(String value) {
        if (value.length() < 2) {
            return value.charAt(0) - '0';
        }

        return (value.charAt(0) - '0') * 10 + (value.charAt(1) - '0');
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
            new Uva_11462();
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
