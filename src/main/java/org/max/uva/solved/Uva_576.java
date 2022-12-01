package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://vjudge.net/problem/UVA-576">UVA-576: Haiku Review</a>
 */
public class Uva_576 {

    private static final Pattern SYLLABLE_REGEXP = Pattern.compile("([^aeiouy]*[aeiouy]+[^aeiouy]*)");

    private static final int[] EXPECTED_SYLLABLES_COUNT = {5, 7, 5};

    private static void mainLogic() throws IOException, InterruptedException {
        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                if("e/o/i".equals(line)){
                    break;
                }

                String[] sentences = line.split("/");

                int index = 0;
                for (; index < sentences.length; ++index) {
                    int actualCount = countSyllablesInSentence(sentences[index]);
                    if (actualCount != EXPECTED_SYLLABLES_COUNT[index]) {
                        out.println(index + 1);
                        break;
                    }
                }

                if (index == sentences.length) {
                    out.println("Y");
                }
            }

            diff();
        }
    }

    private static int countSyllablesInSentence(String str) {

        String[] words = str.split(" ");

        int cnt = 0;

        for (String singleWord : words) {
            cnt += countSyllablesInWord(singleWord);
        }

        return cnt;
    }

    private static int countSyllablesInWord(String word) {
        Matcher matcher = SYLLABLE_REGEXP.matcher(word);

        int cnt = 0;

        while (matcher.find()) {
            ++cnt;
        }

        return cnt;
    }


    //------------------------------------------------------------------------------------------------------------------
    // UTILS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read array from input stream, each element of an array is read as a separate new line.
     */
    private static int[] readArray(BufferedReader reader, int length) throws IOException {
        final int[] arr = new int[length];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = Integer.parseInt(reader.readLine());
        }

        return arr;
    }

    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static final String DIFF_TOOL = "/usr/bin/diff";

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            mainLogic();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getPathFromResourceFolder(String filePath) {
        return Objects.requireNonNull(MethodHandles.lookup().lookupClass().getResource(filePath)).getPath();
    }

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get(getPathFromResourceFolder("in.txt")));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(Paths.get(getPathFromResourceFolder("out-actual.txt"))));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {
        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
                .exec(String.format("%s %s %s", DIFF_TOOL, getPathFromResourceFolder("out.txt"),
                                    getPathFromResourceFolder("out-actual.txt")));

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
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
