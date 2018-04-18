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
import java.util.stream.Collectors;

/**
 * 1237 - Expert Enough?
 */
public class Uva_1237 {

    private Uva_1237() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testsCnt = Integer.parseInt(rd.readLine().trim());

            for (int test = 0; test < testsCnt; ++test) {

                if( test > 0 ){
                    out.println();
                }

                List<MachineWithBoundary> allMachines = new ArrayList<>();

                int machinesCnt = Integer.parseInt(rd.readLine().trim());

                for (int i = 0; i < machinesCnt; ++i) {

                    // HONDA 10000 45000
                    String[] machineData = rd.readLine().trim().split("\\s");

                    String name = machineData[0];
                    int lo = Integer.parseInt(machineData[1]);
                    int hi = Integer.parseInt(machineData[2]);

                    allMachines.add(new MachineWithBoundary(name, lo, hi));
                }

                int queriesCnt = Integer.parseInt(rd.readLine().trim());

                for (int q = 0; q < queriesCnt; ++q) {
                    int price = Integer.parseInt(rd.readLine().trim());

                    List<MachineWithBoundary> matchedResults = findMatchedValues(price, allMachines);

                    if (matchedResults.size() == 1) {
                        out.println(matchedResults.get(0).name);
                    }
                    else {
                        out.println("UNDETERMINED");
                    }
                }
            }

            diff();
        }
    }

    private static List<MachineWithBoundary> findMatchedValues(int price, List<MachineWithBoundary> allMachines) {
        return allMachines.stream().
                filter(machineWithBoundary ->
                        price >= machineWithBoundary.lo && price <= machineWithBoundary.hi).
                collect(Collectors.toList());
    }

    private static class MachineWithBoundary {

        final String name;
        final int lo;
        final int hi;

        MachineWithBoundary(String name, int lo, int hi) {
            this.name = name;
            this.lo = lo;
            this.hi = hi;
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
            new Uva_1237();
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
