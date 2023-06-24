import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <a href="https://vjudge.net/problem/UVA-xxx">UVA-xxx: Title</a>
 */
public class Main {

    private static void mainLogic() throws IOException, InterruptedException {
        try (PrintStream out = createOutput();
             BufferedReader reader = new BufferedReader(new InputStreamReader(createInput()))) {

            String line;

            while( (line = reader.readLine()) != null){
                out.println(line);
            }

            diff();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // UTILS
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read array from input stream, each element of an array is read as a separate new line.
     */
    private static int[] readArray(BufferedReader reader, int length) throws IOException {
        final int[] arr = new int[length];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = Integer.parseInt(reader.readLine());
        }

        return arr;
    }

    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static final String DIFF_TOOL = "/usr/bin/diff";

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            mainLogic();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getPathFromResourceFolder(String filePath) {
        return Objects.requireNonNull(MethodHandles.lookup().lookupClass().getResource(filePath)).getPath();
    }

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get(getPathFromResourceFolder("in.txt")));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(Paths.get(getPathFromResourceFolder("out-actual.txt"))));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {
        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
                .exec(java.lang.String.format("%s %s %s", DIFF_TOOL, getPathFromResourceFolder("out.txt"),
                        getPathFromResourceFolder("out-actual.txt")));

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
    }

    private static class StreamGobbler implements Runnable {

        private final InputStream inputStream;

        private final Consumer<String> consumer;


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
