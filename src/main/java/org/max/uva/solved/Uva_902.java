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
import java.util.regex.Pattern;


/**
 * <a href="https://vjudge.net/problem/UVA-902">UVA-902</a>
 */
public class Uva_902 {

    private static final Pattern DELIMITER = Pattern.compile("\\s+");

    private Uva_902() throws IOException, InterruptedException {

        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] lineParts = DELIMITER.split(line, line.length());

                int n;
                String text;

                if( lineParts.length < 2 ){
                    n = Integer.parseInt(lineParts[0]);
                    line = reader.readLine();

                    while ("".equals(line.trim())) {
                        line = reader.readLine();
                    }
                    text = line;
                }
                else {
                    n = Integer.parseInt(lineParts[0]);
                    text = lineParts[1];
                }

                String password = findMostCommonSubstring(text, n);
                out.println(password);

            }

            diff();
        }
    }

    private static String findMostCommonSubstring(String text, int subLength) {

        int maxCount = 0;
        String mostFreqSub = null;

        Map<String, Integer> subFreq = new HashMap<>();

        for (int i = 0; i <= (text.length() - subLength); ++i) {
            String curSubstring = text.substring(i, i + subLength);
            int curSubCount = subFreq.compute(curSubstring, (subKeyNotUsed, subCount) -> subCount == null ? 1 : subCount + 1);

            if (curSubCount > maxCount) {
                maxCount = curSubCount;
                mostFreqSub = curSubstring;
            }
        }

        return mostFreqSub;
    }


    //------------------------------------------------------------------------------------------------------------------
    // UTILS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read array from input stream, each element of an array is read as a separate new line.
     */
    private int[] readArray(BufferedReader reader, int arrLength) throws IOException {
        final int[] arr = new int[arrLength];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = Integer.parseInt(reader.readLine());
        }

        return arr;
    }

    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get(Uva_902.class.getResource("in.txt").getPath()));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(
                Paths.get(Uva_902.class.getResource("out-actual.txt").getPath())));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {

        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
            .exec(java.lang.String.format("/usr/bin/diff %s %s",
                                          Uva_902.class.getResource("out.txt").getPath(),
                                          Uva_902.class.getResource("out-actual.txt").getPath()));

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
            new Uva_902();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private final InputStream inputStream;

        private final Consumer<String> consumer;


        StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
