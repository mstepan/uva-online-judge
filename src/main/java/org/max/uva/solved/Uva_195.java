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
 * 195 - Anagram
 */
public class Uva_195 {

    private Uva_195() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int wordsCount = Integer.parseInt(rd.readLine());

            for (int i = 0; i < wordsCount; ++i) {
                String word = rd.readLine().trim();
                printAllPermutations(out, word);
            }

            diff();
        }
    }

    private static void printAllPermutations(PrintStream out, String word) {

        Character[] arr = new Character[word.length()];

        for (int i = 0; i < word.length(); ++i) {
            arr[i] = word.charAt(i);
        }

        Arrays.sort(arr, (ch1, ch2) -> {

            Character lowerCh1 = Character.toLowerCase(ch1);
            Character lowerCh2 = Character.toLowerCase(ch2);

            int cmp = lowerCh1.compareTo(lowerCh2);

            if (cmp != 0) {
                return cmp;
            }

            return Character.compare(ch1, ch2);
        });

        printRec(out, arr, new boolean[arr.length], new StringBuilder());
    }

    private static void printRec(PrintStream out, Character[] arr, boolean[] used, StringBuilder res) {

        if (res.length() == arr.length) {
            out.println(res);
            return;
        }

        char prev = ' ';
        for (int i = 0; i < arr.length; ++i) {

            if (!used[i] && prev != arr[i]) {

                prev = arr[i];

                res.append(arr[i]);
                used[i] = true;

                printRec(out, arr, used, res);

                used[i] = false;
                res.deleteCharAt(res.length() - 1);
            }
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
            new Uva_195();
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
