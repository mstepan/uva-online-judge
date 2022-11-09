package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 156 - Ananagrams
 */
public class Uva_156 {

    private Uva_156() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            Map<String, List<String>> anagrams = new HashMap<>();

            Set<String> singleChars = new HashSet<>();

            while (true) {

                String line = rd.readLine().trim();

                if ("#".equals(line)) {
                    break;
                }

                String[] words = line.split("\\s+");

                for (String singleWord : words) {

                    if( singleWord.length() == 1 ){
                        singleChars.add(singleWord);
                    }
                    else {
                        String key = computeKey(singleWord);
                        anagrams.compute(key, (key1, listValue) -> {

                            List<String> bucket = listValue;

                            if (bucket == null) {
                                bucket = new ArrayList<>();
                            }

                            bucket.add(singleWord);
                            return bucket;
                        });
                    }
                }
            }

            List<String> ananagrams = new ArrayList<>(singleChars);

            for (Map.Entry<String, List<String>> entry : anagrams.entrySet()) {
                if (entry.getValue().size() == 1) {
                    ananagrams.add(entry.getValue().get(0));
                }
            }

            Collections.sort(ananagrams);

            for (String outWord : ananagrams) {
                out.println(outWord);
            }

            diff();
        }
    }

    private static String computeKey(String word) {
        char[] arr = word.toLowerCase().toCharArray();
        Arrays.sort(arr);
        return new String(arr);
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
            new Uva_156();
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
