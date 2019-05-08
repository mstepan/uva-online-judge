package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Hartals (https://vjudge.net/problem/UVA-10050)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10050 {


    private Uva_10050() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCasesCount = Integer.parseInt(rd.readLine().trim());

            for (int testCase = 0; testCase < testCasesCount; ++testCase) {
                final int days = Integer.parseInt(rd.readLine().trim());

                final int partiesCount = Integer.parseInt(rd.readLine().trim());

                int[] cycles = new int[partiesCount];

                for (int i = 0; i < cycles.length; ++i) {
                    cycles[i] = Integer.parseInt(rd.readLine().trim());
                }

                out.println(calculateLostDays(days, cycles));

            }

            diff();
        }
    }

    private int calculateLostDays(int days, int[] cycles) {

        boolean[] notWorkingDays = new boolean[days + 1];

        for (int i = 6; i < notWorkingDays.length; i += 7) {
            notWorkingDays[i] = true;
        }

        for (int i = 7; i < notWorkingDays.length; i += 7) {
            notWorkingDays[i] = true;
        }

        Set<Integer> hartsDays = new HashSet<>();

        for (int singleCycle : cycles) {
            for (int day = singleCycle; day <= days; day += singleCycle) {
                if (!notWorkingDays[day]) {
                    hartsDays.add(day);
                }
            }
        }

        return hartsDays.size();
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
            new Uva_10050();
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
