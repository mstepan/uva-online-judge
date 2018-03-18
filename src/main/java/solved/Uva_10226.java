package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 10226 - Hardwood Species
 */
public class Uva_10226 {


    private Uva_10226() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCases = Integer.parseInt(rd.readLine().trim());
            rd.readLine();

            for (int i = 0; i < testCases; ++i) {

                Map<String, Integer> treesFreq = new HashMap<>();
                int totalTreesCnt = 0;

                while (true) {
                    String tree = rd.readLine();

                    if (tree == null) {
                        break;
                    }

                    tree = tree.trim();

                    if ("".equals(tree)) {
                        break;
                    }

                    treesFreq.compute(tree, (treeName, cnt) -> cnt == null ? 1 : cnt + 1);
                    ++totalTreesCnt;
                }

                List<TreeStat> stats = TreeStat.create(treesFreq, totalTreesCnt);
                stats.sort(TreeStat.NAME_ASC);

                if (i != 0) {
                    out.println();
                }
                for (TreeStat singleStat : stats) {
                    out.printf("%s %.4f%n", singleStat.name, singleStat.percentage);
                }
            }

            diff();
        }
    }

    private static class TreeStat {

        static final Comparator<TreeStat> NAME_ASC = Comparator.comparing(TreeStat::getName);

        static List<TreeStat> create(Map<String, Integer> freq, int totalCnt) {

            List<TreeStat> res = new ArrayList<>();

            for (Map.Entry<String, Integer> treeEntry : freq.entrySet()) {
                res.add(new TreeStat(treeEntry.getKey(), (treeEntry.getValue() * 100.0) / totalCnt));
            }

            return res;
        }

        final String name;
        final double percentage;

        public TreeStat(String name, double percentage) {
            this.name = name;
            this.percentage = percentage;
        }

        String getName() {
            return name;
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
            new Uva_10226();
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
