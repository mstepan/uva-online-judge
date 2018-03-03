package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * 11221 - Magic square palindromes.
 */
public class Uva_11221 {

    private Uva_11221() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int sentencesCnt = Integer.parseInt(rd.readLine());

            for (int i = 0; i < sentencesCnt; ++i) {

                String str = onlyCharacters(rd.readLine().trim().toLowerCase());

                out.printf("Case #%d:%n", i + 1);

                final int size = squarePalindromeLength(str);

                out.println(size == -1 ? "No magic :(" : size);
            }

            diff();
        }
    }

    private static int squarePalindromeLength(String str) {
        if (!isPerfectSquare(str.length())) {
            return -1;
        }

        if (!isPalindrome(str)) {
            return -1;
        }

        int size = (int) Math.sqrt(str.length());

        StringBuilder bufByColumn = new StringBuilder(str.length());

        for (int column = 0; column < size; ++column) {
            for (int row = column; row < str.length(); row += size) {
                bufByColumn.append(str.charAt(row));
            }
        }

        String columnStr = bufByColumn.toString();

        if (!isPalindrome(columnStr)) {
            return -1;
        }

        return size;
    }

    private static boolean isPalindrome(String str) {

        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            ++left;
            --right;
        }

        return true;
    }

    private static String onlyCharacters(String base) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < base.length(); ++i) {
            char ch = base.charAt(i);

            if (ch >= 'a' && ch <= 'z') {
                res.append(ch);
            }
        }

        return res.toString();
    }

    private static boolean isPerfectSquare(int value) {
        int sqrt = (int) Math.sqrt(value);
        return sqrt * sqrt == value;
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
            new Uva_11221();
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
