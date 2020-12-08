import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * G. Парные забеги
 * https://codeforces.com/problemset/problem/1244/G
 */
public class Main {

    private Main() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            int testCasesCount = Integer.parseInt(rd.readLine().trim());

            for (int i = 0; i < testCasesCount; ++i) {
                int n = Integer.parseInt(rd.readLine().trim());
                String map = rd.readLine().trim();

                out.println(maxRoomVisited(map, n));
            }


            diff();
        }
    }

    private int maxRoomVisited(String map, int n) {

        int leftDoor = findLeftDoor(map);

        if (leftDoor == -1) {
            return n;
        }

        int rightDoor = findRightDoor(map);

        int leftCount = leftDoor + 1;
        int rightCount = n - rightDoor;

        int maxFromLeft = Math.max(2 * leftCount, leftCount + (n-leftDoor));

        int maxFromRight = Math.max(2 * rightCount, rightCount + rightDoor + 1);

        return  Math.max(maxFromLeft, maxFromRight);
    }

    private int findLeftDoor(String map) {
        for (int i = 0; i < map.length(); ++i) {
            if (map.charAt(i) == '1') {
                return i;
            }
        }

        return -1;
    }

    private int findRightDoor(String map) {
        for (int i = map.length() - 1; i >= 0; --i) {
            if (map.charAt(i) == '1') {
                return i;
            }
        }

        return -1;
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
                .exec(java.lang.String.format("/usr/bin/diff %s %s",
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
            new Main();
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
