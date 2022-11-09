package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * https://vjudge.net/problem/UVA-11292
 * <p>
 * https://vjudge.net/problem
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_11292 {

    private Uva_11292() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String[] data = rd.readLine().trim().split("\\s+");

                int headsCnt = Integer.parseInt(data[0]);
                int knightsCnt = Integer.parseInt(data[1]);

                if (headsCnt == 0 && knightsCnt == 0) {
                    break;
                }

                int[] heads = new int[headsCnt];
                for (int i = 0; i < heads.length; ++i) {
                    heads[i] = Integer.parseInt(rd.readLine().trim());
                }

                int[] knights = new int[knightsCnt];
                for (int i = 0; i < knights.length; ++i) {
                    knights[i] = Integer.parseInt(rd.readLine().trim());
                }

                int optimalSol = findOptimalSolution(heads, knights);

                if (optimalSol >= 0) {
                    out.println(optimalSol);
                }
                else {
                    out.println("Loowater is doomed!");
                }
            }

            diff();
        }
    }

    private int findOptimalSolution(int[] heads, int[] knights) {

        if (heads.length > knights.length) {
            return -1;
        }

        Arrays.sort(heads);
        Arrays.sort(knights);

        int money = 0;
        int headIndex = 0;

        for (int i = 0; i < knights.length && headIndex < heads.length; ++i) {
            if (heads[headIndex] <= knights[i]) {
                money += knights[i];
                ++headIndex;
            }
        }

        return headIndex == heads.length ? money : -1;
    }


    /*

    10360 Rat Attack - complete search
    10341 Solve It - bisection method (divide-and-conquer)
    11292 Dragon of Loowater <---
    11450 Wedding Shopping
    10911 Forming Quiz Teams
    11635 Hotel Booking
    11506 Angry Programmer
    10243 Fire! Fire!! Fire!!!
    10717 Mint
    11512 GATTACA
    10065 Useless Tile Packers

     */


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
            new Uva_11292();
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
