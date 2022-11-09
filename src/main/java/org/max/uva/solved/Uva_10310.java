package org.max.uva.solved;

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
 * https://vjudge.net/problem/UVA-10310
 *
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10310 {

    private Uva_10310() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while(true) {

                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                String[] data = line.trim().split("\\s+");

                Location[] holes = new Location[Integer.parseInt(data[0])];
                Location gopher = new Location(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                Location dog = new Location(Double.parseDouble(data[3]), Double.parseDouble(data[4]));


                for (int i = 0; i < holes.length; ++i) {
                    String[] coordinates = rd.readLine().trim().split("\\s+");
                    holes[i] = new Location(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                }

                Optional<Location> escapeHole = findEscapeHole(gopher, dog, holes);


                if (escapeHole.isPresent()) {
                    out.printf("The gopher can escape through the hole at (%.3f,%.3f).%n",
                               escapeHole.get().x, escapeHole.get().y);
                }
                else {
                    out.println("The gopher cannot escape.");
                }

                rd.readLine();
            }

            diff();
        }
    }

    private Optional<Location> findEscapeHole(Location gopher, Location dog, Location[] holes) {

        for (Location singleHole : holes) {

            double gohperDistance = singleHole.distance(gopher);
            double dogDistance = singleHole.distance(dog);

            if (Double.compare(gohperDistance * 2, dogDistance) <= 0) {
                return Optional.of(singleHole);
            }

        }

        return Optional.empty();
    }

    private static final class Location {
        final double x;
        final double y;

        Location(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double distance(Location other) {
            double dx = x - other.x;
            double dy = y - other.y;

            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    /*

    10360 Rat Attack - complete search
    10341 Solve It - bisection method (divide-and-conquer)
    11292 Dragon of Loowater
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
            new Uva_10310();
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
