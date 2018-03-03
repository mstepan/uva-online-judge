package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 401 - Palindromes
 */
public class Uva_401 {

    private Uva_401() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (Scanner sc = new Scanner(in)) {

            while (sc.hasNext()) {
                String str = sc.next();

                if (isMirroredPalindrome(str)) {
                    out.printf("%s -- is a mirrored palindrome.", str);
                }
                else if (isPalindrome(str)) {
                    out.printf("%s -- is a regular palindrome.", str);
                }
                else if (isMirroredString(str)) {
                    out.printf("%s -- is a mirrored string.", str);
                }
                else {
                    out.printf("%s -- is not a palindrome.", str);
                }

                out.printf("%n%n");
            }

            diff();
        }
    }

    private static boolean isMirroredPalindrome(String str) {
        return isPalindrome(str) && isMirroredString(str);
    }

    private static boolean isPalindrome(String str) {

        char[] arr = str.toCharArray();

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            if (arr[left] != arr[right]) {
                return false;
            }

            ++left;
            --right;
        }
        return true;
    }

    private static final Map<Character, Character> REVERSE = new HashMap<>();

    static {
        REVERSE.put('A', 'A');
        REVERSE.put('E', '3');
        REVERSE.put('H', 'H');
        REVERSE.put('I', 'I');
        REVERSE.put('J', 'L');
        REVERSE.put('L', 'J');
        REVERSE.put('M', 'M');
        REVERSE.put('O', 'O');
        REVERSE.put('S', '2');
        REVERSE.put('T', 'T');
        REVERSE.put('U', 'U');
        REVERSE.put('V', 'V');
        REVERSE.put('W', 'W');
        REVERSE.put('X', 'X');
        REVERSE.put('Y', 'Y');
        REVERSE.put('Z', '5');
        REVERSE.put('1', '1');
        REVERSE.put('2', 'S');
        REVERSE.put('3', 'E');
        REVERSE.put('5', 'Z');
        REVERSE.put('8', '8');
    }

    private static boolean isMirroredString(String str) {
        char[] arr = str.toCharArray();

        for (char ch : arr) {
            if (!REVERSE.containsKey(ch)) {
                return false;
            }
        }

        char[] reverseArr = new char[arr.length];

        for (int i = 0; i < reverseArr.length; ++i) {
            reverseArr[i] = REVERSE.get(arr[i]);
        }

        reverse(reverseArr);

        return Arrays.equals(arr, reverseArr);
    }

    private static void reverse(char[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {

            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;

            ++left;
            --right;
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
            new Uva_401();
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
