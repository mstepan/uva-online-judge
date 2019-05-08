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
 * Soundex (https://vjudge.net/problem/UVA-10260)
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_10260 {


    private Uva_10260() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String word = rd.readLine();

            while (word != null) {

                word = word.trim();

                out.println(encodeAsSoundex(word));

                word = rd.readLine();
            }

            diff();
        }
    }

    private static final Map<Character, Character> SOUNDEX_CODES = new HashMap<>();

    static {
        SOUNDEX_CODES.put('B', '1');
        SOUNDEX_CODES.put('F', '1');
        SOUNDEX_CODES.put('P', '1');
        SOUNDEX_CODES.put('V', '1');

        SOUNDEX_CODES.put('C', '2');
        SOUNDEX_CODES.put('G', '2');
        SOUNDEX_CODES.put('J', '2');
        SOUNDEX_CODES.put('K', '2');
        SOUNDEX_CODES.put('Q', '2');
        SOUNDEX_CODES.put('S', '2');
        SOUNDEX_CODES.put('X', '2');
        SOUNDEX_CODES.put('Z', '2');

        SOUNDEX_CODES.put('D', '3');
        SOUNDEX_CODES.put('T', '3');

        SOUNDEX_CODES.put('L', '4');

        SOUNDEX_CODES.put('M', '5');
        SOUNDEX_CODES.put('N', '5');

        SOUNDEX_CODES.put('R', '6');
    }

    private String encodeAsSoundex(String word) {

        StringBuilder buf = new StringBuilder(word.length());

        Character lastEncodedSoundexCh = null;

        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);

            if (SOUNDEX_CODES.containsKey(ch)) {
                char encodedCh = SOUNDEX_CODES.get(ch);

                if (lastEncodedSoundexCh == null || lastEncodedSoundexCh != encodedCh) {
                    buf.append(encodedCh);
                    lastEncodedSoundexCh = encodedCh;
                }
            }
            else {
                lastEncodedSoundexCh = null;
            }
        }

        return buf.toString();
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
            new Uva_10260();
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
