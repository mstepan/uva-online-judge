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

/**
 * <a href="https://vjudge.net/problem/UVA-11173">UVA-11173: Grey Codes</a>
 */
public class Uva_11173_complex_solution {

    private static void mainLogic() throws IOException, InterruptedException {
        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            int testCases = Integer.parseInt(reader.readLine().trim());

            for(int testIdx = 0; testIdx < testCases; ++testIdx){
                String[] bitsAndIndexParts = reader.readLine().split("\\s+");

                int bitsCount = Integer.parseInt(bitsAndIndexParts[0]);
                int index = Integer.parseInt(bitsAndIndexParts[1]);

                int grayCode = binaryToGray(bitsCount, index);

                out.println(grayCode);
            }

            diff();
        }
    }

    /**
     * Convert binary value to Gray code value.
     */
    public static int binaryToGray(int bitsCount, int value) {

        int grayCode = value;

        for (int div = 2; div < (1 << bitsCount); div <<= 1) {
            int chunkIdx = value / div;

            // odd case, flit bit
            if ((chunkIdx & 1) != 0) {
                grayCode ^= (div >> 1);
            }
        }

        return grayCode;
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
