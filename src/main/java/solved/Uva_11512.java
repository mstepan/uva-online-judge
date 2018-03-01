package solved;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 11512 - GATTACA
 */
public class Uva_11512 {

    static final class Substring implements Comparable<Substring> {

        final String base;
        final int from;

        Substring(String base, int from) {
            this.base = base;
            this.from = from;
        }

        @Override
        public int compareTo(Substring other) {
            int first = from;
            int second = other.from;

            while (first < base.length() && second < other.base.length()) {

                char ch1 = base.charAt(first);
                char ch2 = other.base.charAt(second);

                int cmp = Character.compare(ch1, ch2);

                if (cmp != 0) {
                    return cmp;
                }

                ++first;
                ++second;
            }

            if (first == base.length()) {
                return second == other.base.length() ? 0 : -1;
            }

            return 1;
        }

        int length() {
            return base.length() - from;
        }

        char charAt(int index) {
            return base.charAt(from + index);
        }

        @Override
        public String toString() {
            return base.substring(from);
        }
    }


    private static final class RepetitionAndCount {

        private static final RepetitionAndCount EMPTY = new RepetitionAndCount("", -1);

        final String str;
        final int count;

        RepetitionAndCount(String str, int count) {
            this.str = str;
            this.count = count;
        }
    }


    /**
     * time: O(N^2)
     * space: O(N)
     */
    private static RepetitionAndCount longestRepeatedSubstring(String str) {

        Substring[] suffixArray = new Substring[str.length()];

        for (int i = 0; i < str.length(); ++i) {
            suffixArray[i] = new Substring(str, i);
        }

        Arrays.sort(suffixArray);

        String longestRepeated = "";
        int cnt = 0;

        for (int i = 1; i < suffixArray.length; ++i) {

            String prefix = findCommonPrefix(suffixArray[i - 1], suffixArray[i]);

            if (prefix.length() > longestRepeated.length()) {
                longestRepeated = prefix;
                cnt = 2;
            }
            else if (prefix.equals(longestRepeated)) {
                ++cnt;
            }
        }

        return longestRepeated.length() == 0 ? RepetitionAndCount.EMPTY : new RepetitionAndCount(longestRepeated, cnt);
    }

    private static String findCommonPrefix(Substring first, Substring second) {

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < Math.min(first.length(), second.length()); ++i) {
            if (first.charAt(i) != second.charAt(i)) {
                break;
            }

            buf.append(first.charAt(i));
        }

        return buf.toString();
    }

    private Uva_11512(boolean debugMode) throws IOException {

        InputStream in = System.in;

        if (debugMode) {
            in = Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }

        try (Scanner sc = new Scanner(in)) {

            int dnasCount = sc.nextInt();

            for (int i = 0; i < dnasCount; ++i) {
                String dna = sc.next();
                RepetitionAndCount repeated = longestRepeatedSubstring(dna);

                if (repeated == RepetitionAndCount.EMPTY) {
                    System.out.println("No repetitions found!");
                }
                else {
                    System.out.printf("%s %d%n", repeated.str, repeated.count);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Uva_11512(args.length == 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

