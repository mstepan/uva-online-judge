package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Permutation Arrays (https://vjudge.net/problem/UVA-482)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_482 {


    private Uva_482() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {


            final int testCases = Integer.parseInt(rd.readLine().trim());

            for (int testNo = 0; testNo < testCases; ++testNo) {

                // skip one empty line
                rd.readLine();

                final int[] indexes = readIndexes(rd);
                final String[] arr = rd.readLine().split(" ");


                if( testNo != 0 ){
                    out.println();
                }

                printInPermutationOrder(arr, indexes, out);
            }

            diff();
        }
    }

    private void printInPermutationOrder(String[] arr, int[] indexes, PrintStream out) throws IOException {

        assert arr.length == indexes.length;

        Map<Integer, String> indexToValue = new HashMap<>();

        for (int i = 0; i < indexes.length; ++i) {
            int curIndex = indexes[i];
            String curValue = arr[i];
            indexToValue.put(curIndex, curValue);
        }

        for (int i = 1; indexToValue.containsKey(i); ++i) {
            out.println(indexToValue.get(i));
        }
    }

    private static int[] readIndexes(BufferedReader rd) throws IOException {
        String[] data = rd.readLine().split(" ");

        int[] indexes = new int[data.length];

        for (int i = 0; i < data.length; ++i) {
            indexes[i] = Integer.parseInt(data[i].trim());
        }

        return indexes;
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
            new Uva_482();
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
