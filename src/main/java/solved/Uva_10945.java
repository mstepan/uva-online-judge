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
 * 10945 - Mother bear
 */
public class Uva_10945 {

    private Uva_10945() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (Scanner sc = new Scanner(in)) {

            while (true) {
                String sentence = sc.nextLine().trim();

                if ("DONE".equals(sentence)) {
                    break;
                }

                sentence = sentence.toLowerCase();

                if (isPalindromicSentence(sentence)) {
                    out.println("You won't be eaten!");
                }
                else {
                    out.println("Uh oh..");
                }
            }

            diff();
        }
    }

    private static boolean isPalindromicSentence(String sentence) {

        char[] arr = sentence.toCharArray();
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            if ( arr[left] < 'a' || arr[left] > 'z') {
                ++left;
            }
            else if (arr[right] < 'a' || arr[right] > 'z') {
                --right;
            }
            else {

                if (arr[left] != arr[right]) {
                    return false;
                }

                ++left;
                --right;
            }
        }

        return true;
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
            new Uva_10945();
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
