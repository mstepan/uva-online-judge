package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 10976 - Fractions Again?!
 */
public class Uva_10976 {

    private Uva_10976() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                int k = Integer.parseInt(line.trim());

                List<String> res = countSolutions(k);

                out.println(res.size());

                for (String singleSolution : res) {
                    out.println(singleSolution);
                }
            }

            diff();
        }
    }

    private static List<String> countSolutions(int k) {

        List<String> res = new ArrayList<>();

        for (int y = k + 1; y <= 2 * k; ++y) {

            int[] divAndRem = subtract(k, y);

            if (divAndRem[1] == 0) {
                int x = divAndRem[0];
                res.add(String.format("1/%d = 1/%d + 1/%d", k, x, y));
            }
        }

        return res;
    }

    private static int[] subtract(int k, int y) {

        int denom = (k * y) / gcd(k, y);

        int num = denom / k - denom / y;

        int div = denom / num;
        int rem = denom % num;

        return new int[]{div, rem};

    }

    private static int gcd(int x, int y) {

        int a = x;
        int b = y;

        while (b != 0) {
            int rem = a % b;
            a = b;
            b = rem;
        }

        return a;

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
            new Uva_10976();
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
