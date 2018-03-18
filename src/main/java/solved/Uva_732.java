package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * 732 - Anagrams by Stack
 */
public class Uva_732 {


    private Uva_732() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {
                String str = rd.readLine();

                if (str == null) {
                    break;
                }

                str = str.trim();
                String other = rd.readLine().trim();

                out.println("[");

                List<String> sequences = calculatePossibleTransformations(str, other);
                Collections.sort(sequences);

                for (String singleSeq : sequences) {
                    out.println(singleSeq);
                }

                out.println("]");
            }

            diff();
        }
    }

    private static List<String> calculatePossibleTransformations(String first, String second) {

        if (first.length() != second.length()) {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();

        calcRec(first.toCharArray(), 0, second.toCharArray(), 0, new ArrayDeque<>(), new ArrayDeque<>(),
                results);

        return results;
    }

    private static void calcRec(char[] first, int i, char[] second, int j, Deque<Character> stack,
                                Deque<String> ops, List<String> results) {

        if (stack.isEmpty() && i == first.length) {
            results.add(combineResult(ops));
            return;
        }

        if (!stack.isEmpty() && stack.peekFirst() == second[j]) {

            final char stackCh = stack.pop();
            ops.push("o");

            calcRec(first, i, second, j + 1, stack, ops, results);

            ops.pop();
            stack.push(stackCh);
        }


        if (i < first.length) {
            stack.push(first[i]);
            ops.push("i");

            calcRec(first, i + 1, second, j, stack, ops, results);

            stack.pop();
            ops.pop();
        }
    }

    private static String combineResult(Deque<String> ops) {
        StringBuilder buf = new StringBuilder();

        Iterator<String> revIt = ops.descendingIterator();

        while (revIt.hasNext()) {
            buf.append(revIt.next()).append(" ");
        }

        return buf.toString().trim();
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
            new Uva_732();
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
