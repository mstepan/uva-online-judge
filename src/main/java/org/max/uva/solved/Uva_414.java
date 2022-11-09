package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Machined Surfaces (https://vjudge.net/problem/UVA-414).
 *
 * N <= 13 (rows) ~ O(1)
 * M = 25 (cols) ~ O(1)
 * K = images count
 *
 * time: O(K)
 * space: O(1)
 *
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_414 {

    private static final int COLUMNS_COUNT = 25;
    private static final int MAX_ROW_SIZE = 12;

    private Uva_414() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int imageSize = Integer.parseInt(rd.readLine().trim());

            assert imageSize >= 0 && imageSize <= MAX_ROW_SIZE;

            while (imageSize != 0) {

                RowSlice[] slices = new RowSlice[imageSize];

                for (int row = 0; row < imageSize; ++row) {
                    String line = rd.readLine();

                    assert line != null : "null line detected instead of a row";

                    line = line.trim();

                    assert line.length() == COLUMNS_COUNT : "incorrect columns count detected in a row of image";

                    slices[row] = RowSlice.create(line);
                }

                out.println(calculateVoid(slices));

                imageSize = Integer.parseInt(rd.readLine().trim());
            }

            diff();
        }
    }

    private static int calculateVoid(RowSlice[] slices) {

        int maxValue = 0;
        for (RowSlice singleSlice : slices) {
            maxValue = Math.max(maxValue, singleSlice.sum());
        }

        assert maxValue > 0 && maxValue <= COLUMNS_COUNT;

        int voidSize = 0;
        for (RowSlice singleSlice : slices) {
            voidSize += (maxValue - singleSlice.sum());
        }

        assert voidSize >= 0;

        return voidSize;
    }

    private static final class RowSlice {
        final int left;
        final int right;

        RowSlice(int left, int right) {
            this.left = left;
            this.right = right;
        }

        static RowSlice create(String line) {

            final int lineLength = line.length();

            int leftLength = 0;

            for (int i = 0; i < lineLength; ++i) {
                if (line.charAt(i) != 'X') {
                    break;
                }

                ++leftLength;
            }

            if( leftLength == line.length() ){
                return new RowSlice(leftLength, 0);
            }

            assert leftLength > 0;

            int rightLength = 0;
            for (int i = lineLength - 1; i >= 0; --i, ++rightLength) {
                if (line.charAt(i) != 'X') {
                    break;
                }
            }

            assert rightLength > 0;

            assert leftLength + rightLength <= COLUMNS_COUNT;

            return new RowSlice(leftLength, rightLength);
        }

        int sum() {
            return left + right;
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
            new Uva_414();
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
