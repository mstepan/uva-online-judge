package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * GATTACA, https://vjudge.net/problem/UVA-11512
 *
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_11512 {

    private Uva_11512() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCasesCnt = Integer.parseInt(rd.readLine().trim());

            for (int i = 0; i < testCasesCnt; ++i) {
                String str = rd.readLine().trim();

                Optional<Pair> res = findLRS(str);

                if (res.isPresent()) {
                    out.printf("%s %d%n", res.get().str, res.get().count);
                }
                else {
                    out.println("No repetitions found!");
                }
            }

            diff();
        }
    }

    private static Optional<Pair> findLRS(String str) {
        Suffix[] suffixArr = createSuffixArray(str);

        String longestSoFar = "";
        int cnt = 1;

        for (int i = 1; i < suffixArr.length; ++i) {

            Suffix prev = suffixArr[i - 1];
            Suffix cur = suffixArr[i];

            String commonStr = Suffix.commonPrefix(prev, cur);

            if (commonStr.length() > longestSoFar.length()) {
                longestSoFar = commonStr;
                cnt = 2;
            }
            else if (longestSoFar.equals(commonStr)) {
                ++cnt;
            }
        }

        if ("".equals(longestSoFar)) {
            return Optional.empty();
        }

        return Optional.of(new Pair(longestSoFar, cnt));
    }

    static final class Pair {
        final String str;
        final int count;

        Pair(String str, int count) {
            this.str = str;
            this.count = count;
        }
    }

    private static Suffix[] createSuffixArray(String str) {

        Suffix[] suffixArr = new Suffix[str.length()];

        for (int i = 0; i < suffixArr.length; ++i) {
            suffixArr[i] = new Suffix(str, i);
        }

        Arrays.sort(suffixArr);

        return suffixArr;
    }

    private static final class Suffix implements Comparable<Suffix> {

        final String str;
        final int from;

        Suffix(String str, int from) {
            this.str = str;
            this.from = from;
        }

        @Override
        public int compareTo(Suffix other) {

            if (from == other.from) {
                return 0;
            }

            int i = from;
            int j = other.from;

            while (i < str.length() && j < str.length()) {

                char ch1 = str.charAt(i);
                char ch2 = str.charAt(j);

                int cmpRes = Character.compare(ch1, ch2);

                if (cmpRes != 0) {
                    return cmpRes;
                }

                ++i;
                ++j;
            }

            return i < str.length() ? 1 : -1;
        }

        @Override
        public String toString() {
            return from + ": " + str.substring(from);
        }

        static String commonPrefix(Suffix first, Suffix second) {

            int i = first.from;
            int j = second.from;

            int matchedLength = 0;

            while (i < first.str.length() && j < second.str.length()) {

                if (first.str.charAt(i) != second.str.charAt(j)) {
                    break;
                }

                ++matchedLength;
                ++i;
                ++j;
            }

            return first.str.substring(first.from, first.from + matchedLength);
        }
    }


    /*
    11512 GATTACA
    10065 Useless Tile Packers
     */

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
            new Uva_11512();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


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
