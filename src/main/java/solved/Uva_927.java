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
 * 927 - Integer Sequences from Addition of Terms
 */
public class Uva_927 {

    private Uva_927() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCnt = Integer.parseInt(rd.readLine().trim());

            for (int test = 0; test < testsCnt; ++test) {

                String[] polData = rd.readLine().trim().split("\\s+");

                int[] coeff = new int[Integer.parseInt(polData[0]) + 1];

                for (int i = 0; i < coeff.length; ++i) {
                    coeff[i] = Integer.parseInt(polData[i + 1]);
                }

                int d = Integer.parseInt(rd.readLine().trim());
                int k = Integer.parseInt(rd.readLine().trim());

                long seqNumber = findSequenceNumber(coeff, d, k);

                out.println(seqNumber);
            }

            diff();
        }
    }

    private static long findSequenceNumber(int[] coeff, int d, int k) {


        int x = 1;

        int upperBound = d;
        int offset = d;

        while (k > upperBound) {
            offset += d;
            upperBound += offset;
            ++x;
        }

        long res = 0L;

        for (int i = coeff.length - 1; i >= 0; --i) {

            int c = coeff[i];

            res = res * x + c;
        }

        return res;
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
            new Uva_927();
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
