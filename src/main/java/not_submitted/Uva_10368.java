package not_submitted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * UVa 10368 - Euclid’s Game.
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10368 {

    private Uva_10368() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        final String[] players = {"Stan", "Ollie"};

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String line = rd.readLine();

                String[] values = line.trim().split("\\s+");

                int a = Integer.parseInt(values[0]);
                int b = Integer.parseInt(values[1]);

                if (a == 0 && b == 0) {
                    break;
                }

                int winnerIndex = calculateWinner(Math.max(a, b), Math.min(a, b));

                assert winnerIndex >= 0 && winnerIndex < players.length;

                out.printf("%s wins%n", players[winnerIndex]);

                System.out.println("OVERLAPS_COUNT: " + OVERLAPS_COUNT);
            }

            diff();
        }
    }

    private static final Map<NumbersPair, WinnerOrder> CACHE = new HashMap<>();

    private static int OVERLAPS_COUNT = 0;

    private int calculateWinner(int bigger1, int smaller1) {

        PartialState rootState = new PartialState(0, bigger1, smaller1);

        Deque<PartialState> stack = new ArrayDeque<>();
        stack.push(rootState);

        while (!stack.isEmpty()) {
            PartialState curState = stack.pop();

            if (curState.isEndState()) {
                continue;
            }

            NumbersPair key = new NumbersPair(curState.bigger, curState.smaller);

            WinnerOrder cachedWinner = CACHE.get(key);

            if (cachedWinner != null) {
                ++OVERLAPS_COUNT;
                curState.winner = (cachedWinner == WinnerOrder.CURRENT) ? curState.player : curState.otherPlayer();
                continue;
            }

            if (curState.children.isEmpty()) {

                stack.push(curState);

                for (int val = curState.smaller; val < curState.bigger; val += curState.smaller) {
                    int a = curState.bigger - val;

                    PartialState newChild = new PartialState(curState.otherPlayer(), Math.max(a, curState.smaller),
                            Math.min(a, curState.smaller));

                    curState.children.add(newChild);

                    stack.push(newChild);
                }
            }
            else {
                for (PartialState child : curState.children) {
                    if (child.winner == curState.player) {
                        curState.winner = child.winner;
                    }
                }

                if (curState.winner == -1) {
                    curState.winner = curState.otherPlayer();
                }

                CACHE.put(key, curState.player == curState.winner ? WinnerOrder.CURRENT : WinnerOrder.OTHER);
            }
        }

        return rootState.winner;
    }

    private enum WinnerOrder {
        CURRENT,
        OTHER
    }

    private static final class NumbersPair {
        final int bigger;
        final int smaller;

        NumbersPair(int bigger, int smaller) {
            this.bigger = bigger;
            this.smaller = smaller;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NumbersPair that = (NumbersPair) o;

            if (bigger != that.bigger) return false;
            return smaller == that.smaller;
        }

        @Override
        public int hashCode() {
            int result = bigger;
            result = 31 * result + smaller;
            return result;
        }
    }

    private static final class PartialState {

        final int player;
        final int bigger;
        final int smaller;

        final List<PartialState> children = new ArrayList<>();

        int winner = -1;

        PartialState(int player, int bigger, int smaller) {
            this.player = player;
            this.bigger = bigger;
            this.smaller = smaller;

            if (isEndState()) {
                winner = player;
            }

            assert this.player == 0 || this.player == 1;
            assert this.bigger >= 0 && this.bigger >= this.smaller;
            assert this.smaller >= 0;
        }

        int otherPlayer() {
            return (player + 1) % 2;
        }

        boolean isEndState() {
            return bigger % smaller == 0;
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
            new Uva_10368();
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
