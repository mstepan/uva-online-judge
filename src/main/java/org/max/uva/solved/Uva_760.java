package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * UVA-760: DNA Sequencing.
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_760 {

    private Uva_760() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String delimiter = "#";

            while (delimiter != null) {
                String first = rd.readLine().trim();
                String second = rd.readLine().trim();

                String smaller = first;
                String bigger = second;

                if (smaller.length() >= bigger.length()) {
                    String temp = smaller;
                    smaller = bigger;
                    bigger = temp;
                }

                List<String> res = findLongestCommonSubstrings(smaller, bigger);

                if (!delimiter.equals("#")) {
                    out.println();
                }

                if (res.isEmpty()) {
                    out.println("No common sequence.");
                }
                else {
                    for (String singleRes : res) {
                        out.println(singleRes);
                    }
                }

                delimiter = rd.readLine();
            }

            diff();
        }
    }

    /**
     * time: O(N*M)
     * space: O(N*M)
     */
    private static List<String> findLongestCommonSubstrings(String small, String big) {

        final int rowsCnt = small.length() + 1;
        final int colsCnt = big.length() + 1;

        List<Integer> maxPositions = new ArrayList<>();
        int maxLength = 0;

        int[] prev = new int[colsCnt];

        for (int row = 1; row < rowsCnt; ++row) {

            int[] cur = new int[colsCnt];

            char ch1 = small.charAt(row - 1);

            for (int col = 1; col < colsCnt; ++col) {

                char ch2 = big.charAt(col - 1);

                int curLength = (ch1 == ch2 ? prev[col - 1] + 1 : 0);

                if (curLength > maxLength) {
                    maxLength = curLength;
                    maxPositions.clear();
                    maxPositions.add(col - 1);
                }
                else if (maxLength != 0 && curLength == maxLength) {
                    maxPositions.add(col - 1);
                }

                cur[col] = curLength;
            }

            prev = cur;
        }


        List<String> longestSubstrings = gatherSubstringsWithMaxLength(maxLength, maxPositions, big);

        if (longestSubstrings.size() < 2) {
            return longestSubstrings;
        }

        Collections.sort(longestSubstrings);

        removeDuplicatesInSortedList(longestSubstrings);

        return longestSubstrings;
    }

    private static void removeDuplicatesInSortedList(List<String> longestSubstrings) {
        Iterator<String> it = longestSubstrings.iterator();
        String prevValue = it.next();

        while (it.hasNext()) {
            String curValue = it.next();

            if (curValue.equals(prevValue)) {
                it.remove();
            }
            else {
                prevValue = curValue;
            }
        }
    }

    private static List<String> gatherSubstringsWithMaxLength(int maxLength, List<Integer> maxPositions, String first) {

        List<String> res = new ArrayList<>();

        for (int pos : maxPositions) {
            res.add(first.substring(pos - maxLength + 1, pos + 1));
        }

        return res;
    }

    private static int findMax(int[][] sol) {

        int maxValue = 0;

        final int rowsCnt = sol.length;
        final int colsCnt = sol[0].length;

        for (int row = 1; row < rowsCnt; ++row) {
            for (int col = 1; col < colsCnt; ++col) {
                if (sol[row][col] > maxValue) {
                    maxValue = sol[row][col];
                }
            }
        }

        return maxValue;
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
            new Uva_760();
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
