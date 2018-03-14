package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.function.Consumer;

/**
 * 11926 - Multitasking
 */
public class Uva_11926 {


    private Uva_11926() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                BitSet calendar = new BitSet(1_000_001);

                String line = rd.readLine().trim();

                String[] data = line.split("\\s+");

                int n = Integer.parseInt(data[0]);
                int m = Integer.parseInt(data[1]);

                if (n == 0 && m == 0) {
                    break;
                }

                boolean hasConflictFlag = false;

                for (int i = 0; i < n; ++i) {
                    String[] oneTimeStr = rd.readLine().trim().split("\\s+");

                    if (hasConflictFlag) {
                        continue;
                    }

                    int start = Integer.parseInt(oneTimeStr[0]);
                    int end = Integer.parseInt(oneTimeStr[1]);

                    boolean curConflict = hasConflict(calendar, start, end);

                    if (curConflict) {
                        hasConflictFlag = true;
                    }

                    updateCalendar(calendar, start, end);
                }

                for (int i = 0; i < m; ++i) {
                    String[] repeatedStr = rd.readLine().trim().split("\\s+");

                    if (hasConflictFlag) {
                        continue;
                    }

                    int start = Integer.parseInt(repeatedStr[0]);
                    int end = Integer.parseInt(repeatedStr[1]);
                    int repeatOffset = Integer.parseInt(repeatedStr[2]);

                    while (!hasConflictFlag && start < 1_000_001) {

                        boolean curConflict = hasConflict(calendar, start, end);

                        if (curConflict) {
                            hasConflictFlag = true;
                        }

                        updateCalendar(calendar, start, end);

                        start += repeatOffset;
                        end += repeatOffset;
                    }
                }

                out.println(hasConflictFlag ? "CONFLICT" : "NO CONFLICT");

            }

            diff();
        }
    }

    private static boolean hasConflict(BitSet calendar, int start, int end) {
        BitSet conflict = calendar.get(start, end);
        return conflict.cardinality() > 0 ;
    }

    private static void updateCalendar(BitSet calendar, int start, int end) {
        calendar.set(start, end, true);
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
            new Uva_11926();
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
