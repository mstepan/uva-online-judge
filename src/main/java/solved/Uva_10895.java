package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 10895 - Matrix Transpose
 */
public class Uva_10895 {

    private Uva_10895() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String line = rd.readLine();

                if (line == null) {
                    break;
                }

                String[] dimension = line.trim().split("\\s+");

                final int rowsCnt = Integer.parseInt(dimension[0]);
                final int colsCnt = Integer.parseInt(dimension[1]);

                List[] transposed = buildTransposed(rd, rowsCnt, colsCnt);

                printTransposedMatrix(out, colsCnt, rowsCnt, transposed);
            }

            diff();
        }
    }

    private static List[] buildTransposed(BufferedReader rd, int rowsCnt, int colsCnt) throws IOException {

        List[] transposed = new List[colsCnt];

        for (int row = 0; row < rowsCnt; ++row) {

            String[] positions = rd.readLine().trim().split("\\s+");
            String[] elements = rd.readLine().trim().split("\\s+");

            for (int posIndex = 1; posIndex < positions.length; ++posIndex) {

                int listIndex = Integer.parseInt(positions[posIndex]) - 1;

                if (transposed[listIndex] == null) {
                    transposed[listIndex] = new ArrayList<ColumnAndValue>();
                }

                transposed[listIndex].add(new ColumnAndValue(row, Integer.parseInt(elements[posIndex - 1])));
            }
        }
        return transposed;
    }

    private static void printTransposedMatrix(PrintStream out, int colsCnt, int rowsCnt, List[] transposed) {
        out.printf("%d %d%n", colsCnt, rowsCnt);

        for (List<ColumnAndValue> transposedRow : transposed) {
            if (transposedRow == null) {
                out.println("0");
                out.println();
            }
            else {
                out.printf("%d %s%n", transposedRow.size(), combineToString(transposedRow,
                        colAndVal -> String.valueOf(colAndVal.column + 1)));

                out.printf("%s%n", combineToString(transposedRow,
                        colAndVal -> String.valueOf(colAndVal.value)));
            }
        }
    }

    private static String combineToString(List<ColumnAndValue> row, Function<ColumnAndValue, String> extract) {
        StringBuilder buf = new StringBuilder();

        for (ColumnAndValue colAndVal : row) {
            buf.append(extract.apply(colAndVal)).append(" ");
        }

        return buf.toString().trim();
    }

    private static class ColumnAndValue {
        final int column;
        final int value;

        ColumnAndValue(int column, int value) {
            this.column = column;
            this.value = value;
        }


        @Override
        public String toString() {
            return "column: " + column + ", value: " + value;
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
            new Uva_10895();
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
