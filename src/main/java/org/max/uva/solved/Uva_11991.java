package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 11991 - Easy Problem from Rujia Liu?
 */
public class Uva_11991 {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private Uva_11991() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                String[] lengthAndQueries = line.trim().split("\\s+");

                final int length = Integer.parseInt(lengthAndQueries[0]);
                final int queriesCnt = Integer.parseInt(lengthAndQueries[1]);
                String[] numbers = rd.readLine().trim().split("\\s+");

                Map<Integer, List<Integer>> elemsLocations = new HashMap<>();

                for (int i = 0; i < length; ++i) {
                    int value = Integer.parseInt(numbers[i]);
                    elemsLocations.computeIfAbsent(value, unused -> new ArrayList<>()).add(i);
                }

                StringBuilder outRes = new StringBuilder(3 * queriesCnt);

                for (int q = 0; q < queriesCnt; ++q) {
                    String[] data = rd.readLine().trim().split("\\s+");

                    int order = Integer.parseInt(data[0]);
                    int value = Integer.parseInt(data[1]);

                    List<Integer> positionsList = elemsLocations.get(value);

                    if (positionsList == null || positionsList.size() < order) {
                        outRes.append("0").append(LINE_SEP);
                    }
                    else {
                        outRes.append(String.valueOf(positionsList.get(order - 1) + 1)).append(LINE_SEP);
                    }
                }

                out.print(outRes);
                out.flush();
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
            new Uva_11991();
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
