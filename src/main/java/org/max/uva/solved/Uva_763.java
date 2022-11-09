package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * 763 - Fibinary Numbers.
 */
public class Uva_763 {


    private Uva_763() throws IOException, InterruptedException {

        int testCnt = 0;

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            while (true) {

                String str1 = rd.readLine();

                if (str1 == null) {
                    break;
                }

                String str2 = rd.readLine();
                rd.readLine();

                BigInteger[] sequence = calculateFibonacciSequence(Math.max(str1.length(), str2.length()));

                BigInteger num1 = toNumber(str1, sequence);
                BigInteger num2 = toNumber(str2, sequence);

                BigInteger res = num1.add(num2);

                if( testCnt != 0 ){
                    out.println();
                }
                out.println(toFibinary(res));
                ++testCnt;
            }


            diff();
        }
    }

    private static String toFibinary(BigInteger value) {

        BigInteger first = BigInteger.ONE;
        BigInteger second = BigInteger.valueOf(2);

        while (second.compareTo(value) <= 0) {
            BigInteger temp = second;

            second = second.add(first);
            first = temp;
        }

        assert first.compareTo(value) <= 0;

        StringBuilder str = new StringBuilder();

        while (!first.equals(second)) {

            if (first.compareTo(value) <= 0) {
                value = value.subtract(first);
                str.append("1");
            }
            else {
                str.append("0");
            }

            BigInteger prev = second.subtract(first);
            second = first;
            first = prev;
        }

        return str.toString();
    }

    private BigInteger toNumber(String str, BigInteger[] sequence) {

        BigInteger res = BigInteger.ZERO;

        for (int i = str.length() - 1, index = 0; i >= 0; --i, ++index) {
            assert i <= sequence.length : "Boom";

            if (str.charAt(i) == '1') {
                res = res.add(sequence[index]);
            }
        }

        return res;
    }

    private BigInteger[] calculateFibonacciSequence(int length) {

        assert length > 0 : "0 length passed";

        BigInteger[] res = new BigInteger[length];

        BigInteger first = BigInteger.valueOf(1);
        BigInteger second = BigInteger.valueOf(2);

        for (int i = 0; i < res.length; ++i) {
            res[i] = first;

            BigInteger temp = second;

            second = second.add(first);
            first = temp;
        }

        return res;
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
            new Uva_763();
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
