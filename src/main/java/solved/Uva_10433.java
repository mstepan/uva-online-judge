package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Automorphic Numbers
 * <p>
 * https://vjudge.net/problem/UVA-10433
 */
public class Uva_10433 {

    private Uva_10433() throws IOException, InterruptedException {


        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String valStr = reader.readLine();

                if (valStr == null) {
                    break;
                }

                valStr = valStr.trim();

                BigInteger val = new BigInteger(valStr);

                BigInteger square = val.multiply(val);

                String squareStr = square.toString();

                if (squareStr.endsWith(valStr)) {
                    out.printf("Automorphic number of %d-digit.%n", valStr.length());
                }
                else {
                    out.println("Not an Automorphic number.");
                }

            }

            diff();
        }
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
            new Uva_10433();
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
