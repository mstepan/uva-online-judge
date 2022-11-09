package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * https://vjudge.net/problem/UVA-11450
 * <p>
 * https://vjudge.net/problem
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_11450 {

    private Uva_11450() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCasesCnt = Integer.parseInt(rd.readLine().trim());

            for (int testCaseIndex = 0; testCaseIndex < testCasesCnt; ++testCaseIndex) {

                String[] moneyAndGarments = rd.readLine().trim().split("\\s+");

                final int money = Integer.parseInt(moneyAndGarments[0]);
                final int garmentsCnt = Integer.parseInt(moneyAndGarments[1]);

                List<int[]> garments = new ArrayList<>();

                for (int garmentIndex = 0; garmentIndex < garmentsCnt; ++garmentIndex) {
                    String[] garmentData = rd.readLine().trim().split("\\s+");

                    int[] curGarments = new int[Integer.parseInt(garmentData[0])];

                    for (int i = 0; i < curGarments.length; ++i) {
                        curGarments[i] = Integer.parseInt(garmentData[i + 1]);
                    }

                    garments.add(curGarments);
                }

                int optSol = findOptinalSolution(garments, money);

                if (optSol >= 0) {
                    out.println(optSol);
                }
                else {
                    out.println("no solution");
                }
            }

            diff();
        }
    }

    private int findOptinalSolution(List<int[]> garments, int money) {

        int[] prevSol = new int[money + 1];

        for (int[] garmentsLine : garments) {

            int[] curSol = new int[money + 1];
            curSol[0] = Integer.MIN_VALUE;

            for (int i = 1; i < curSol.length; ++i) {

                int maxCur = Integer.MIN_VALUE;

                for (int garCost : garmentsLine) {
                    if (garCost <= i && prevSol[i - garCost] >= 0) {
                        maxCur = Math.max(maxCur, garCost + prevSol[i - garCost]);
                    }
                }

                curSol[i] = maxCur;
            }

            prevSol = curSol;
        }

        int optRes = findMax(prevSol);

        return optRes >= 0 ? optRes : -1;
    }

    private int findMax(int[] prevSol) {
        return Arrays.stream(prevSol).max().orElseGet(() -> -1);
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
            new Uva_11450();
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
