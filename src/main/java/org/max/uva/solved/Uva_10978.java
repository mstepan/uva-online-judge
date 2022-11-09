package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Let's Play Magic! (https://vjudge.net/problem/UVA-10978)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10978 {

    private Uva_10978() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int cardsCount = Integer.parseInt(rd.readLine().trim());

            while (cardsCount != 0) {

                CardsDeck cards = new CardsDeck(cardsCount);

                for (int i = 0; i < cardsCount; ++i) {
                    String[] data = rd.readLine().trim().split(" ");

                    final String card = data[0];
                    final String fullName = data[1];

                    cards.place(fullName, card);
                }

                out.println(cards.toStringWithSpaces());

                cardsCount = Integer.parseInt(rd.readLine().trim());
            }

            diff();
        }
    }

    private static final class CardsDeck {

        final String[] cards;
        int index;

        CardsDeck(int size) {
            this.cards = new String[size];
            this.index = cards.length - 1;
        }

        void place(String fullName, String card) {

            for (int i = 0; i < fullName.length(); ++i) {
                moveIndexToNextFreeSlot();
            }

            cards[index] = card;
        }

        private void moveIndexToNextFreeSlot() {

            moveOnePosition();

            while (cards[index] != null) {
                moveOnePosition();
            }
        }

        void moveOnePosition() {
            index = (index + 1) % cards.length;
        }

        String toStringWithSpaces() {
            StringBuilder buf = new StringBuilder(3 * cards.length);

            buf.append(cards[0]);

            for (int i = 1; i < cards.length; ++i) {
                buf.append(" ").append(cards[i]);
            }

            return buf.toString();
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
            new Uva_10978();
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
