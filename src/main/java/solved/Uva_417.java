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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Word Index (https://vjudge.net/problem/UVA-417)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_417 {

    private Uva_417() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            Map<String, Integer> map = createIndexMap();

            String word = rd.readLine();

            while (word != null) {

                Integer index = map.get(word.trim());

                if (index == null) {
                    out.println("0");
                }
                else {
                    out.println(index);
                }

                word = rd.readLine();
            }

            diff();
        }
    }

    private static Map<String, Integer> createIndexMap() {
        Queue<String> q = new ArrayDeque<>(83_681);

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            q.add(String.valueOf(ch));
        }

        Map<String, Integer> map = new HashMap<>();

        int index = 1;

        while (!q.isEmpty()) {
            String str = q.poll();

            map.put(str, index);
            ++index;

            List<String> nextValues = calculateNext(str);

            q.addAll(nextValues);
        }

        return map;
    }

    private static List<String> calculateNext(String str) {

        if (str.length() >= 5) {
            return Collections.emptyList();
        }

        char lastCh = str.charAt(str.length() - 1);

        List<String> newValues = new ArrayList<>();
        for (char ch = (char) (lastCh + 1); ch <= 'z'; ++ch) {
            newValues.add(str + ch);
        }

        return newValues;
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
            new Uva_417();
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
