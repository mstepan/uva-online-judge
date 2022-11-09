package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * 487--3279 (https://vjudge.net/problem/UVA-755)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_755 {


    private Uva_755() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int dataSetsCount = Integer.parseInt(rd.readLine().trim());

            for (int test = 0; test < dataSetsCount; ++test) {

                // skip empty line
                rd.readLine();

                final int phonesCount = Integer.parseInt(rd.readLine().trim());

                Map<String, Integer> allPhones = new TreeMap<>();

                for (int phoneIndex = 0; phoneIndex < phonesCount; ++phoneIndex) {
                    String singlePhone = normalizePhone(rd.readLine().trim());
                    allPhones.compute(singlePhone, (key, count) -> count == null ? 1 : count + 1);
                }

                if( test != 0 ){
                    out.println();
                }

                if (hasDuplicates(allPhones)) {
                    for (Map.Entry<String, Integer> entry : allPhones.entrySet()) {
                        if (entry.getValue() > 1) {
                            out.printf("%s %d%n", formatPhone(entry.getKey()), entry.getValue());
                        }
                    }
                }
                else {
                    out.println("No duplicates.");
                }
            }

            diff();
        }
    }

    private boolean hasDuplicates(Map<String, Integer> allPhones) {

        for (Integer value : allPhones.values()) {
            if (value > 1) {
                return true;
            }
        }

        return false;
    }

    private String formatPhone(String phone) {
        return phone.substring(0, 3) + "-" + phone.substring(3);
    }

    private static final int[] CHAR_TO_INT = {
            2, 2, 2,
            3, 3, 3,
            4, 4, 4,
            5, 5, 5,
            6, 6, 6,
            7, 7, 7,
            8, 8, 8,
            9, 9, 9
    };

    private static final int EXPECTED_PHONE_LENGTH = 7;

    private static String normalizePhone(String phone) {

        StringBuilder res = new StringBuilder(EXPECTED_PHONE_LENGTH);

        for (int i = 0; i < phone.length(); ++i) {
            char ch = phone.charAt(i);

            if (Character.isDigit(ch)) {
                res.append(ch);
            }
            else if (Character.isAlphabetic(ch)) {

                char smallCh = Character.toLowerCase(ch);

                int index = smallCh - 'a';

                if (smallCh > 'q') {
                    --index;
                }

                res.append(CHAR_TO_INT[index]);
            }
        }

        assert res.length() == EXPECTED_PHONE_LENGTH : "incorrect phone length detected, expected: " + EXPECTED_PHONE_LENGTH +
                ", actual: " + res.length();

        return res.toString();
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
            new Uva_755();
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
