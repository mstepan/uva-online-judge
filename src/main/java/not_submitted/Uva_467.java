package not_submitted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Synching Signals (https://vjudge.net/problem/UVA-467).
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_467 {

    private static final String RESULT_MSG_FORMAT =
            "Set %d synchs again at %d minute(s) and %d second(s) after all turning green.%n";

    private static final String NO_SOLUTION_MSG = "Set %d is unable to synch after one hour.%n";

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 60 * 60;

    private Uva_467() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine();
            int index = 1;

            while (line != null) {
                String[] data = line.split(" ");

                final int[] cycles = toCycles(data);

                Optional<Integer> allGreenTime = findAllTurningGreenSecondTimeOffset(cycles);

                if (allGreenTime.isPresent()) {
                    final int timeInSec = allGreenTime.get();

                    final int minutes = timeInSec / SECONDS_IN_MINUTE;
                    final int leftSeconds = timeInSec % SECONDS_IN_MINUTE;

                    out.printf(RESULT_MSG_FORMAT, index, minutes, leftSeconds);
                }
                else {
                    out.printf(NO_SOLUTION_MSG, index);
                }

                line = rd.readLine();
                ++index;
            }

            diff();
        }
    }

    private static int[] toCycles(String[] data) {
        int[] cycles = new int[data.length];

        for (int i = 0; i < data.length; ++i) {
            cycles[i] = Integer.parseInt(data[i].trim());
        }

        return cycles;
    }

    private static Boundary[] toBoundary(int[] cycles) {
        Boundary[] boundaries = new Boundary[cycles.length];

        for (int i = 0; i < boundaries.length; ++i) {
            boundaries[i] = Boundary.fromCycle(cycles[i]);
        }

        return boundaries;
    }

    private Optional<Integer> findAllTurningGreenSecondTimeOffset(int[] cycles) {
        Boundary[] boundaries = toBoundary(cycles);

        while (allStartsBeforeHour(boundaries) && !allHasIntersection(boundaries)) {
            moveLeftmostIntervalOneCycle(boundaries);
        }

        Boundary allGreenFirstTime = findIntersection(boundaries);

        if (allGreenFirstTime == null) {
            return Optional.empty();
        }

        moveAllOneCycle(boundaries);

        while (allStartsBeforeHour(boundaries)) {

            Boundary intersection = findIntersection(boundaries);

            if (intersection != null) {
                return Optional.of(intersection.from - allGreenFirstTime.from);
            }

            moveLeftmostIntervalOneCycle(boundaries);
        }

        return Optional.empty();
    }

    private static void moveLeftmostIntervalOneCycle(Boundary[] boundaries) {
        Boundary leftmost = findLeftmost(boundaries);
        leftmost.moveOneCycle();
    }

    private static Boundary findLeftmost(Boundary[] boundaries) {

        Boundary left = boundaries[0];

        for (int i = 1; i < boundaries.length; ++i) {
            Boundary cur = boundaries[i];

            if (cur.from < left.from || (cur.from == left.from && cur.to < left.to)) {
                left = cur;
            }
        }

        return left;
    }

    private void moveAllOneCycle(Boundary[] boundaries) {
        for (Boundary singleBoundary : boundaries) {
            singleBoundary.moveOneCycle();
        }
    }

    private boolean allHasIntersection(Boundary[] boundaries) {
        return findIntersection(boundaries) != null;
    }

    private Boundary findIntersection(Boundary[] boundaries) {

        if (boundaries.length < 2) {
            return null;
        }

        Boundary intersection = boundaries[0];

        for (int i = 1; i < boundaries.length && intersection != null; ++i) {
            intersection = Boundary.intersect(intersection, boundaries[i]);
        }

        return intersection;
    }

    private boolean allStartsBeforeHour(Boundary[] boundaries) {
        for (Boundary singleBoundary : boundaries) {
            if (singleBoundary.from > SECONDS_IN_HOUR) {
                return false;
            }
        }

        return true;
    }

    private static final class Boundary {

        private static final int YELLOW_LENGTH = 5;

        int from;
        int to;
        final int cycle;

        static Boundary fromCycle(int cycle) {
            return new Boundary(cycle, cycle + (cycle - YELLOW_LENGTH), cycle);
        }

        static Boundary intersect(Boundary first, Boundary second) {

            Boundary left = first;
            Boundary right = second;

            if (right.from < left.from) {
                left = second;
                right = first;
            }

            if (left.to <= right.from) {
                // no intersection
                return null;
            }

            return new Boundary(right.from, Math.min(right.to, left.to), -1);
        }

        void moveOneCycle() {
            from += (2 * cycle);
            to += (2 * cycle);
        }

        Boundary(int from, int to, int cycle) {
            this.from = from;
            this.to = to;
            this.cycle = cycle;
        }

        @Override
        public String toString() {
            return "[" + from + ", " + to + "]";
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
            new Uva_467();
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
