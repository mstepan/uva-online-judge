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
 *  11173 - Grey Codes
 */
public class Uva_11173 {

    private Uva_11173() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCount = Integer.parseInt(rd.readLine());

            for (int i = 0; i < testsCount; ++i) {
                String[] lineData = rd.readLine().split("\\s+");

                int bits = Integer.parseInt(lineData[0]);
                int index = Integer.parseInt(lineData[1]);

                out.println(greyCode(index, bits));
            }

            while (true) {
                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                int index = Integer.parseInt(line);
                out.println(greyCode(index, 5));
            }

            diff();
        }
    }

    private static int greyCode(int index, int bits) {

        if (index < 2) {
            return index;
        }

        int mod = 1 << (bits - 1);
        int value = 0;
        int k = index;

        while (mod != 0) {
            int digit = (k & mod) == 0 ? 0 : 1;
            value = (value << 1) | digit;

            if (digit == 1) {
                int offset = k - mod;
                k = (mod - 1) - offset;
            }
            mod >>= 1;
        }

        return value;
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
            new Uva_11173();
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
