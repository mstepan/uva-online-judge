package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <a href="https://vjudge.net/problem/UVA-941">UVA-941: Permutations</a>
 */
public class Uva_941 {


    private static void mainLogic() throws IOException, InterruptedException {
        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            int samplesCount = Integer.parseInt(reader.readLine().trim());

            for (int i = 0; i < samplesCount; ++i) {
                String str = reader.readLine().trim();
                long permutationIndex = Long.parseLong(reader.readLine().trim());

                Optional<String> permutationByIndex = findNthPermutation(str, permutationIndex);

                out.println(permutationByIndex.get());
            }

            diff();
        }
    }

    /**
     * Use factorial number system to find n-th permutation.
     * See: https://www.quora.com/How-can-I-mathematically-get-n-th-lexicographic-permutation-of-a-set-without-actually
     * -writing-them-out
     * See: https://en.wikipedia.org/wiki/Factorial_number_system
     */
    private static Optional<String> findNthPermutation(String str, long permutationIndex) {

        long facValue = fac(str.length());

        if (permutationIndex >= facValue ) {
            return Optional.empty();
        }

        final char[] strArr = str.toCharArray();
        Arrays.sort(strArr);

        final StringBuilder res = new StringBuilder(strArr.length);
        final CharArrayWithUsed arr = new CharArrayWithUsed(strArr);

        long leftPermIndex = permutationIndex;

        for (int facIndex = str.length() - 1; facIndex > 0; --facIndex) {
            facValue = facValue / (facIndex+1);

            if (facValue <= leftPermIndex) {
                int elemIndex = (int) (leftPermIndex / facValue);

                char ch = arr.useAtIndex(elemIndex);
                res.append(ch);

                leftPermIndex -= (elemIndex * facValue);
            }
            else {
                res.append(arr.useAtIndex(0));
            }
        }

        res.append(arr.useAtIndex(0));

        return Optional.of(res.toString());
    }

    private static class CharArrayWithUsed {
        final char[] arr;
        final boolean[] used;

        CharArrayWithUsed(char[] arr) {
            this.arr = arr;
            this.used = new boolean[arr.length];
        }

        public char useAtIndex(int elemIndex) {
            for (int i = 0, curIndex = 0; i < arr.length; ++i) {
                if (!used[i]) {
                    if (curIndex == elemIndex) {
                        used[i] = true;
                        return arr[i];
                    }
                    ++curIndex;
                }
            }

            throw new IllegalArgumentException("All elements are used");
        }
    }

    private static long fac(int value) {

        assert value <= 20 : "value is too big to calculate factorial using 'long' as a type";

        long res = 1L;

        for (long i = 2L; i <= value; ++i) {
            res *= i;
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
