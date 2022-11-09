package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;


/**
 * Clock Hands.
 * https://vjudge.net/problem/UVA-579
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_579 {

    private Uva_579() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String line = rd.readLine().trim();

                if ("0:00".equals(line)) {
                    break;
                }

                String[] data = line.split(":");

                double hours = Double.parseDouble(data[0]);
                double minutes = Double.parseDouble(data[1]);

                out.printf("%.3f%n", calculateAngle(hours, minutes));
            }

            diff();
        }
    }

    private static final double MIN_TO_ANGLE_MULTIPLIER = 6.0;
    private static final double HOUR_COEFFICIENT = 5.0;

    private static final double MINS_60 = 60.0;
    private static final double MINS_30 = 30.0;

    private static double calculateAngle(double hours, double minutes) {

        double hDegree = (hours + (minutes / MINS_60)) * HOUR_COEFFICIENT;
        double mDegree = minutes;

        if (Double.compare(Math.abs(hDegree - mDegree), MINS_30) > 0) {
            if (Double.compare(hDegree, mDegree) < 0) {
                hDegree += MINS_60;
            }
            else {
                mDegree += MINS_60;
            }
        }

        if (Double.compare(hDegree, mDegree) > 0) {
            return (hDegree - mDegree) * MIN_TO_ANGLE_MULTIPLIER;
        }

        return (mDegree - hDegree) * MIN_TO_ANGLE_MULTIPLIER;
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
            new Uva_579();
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
