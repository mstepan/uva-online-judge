package org.max.uva.solved;

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


/**
 * Useless Tile Packers, https://vjudge.net/problem/UVA-10065
 *
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10065 {

    private Uva_10065() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int testCaseIndex = 1;

            while (true) {
                int points = Integer.parseInt(rd.readLine().trim());

                if (points == 0) {
                    break;
                }

                Point[] allPoints = new Point[points];

                for (int i = 0; i < allPoints.length; ++i) {
                    String[] pointData = rd.readLine().trim().split("\\s+");
                    allPoints[i] = new Point(Integer.parseInt(pointData[0]), Integer.parseInt(pointData[1]));
                }

                Point[] convexHull = calculateConvexHull(allPoints);

                double curArea = calculateArea(allPoints);
                double convexArea = calculateArea(convexHull);

                double vestedSpaceInPercentage = 100.0 - ((curArea * 100.0) / convexArea);

                out.printf("Tile #%d%n", testCaseIndex);
                out.printf("Wasted Space = %.2f %%%n%n", vestedSpaceInPercentage);

                ++testCaseIndex;
            }

            diff();
        }
    }

    /**
     * Calculate are of a polygon specified by allPoints parameter.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    private double calculateArea(Point[] allPoints) {

        double res = 0.0;

        for (int i = 1; i < allPoints.length; ++i) {
            Point prev = allPoints[i - 1];
            Point cur = allPoints[i];
            res += prev.x * cur.y - prev.y * cur.x;
        }

        Point first = allPoints[0];
        Point last = allPoints[allPoints.length - 1];

        res += last.x * first.y - last.y * first.x;

        return Math.abs(res / 2);
    }

    /**
     * Calculate convex hull using Jarvis gift wrapping algorithm.
     * <p>
     * time: O(N^2)
     * space: O(N)
     */
    private Point[] calculateConvexHull(Point[] allPoints) {

        List<Point> convexHull = new ArrayList<>();

        boolean[] used = new boolean[allPoints.length];
        int startPointIndex = findLeftmostPoint(allPoints);

        convexHull.add(allPoints[startPointIndex]);

        used[startPointIndex] = true;

        int curIndex = startPointIndex;

        while (convexHull.size() < allPoints.length) {

            int nextIndex = findNextHullPoint(curIndex, allPoints, used);

            if (nextIndex == startPointIndex) {
                break;
            }

            used[nextIndex] = true;
            convexHull.add(allPoints[nextIndex]);
            curIndex = nextIndex;
        }

        assert convexHull.size() >= 2 : "Convex hull too small, only " + convexHull.size() + " points.";
        return convexHull.toArray(new Point[0]);
    }

    private static int findNextHullPoint(int baseIndex, Point[] allPoints, boolean[] used) {

        final Point base = allPoints[baseIndex];

        int candidateIndex = findUnused(used);

        Point candidate = allPoints[candidateIndex];

        for (int i = 0; i < allPoints.length; ++i) {

            Point singlePoint = allPoints[i];

            if (singlePoint == candidate || singlePoint == base) {
                continue;
            }

            Orientation orient = Point.calculateOrientation(base, candidate, singlePoint);

            if (orient == Orientation.COLLINEAR) {

                double d1 = Point.distance(base, candidate);
                double d2 = Point.distance(base, singlePoint);

                if (Double.compare(d1, d2) < 0) {
                    candidate = singlePoint;
                    candidateIndex = i;
                }

            }
            else if (orient == Orientation.CLOCK) {
                candidate = singlePoint;
                candidateIndex = i;
            }
        }


        return candidateIndex;
    }

    private static int findUnused(boolean[] used) {

        for (int i = 0; i < used.length; ++i) {
            if (!used[i]) {
                return i;
            }
        }

        return -1;
    }

    private int findLeftmostPoint(Point[] allPoints) {

        int leftIndex = 0;
        Point left = allPoints[0];

        for (int i = 1; i < allPoints.length; ++i) {
            Point cur = allPoints[i];

            if (cur.x < left.x || (cur.x == left.x && cur.y < left.y)) {
                leftIndex = i;
                left = cur;
            }
        }

        return leftIndex;
    }

    enum Orientation {
        CLOCK,
        COUNTER_CLOCK,
        COLLINEAR
    }

    static final class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Calculate product of 2 vectors AB x BC.
         * If product 0 - points collinear
         * If product < 0 - counterclockwise turn (point C located left from AB).
         * If product > 0 - clockwise turn (point C located right from AB.
         */
        static Orientation calculateOrientation(Point a, Point b, Point c) {

            int product = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);

            if (product == 0) {
                return Orientation.COLLINEAR;
            }

            return product < 0 ? Orientation.COUNTER_CLOCK : Orientation.CLOCK;
        }

        static double distance(Point base, Point other) {

            double dx = (double) base.x - other.x;
            double dy = (double) base.y - other.y;

            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Point other = (Point) obj;

            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
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
            new Uva_10065();
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
