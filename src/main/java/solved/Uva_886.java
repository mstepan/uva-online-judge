package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Uva-886: Named Extension Dialing
 * <p>
 * https://uva.onlinejudge.org
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_886 {

    private Uva_886() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            String line = rd.readLine();

            PhoneBook book = new PhoneBook();

            while (line != null) {
                String[] data = line.trim().split("\\s+");

                // firstName, lastName, extension
                if (data.length == 3) {
                    book.addPerson(data[0].toLowerCase(), data[1].toLowerCase(), data[2].toLowerCase());
                }
                // search number
                else {
                    List<String> matchedExtensions = book.find(data[0].toLowerCase());

                    StringBuilder buf = new StringBuilder();

                    for (String singleMatchedExtension : matchedExtensions) {

                        if (buf.length() > 0) {
                            buf.append(" ");
                        }

                        buf.append(singleMatchedExtension);
                    }

                    out.println(buf);
                }

                line = rd.readLine();
            }


            diff();
        }
    }

    private static final class PhoneBook {

        private final Set<String> extensions = new HashSet<>();

        private final List<NumbersPair> numbers = new ArrayList<>();

        private boolean sorted = false;

        void addPerson(String firstName, String lastName, String extension) {
            extensions.add(extension);
            numbers.add(NumbersPair.create(firstName, lastName, extension));
        }

        List<String> find(String searchNumber) {

            if (!sorted) {
                numbers.sort(Comparator.comparing(pair -> pair.number));
                sorted = true;
            }

            if (extensions.contains(searchNumber)) {
                return Collections.singletonList(searchNumber);
            }

            int index = binarySearch(searchNumber);

            if (index == -1) {
                return Collections.singletonList("0");
            }

            return gatherMatched(index, searchNumber);
        }

        private List<String> gatherMatched(int index, String searchNumber) {
            List<NumbersPair> matched = new ArrayList<>();

            for (int i = index; i < numbers.size(); ++i) {

                NumbersPair curPair = numbers.get(i);

                if (curPair.isPrefixMatched(searchNumber)) {
                    matched.add(curPair);
                }
                else {
                    break;
                }
            }

            return matched.stream().
                    map(pair -> pair.extension).
                    sorted().
                    collect(Collectors.toList());
        }

        private int binarySearch(String searchNumber) {

            int lo = 0;
            int hi = numbers.size() - 1;

            int foundIndex = -1;

            while (lo <= hi) {
                int mid = lo + ((hi - lo) / 2);

                NumbersPair midPair = numbers.get(mid);

                if (midPair.isPrefixMatched(searchNumber)) {
                    foundIndex = mid;
                    hi = mid - 1;
                }
                else if (searchNumber.compareTo(midPair.number) < 0) {
                    hi = mid - 1;
                }
                else {
                    lo = mid + 1;
                }
            }

            return foundIndex;
        }

        private static final class NumbersPair {

            private static final Map<Character, Integer> CH_TO_DIGIT = new HashMap<>();

            static {
                CH_TO_DIGIT.put('a', 2);
                CH_TO_DIGIT.put('b', 2);
                CH_TO_DIGIT.put('c', 2);

                CH_TO_DIGIT.put('d', 3);
                CH_TO_DIGIT.put('e', 3);
                CH_TO_DIGIT.put('f', 3);

                CH_TO_DIGIT.put('g', 4);
                CH_TO_DIGIT.put('h', 4);
                CH_TO_DIGIT.put('i', 4);

                CH_TO_DIGIT.put('j', 5);
                CH_TO_DIGIT.put('k', 5);
                CH_TO_DIGIT.put('l', 5);

                CH_TO_DIGIT.put('m', 6);
                CH_TO_DIGIT.put('n', 6);
                CH_TO_DIGIT.put('o', 6);

                CH_TO_DIGIT.put('p', 7);
                CH_TO_DIGIT.put('q', 7);
                CH_TO_DIGIT.put('r', 7);
                CH_TO_DIGIT.put('s', 7);

                CH_TO_DIGIT.put('t', 8);
                CH_TO_DIGIT.put('u', 8);
                CH_TO_DIGIT.put('v', 8);

                CH_TO_DIGIT.put('w', 9);
                CH_TO_DIGIT.put('x', 9);
                CH_TO_DIGIT.put('y', 9);
                CH_TO_DIGIT.put('z', 9);
            }

            final String number;
            final String extension;

            NumbersPair(String number, String extension) {
                this.number = number;
                this.extension = extension;
            }

            static NumbersPair create(String firstName, String lastName, String extension) {
                StringBuilder key = new StringBuilder();
                key.append(CH_TO_DIGIT.get(firstName.charAt(0)));

                for (int i = 0; i < lastName.length(); ++i) {
                    key.append(CH_TO_DIGIT.get(lastName.charAt(i)));
                }

                return new NumbersPair(key.toString(), extension);
            }

            boolean isPrefixMatched(String other) {

                if (other.length() > number.length()) {
                    return false;
                }

                for (int i = 0; i < other.length(); ++i) {
                    if (other.charAt(i) != number.charAt(i)) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public String toString() {
                return number + " => " + extension;
            }
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
            new Uva_886();
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
