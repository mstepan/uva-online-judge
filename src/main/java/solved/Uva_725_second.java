package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 725 - Division
 */
public class Uva_725_second {

    private Uva_725_second() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            Map<Integer, List<IntPair>> allPairs = new HashMap<>();
            List<Integer> numbersInOrder = new ArrayList<>();

            while (true) {
                int n = Integer.parseInt(rd.readLine().trim());
                if (n == 0) {
                    break;
                }

                allPairs.put(n, new ArrayList<>());
                numbersInOrder.add(n);
            }

            calculateAllSolutions(allPairs);
            printAll(out, numbersInOrder, allPairs);

            diff();
        }
    }

    private static void calculateAllSolutions(Map<Integer, List<IntPair>> allPairs) {

        final int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        while (true) {

            int f = toNumber(arr, 0, 4);
            int a = toNumber(arr, 5, 9);

            if ((a % f) == 0) {
                int n = a / f;

                if (allPairs.containsKey(n)) {
                    List<IntPair> curPairs = allPairs.get(n);
                    curPairs.add(new IntPair(a, f));
                }
            }

            if (!nextPermutation(arr)) {
                break;
            }
        }
    }

    private static void printAll(PrintStream out, List<Integer> numbersInOrder, Map<Integer, List<IntPair>> allPairs) {

        for (int i = 0; i < numbersInOrder.size(); ++i) {
            int n = numbersInOrder.get(i);

            List<IntPair> pairs = allPairs.get(n);

            if (i != 0) {
                out.println();
            }

            if (pairs.isEmpty()) {
                out.printf("There are no solutions for %d.%n", n);
            }
            else {
                for (IntPair singlePair : pairs) {
                    out.printf("%05d / %05d = %d%n", singlePair.a, singlePair.f, n);
                }
            }

        }
    }

    private static final class IntPair {
        final int a;
        final int f;

        IntPair(int a, int f) {
            this.a = a;
            this.f = f;
        }
    }

    /**
     * N - arr.length
     * <p>
     * time: O(N)
     * space: O(1), in place
     */
    private static boolean nextPermutation(int[] arr) {

        final int last = arr.length - 1;

        for (int i = last - 1; i >= 0; --i) {
            if (arr[i] < arr[i + 1]) {
                for (int k = last; k > i; --k) {
                    if (arr[k] > arr[i]) {
                        swap(arr, k, i);
                        reverse(arr, i + 1, last);
                        break;
                    }
                }

                return true;
            }
        }

        return false;
    }

    private static void swap(int[] arr, int k, int i) {
        int temp = arr[k];
        arr[k] = arr[i];
        arr[i] = temp;
    }

    private static void reverse(int[] arr, int from, int to) {

        int left = from;
        int right = to;

        while (left < right) {
            swap(arr, left, right);
            ++left;
            --right;
        }

    }


    private static int toNumber(int[] arr, int from, int to) {
        int value = 0;

        for (int i = from; i <= to; ++i) {
            value = value * 10 + arr[i];
        }

        return value;
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
            new Uva_725_second();
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
