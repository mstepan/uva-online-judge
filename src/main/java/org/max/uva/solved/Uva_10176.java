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
 * <a href="https://vjudge.net/problem/UVA-10176">UVA-10176: Ocean Deep! - Make it shallow!! </a>
 */
public class Uva_10176 {

    private static void mainLogic() throws IOException, InterruptedException {
        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            while (true) {
                String line = readBinaryStr(reader);

                if (line == null) {
                    break;
                }

                if (remain(line, M) == 0) {
                    out.println("YES");
                }
                else {
                    out.println("NO");
                }
            }

            diff();
        }
    }

    private static String readBinaryStr(BufferedReader reader) throws IOException {

        StringBuilder res = new StringBuilder();

        while (true) {
            String line = reader.readLine();

            if (line == null) {
                break;
            }

            if (line.endsWith("#")) {
                res.append(line.substring(0, line.length() - 1).trim());
                break;
            }
            else {
                res.append(line.trim());
            }

        }

        return res.length() == 0 ? null : res.toString();
    }

    private static final int M = 131_071;

    public static int remain(String binaryValue, int m) {
        Objects.requireNonNull(binaryValue);

        final char[] arr = binaryValue.trim().toCharArray();

        final int chunkSize = 8;
        Chunk[] chunks = toChunks(arr, chunkSize);

        int res = 0;

        for (Chunk singleChunk : chunks) {
            res = ((res << singleChunk.bitsCount) + singleChunk.digit) % m;
        }

        return res;
    }

    static class Chunk {
        final int digit;
        final int bitsCount;

        public Chunk(int digit, int bitsCount) {
            this.digit = digit;
            this.bitsCount = bitsCount;
        }
    }

    private static Chunk[] toChunks(char[] arr, int chunkSize) {
        assert arr != null;
        assert chunkSize > 0 && chunkSize <= 16;

        int from = 0;
        int to = Math.min(from + chunkSize, arr.length);

        Chunk[] res = new Chunk[(int) Math.ceil((double) arr.length / chunkSize)];
        int index = 0;

        while (from < arr.length) {
            int val = 0;

            for (int i = from; i < to; ++i) {
                val = (val << 1) + (arr[i] - '0');
            }
            res[index] = new Chunk(val, to - from);
            ++index;

            from = to;
            to = Math.min(to + chunkSize, arr.length);
        }

        return res;
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
