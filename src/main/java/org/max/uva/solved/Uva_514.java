package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * 514 - Rails
 */
public class Uva_514 {


    private Uva_514() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                final int cnt = Integer.parseInt(rd.readLine().trim());

                if (cnt == 0) {
                    break;
                }

                while (true) {
                    String line = rd.readLine().trim();

                    if ("0".equals(line)) {
                        break;
                    }

                    int[] train = createTrain(line);

                    if (canReshuffle(train, cnt)) {
                        out.println("Yes");
                    }
                    else {
                        out.println("No");
                    }
                }

                out.println();
            }

            diff();
        }
    }

    private static boolean canReshuffle(int[] train, int cnt) {

        Deque<Integer> stack = new ArrayDeque<>();
        int cur = 1;

        for (int i = 0; i < train.length; ) {
            if (stack.isEmpty() || stack.peekFirst() != train[i]) {

                if (cur > cnt) {
                    return false;
                }

                stack.push(cur);
                ++cur;
            }
            else {
                stack.pop();
                ++i;
            }
        }

        return true;
    }

    private static int[] createTrain(String trainStr) {
        String[] trainData = trainStr.split("\\s+");

        int[] train = new int[trainData.length];

        for (int i = 0; i < trainData.length; ++i) {
            train[i] = Integer.parseInt(trainData[i]);
        }

        return train;
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
            new Uva_514();
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
