package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * 10264 - The Most Potent Corner
 */
public class Uva_10264 {


    private Uva_10264() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                line = line.trim();

                final int dimensions = Integer.parseInt(line);

                int[] cornerWeights = new int[1 << dimensions];

                for (int i = 0; i < cornerWeights.length; ++i) {
                    cornerWeights[i] = Integer.parseInt(rd.readLine().trim());
                }

                int[] potentials = calculatePotentials(cornerWeights, dimensions);

                out.printf("%d%n", biggestSumOfTwoNeighbourPotentials(potentials, dimensions));
            }
            diff();
        }
    }

    private static int biggestSumOfTwoNeighbourPotentials(int[] potentials, int dimensions) {

        int maxSum = 0;

        for (int corner = 0; corner < potentials.length; ++corner) {

            int curPotential = potentials[corner];

            for (int neighbourCorner : neighbours(corner, dimensions)) {
                maxSum = Math.max(maxSum, curPotential + potentials[neighbourCorner]);
            }
        }
        return maxSum;
    }

    private static int[] calculatePotentials(int[] cornersWeight, int dimensions) {
        int[] potentials = new int[cornersWeight.length];

        for (int corner = 0; corner < cornersWeight.length; ++corner) {

            int curPotential = 0;

            for (int neighbourCorner : neighbours(corner, dimensions)) {
                // no overflow here, max possible value = 15 * 256 = 3840
                curPotential += cornersWeight[neighbourCorner];
            }

            potentials[corner] = curPotential;
        }

        return potentials;
    }

    private static int[] neighbours(int corner, int dimensions) {

        int[] neighbours = new int[dimensions];
        int mask = 1;

        for (int i = 0; i < dimensions; ++i) {
            neighbours[i] = corner ^ mask;
            mask <<= 1;
        }

        return neighbours;
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
            new Uva_10264();
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
