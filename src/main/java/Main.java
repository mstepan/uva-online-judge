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
 * UVA-10219: Find the ways !
 */
public class Main {


    private Main() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine();

            while (line != null) {

                String[] data = line.trim().split("\\s+");

                int n = Integer.parseInt(data[0]);
                int k = Integer.parseInt(data[1]);

//                long starTime = System.currentTimeMillis();
                out.println(combinationsCnt2(n, k).toString().length());
//                long endTime = System.currentTimeMillis();


//                System.out.println("time: " + (endTime - starTime) + " ms");

                line = rd.readLine();
            }

            diff();
        }
    }

    private static BigInteger fac(int value) {
        return fac(1, value);
    }

    private static BigInteger fac(int from, int to) {

        BigInteger res = BigInteger.ONE;

        for (int i = from; i <= to; ++i) {
            res = res.multiply(BigInteger.valueOf(i));
        }

        return res;
    }

    private static BigInteger combinationsCnt2(int n, int k) {

        int diff = n - k;

        if (diff > k) {
            return fac(diff + 1, n).divide(fac(k));
        }

        return fac(k + 1, n).divide(fac(diff));
    }

//    private static BigInteger combinationsCnt(int n, int k) {
//
//        final int cols = k + 1;
//
//        BigInteger[] prev = new BigInteger[cols];
//        prev[0] = BigInteger.ONE;
//
//        for (int i = 0; i < prev.length; ++i) {
//            prev[i] = BigInteger.ZERO;
//        }
//
//        for (int row = 1; row <= n; ++row) {
//
//            BigInteger[] cur = new BigInteger[cols];
//            cur[0] = BigInteger.ONE;
//
//            for (int col = 1; col < cols; ++col) {
//                cur[col] = prev[col].add(prev[col - 1]);
//            }
//
//            prev = cur;
//        }
//
//        return prev[cols - 1];
//    }


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
                .exec(java.lang.String.format("/usr/bin/diff %s %s",
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
            new Main();
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
